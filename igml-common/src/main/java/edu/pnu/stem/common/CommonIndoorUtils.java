package edu.pnu.stem.common;

import edu.pnu.stem.binder.IndoorGMLMap;
import edu.pnu.stem.feature.Room;
import edu.pnu.stem.feature.VisibilityGraph;
import edu.pnu.stem.feature.core.CellSpace;
import edu.pnu.stem.feature.core.CellSpaceBoundary;
import edu.pnu.stem.feature.core.IndoorFeatures;
import edu.pnu.stem.feature.core.PrimalSpaceFeatures;
import edu.pnu.stem.geometry.jts.Solid;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.operation.distance3d.Distance3DOp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by STEM_KTH on 2018-11-03.
 * @author Taehoon Kim, Pusan National University, STEM Lab.
 */
public class CommonIndoorUtils {
    public static final int BUFFER_SIZE = 20;
    public static final int EPSILON = 5;
    private static final GeometryFactory gf = new GeometryFactory();


    public static double getIndoorDistance(Coordinate start, Coordinate end, IndoorGMLMap indoorGMLMap, IndoorFeatures indoorFeatures) {
        LineString resultPath = getIndoorRoute(start, end, indoorGMLMap, indoorFeatures);
        return get3DLength(resultPath);
    }

    public static LineString getIndoorRoute(Coordinate start, Coordinate end, IndoorGMLMap indoorGMLMap, IndoorFeatures indoorFeatures) {
        LineString trajectory = gf.createLineString(new Coordinate[]{start, end});
        ArrayList<Room> roomArrayList = new ArrayList<>();
        ArrayList<LineString> doorGeometries = new ArrayList<>();
        // TODO: Need the process for reusing Room variable
        // TODO: Need the exception handling process: execution only when the geometries in IndoorGML have same dimension

        // Create Room objects
        PrimalSpaceFeatures primalSpaceFeatures = indoorFeatures.getPrimalSpaceFeatures();
        for(int i = 0; i < primalSpaceFeatures.getCellSpaceMember().size(); i++){
            CellSpace cellSpace = (CellSpace) indoorGMLMap.getFeature(primalSpaceFeatures.getCellSpaceMember().get(i).getId());

            if(cellSpace.getGeometry().getDimension() == 3) {
                // Case 1: CellSpace has 3-D geometry
                Solid solid = (Solid) cellSpace.getGeometry();

                // Get horizontal polygon
                ArrayList<Polygon> zFlatPolygons = new ArrayList<>();
                MultiPolygon multiPolygon = solid.getShell();
                for(int j = 0; j < multiPolygon.getNumGeometries(); j++) {
                    Polygon polygon = (Polygon) multiPolygon.getGeometryN(j);
                    Coordinate first = polygon.getCoordinate();

                    boolean isFlat = true;
                    for(Coordinate coordinate : polygon.getCoordinates()) {
                        if(!coordinate.equalInZ(first, 0.1)) {
                            isFlat = false;
                            break;
                        }
                    }
                    if(isFlat) {
                        zFlatPolygons.add(polygon);
                    }
                }

                // Distinguish Ceiling and Floor
                Polygon floorGeometry, ceilingGeometry;
                if(zFlatPolygons.get(0).getCoordinate().z > zFlatPolygons.get(1).getCoordinate().z) {
                    ceilingGeometry = zFlatPolygons.get(0);
                    floorGeometry = zFlatPolygons.get(1);
                }
                else {
                    ceilingGeometry = zFlatPolygons.get(1);
                    floorGeometry = zFlatPolygons.get(0);
                }
                Room room = new Room(floorGeometry);
                room.setCeilingGeometry(ceilingGeometry);
                room.setLabel(cellSpace.getId());
                roomArrayList.add(room);
            }
            else {
                // Case 2: CellSpace has 2-D geometry
                Polygon polygon = (Polygon) cellSpace.getGeometry();
                Room room = new Room(polygon);
                room.setLabel(cellSpace.getId());
                roomArrayList.add(room);
            }
        }

        // Create Door objects
        for(int i = 0; i < primalSpaceFeatures.getCellSpaceBoundaryMember().size(); i++){
            CellSpaceBoundary cellSpaceBoundary = (CellSpaceBoundary) indoorGMLMap.getFeature(primalSpaceFeatures.getCellSpaceBoundaryMember().get(i).getId());

            //if(cellSpaceBoundary.getGeometry().getDimension() == 3) {
            // TODO: CellSpaceBoundary's geometry dimension has an error, so necessary to fix it
                // Case 1: CellSpaceBoundary has 3-D geometry
                Polygon polygon = (Polygon) cellSpaceBoundary.getGeometry();
                // Convert polygon to curve, consider only two end points
                // TODO: extract real door geometry (LineString) from polygon
                Coordinate sp = polygon.getCoordinates()[0];
                Coordinate ep = null;
                for(int j = 2; j < polygon.getNumPoints(); j++) {
                    ep = polygon.getCoordinates()[polygon.getNumPoints() - j];
                    if(sp.equalInZ(ep, 0.1)) {
                        break;
                    }
                }
                GeometryFactory gf = new GeometryFactory();
                LineString door = gf.createLineString(new Coordinate[]{sp, ep});
                boolean isEqual = false;
                for(LineString anotherDoor: doorGeometries) {
                    if(anotherDoor.covers(door)) {
                        isEqual = true;
                        break;
                    }
                }
                if(!isEqual) {
                    doorGeometries.add(door);
                }
            //}
            /*
            else {
                // Case 2: CellSpaceBoundary has 2-D geometry
                LineString door = (LineString) cellSpaceBoundary.getGeometry();
                doorGeometries.add(door);
            }
            */
        }

        // Add the door to the room
        for(Room room: roomArrayList) {
            for(LineString door: doorGeometries) {
                if(room.getFloorGeometry().buffer(Room.tolerance).covers(door)) {
                    room.addDoors(door);
                }
            }
        }

        return getIndoorRoute(trajectory, roomArrayList);
    }

    /**
     * This function provides an indoor route that consider the indoor space geometry for a given trajectory.
     *
     * @param trajectory It consists of two points(start and end point) that want to create an indoor route
     * @param roomArrayList List of CellSpace that contains all indoor space information in the building
     * @return Indoor route
     * */
    public static LineString getIndoorRoute(LineString trajectory, ArrayList<Room> roomArrayList) {
        // TODO : Make IndoorRoute with way-points (currently make with just two points)
        LineString resultIndoorPath = null;
        int startPRoomIndex = -1;
        int endPRoomIndex = -1;

        Point startP = trajectory.getStartPoint();
        Point endP = trajectory.getEndPoint();

        int roomIndex = 0;
        for (Room room : roomArrayList) {
            Polygon roomFloorGeometry = room.getFloorGeometry();
            /*
            possible case : locate of trajectory coordinates
            1. contain in a cell
            2. on a cell boundary
            2-1. on a cell door boundary
            */
            if (roomFloorGeometry.covers(startP) && roomFloorGeometry.covers(endP)) {
                /*
                In case : trajectory is included in same cell
                but in this case, one of the trajectory boundary point possible to located at the boundary of the cell
                If it is located at the boundary of the cell,
                Need to check it is also located at the boundary of the door or not
                Because, If it isn't located at the boundary of the door,
                The two points must be distinguished as being located in another space.
                */
                boolean isStartPOnADoor = false;
                boolean isEndPOnADoor = false;
                for(LineString doorGeom : room.getDoors()) {
                    if(doorGeom.getStartPoint().equals(startP) || doorGeom.getEndPoint().equals(startP)) {
                        isStartPOnADoor = true;
                    }
                    if(doorGeom.getStartPoint().equals(endP) || doorGeom.getEndPoint().equals(endP)) {
                        isEndPOnADoor = true;
                    }
                }
                if(!roomFloorGeometry.contains(startP) && !isStartPOnADoor) {
                    endPRoomIndex = roomIndex;
                }
                else if(!roomFloorGeometry.contains(endP) && !isEndPOnADoor) {
                    startPRoomIndex = roomIndex;
                }
                else {
                    startPRoomIndex = endPRoomIndex = roomIndex;
                    resultIndoorPath = makeIndoorRouteInCell(startP, endP, room);
                    break;
                }
            }
            else {
                if(roomFloorGeometry.covers(startP) && startPRoomIndex == -1) {
                    startPRoomIndex = roomIndex;
                }
                else if(roomFloorGeometry.covers(endP) && endPRoomIndex == -1) {
                    endPRoomIndex = roomIndex;
                }
            }
            roomIndex++;
        }

        if(startPRoomIndex == -1 || endPRoomIndex == -1) {
            // For the Thick model, need to handle the case where coordinates are entered between walls
            if(startPRoomIndex == -1) {
                startPRoomIndex = getRoomIndex(startP.getCoordinate(), roomArrayList);
                startP = getNearestPoint(startP, roomArrayList.get(startPRoomIndex).getFloorGeometry());
            }
            if(endPRoomIndex == -1) {
                endPRoomIndex = getRoomIndex(endP.getCoordinate(), roomArrayList);
                endP = getNearestPoint(endP, roomArrayList.get(endPRoomIndex).getFloorGeometry());
            }

            if(startPRoomIndex == -1 || endPRoomIndex == -1) {
                // In case : trajectory is defined outside of the cells
                // TODO : Make alert the path of point is must include in Cell Space
                System.out.println("The coordinates were bounced off the building");
            }
            else if (startPRoomIndex == endPRoomIndex) {
                resultIndoorPath = makeIndoorRouteInCell(startP, endP, roomArrayList.get(startPRoomIndex));
            }
            else {
                resultIndoorPath = getIndoorRoute(startPRoomIndex, endPRoomIndex, startP, endP, roomArrayList);
                if(resultIndoorPath == null) {
                    // In case : There is no connection between start and end
                    // In this case, the endP is corrected to belong to the cell to which startP belongs
                    // There are two ways to do this: Currently implemented in 2
                    // 1. Creates a straight line between startP and endP, corrects to a point intersecting CellBoundary
                    // 2. Corrected to the closest point between endP and CellBoundary
                    // Then find the indoor route in the same cell
                    endP = getNearestPoint(endP, roomArrayList.get(startPRoomIndex).getFloorGeometry());
                    resultIndoorPath = makeIndoorRouteInCell(startP, endP, roomArrayList.get(startPRoomIndex));
                }
            }
        }
        else if(resultIndoorPath == null) {
            resultIndoorPath = getIndoorRoute(startPRoomIndex, endPRoomIndex, startP, endP, roomArrayList);
        }

        return resultIndoorPath;
    }

    /**
     * In case : trajectory cover several cell spaces
     * Consider only same floor's doors connection
     * TODO : Make door2door graph take into account several floors
     *
     * @param startPCellIndex
     * @param endPCellIndex
     * @param startP
     * @param endP
     * @param cellSpaces
     * @return
     */
    private static LineString getIndoorRoute(int startPCellIndex, int endPCellIndex, Point startP, Point endP, ArrayList<Room> cellSpaces) {
        LineString resultIndoorPath = null;
        VisibilityGraph graph = new VisibilityGraph();

        if(startPCellIndex != endPCellIndex) {
            // Makes door2door graph
            if(VisibilityGraph.getBaseGraph() == null) {
                ArrayList<LineString> doors = new ArrayList<>();
                for (Room cellSpace: cellSpaces) {
                    ArrayList<LineString> d2dGraph = cellSpace.getDoor2doorEdges();
                    if(d2dGraph != null && d2dGraph.size() != 0) {
                        graph.addEdges(d2dGraph);
                    }
                    doors.addAll(cellSpace.getDoors());
                }
                // Determine whether the indoor model is thin or thick by door objects
                // If it is a thick model, creates edges for the door2door graph by finding separated two objects but actually same object (door).
                HashSet<LineString> interDoorGraph = new HashSet<>();
                for(int i = 0; i < doors.size(); i++) {
                    for(int j = i + 1; j <  doors.size(); j++) {
                        LineString doorA = doors.get(i);
                        LineString doorB = doors.get(j);
                        if(doorA.equals(doorB)) {
                            break; // In case: thin model
                        }
                        else {
                            if(doorA.buffer(BUFFER_SIZE,2).covers(doorB)) { // In case: thick model
                                interDoorGraph.add(gf.createLineString(new Coordinate[]{doorA.getStartPoint().getCoordinate(), doorB.getStartPoint().getCoordinate()}));
                                interDoorGraph.add(gf.createLineString(new Coordinate[]{doorA.getEndPoint().getCoordinate(), doorB.getEndPoint().getCoordinate()}));
                            }
                        }
                    }
                }
                doors.clear();
                doors.addAll(interDoorGraph);
                graph.addEdges(doors);

                VisibilityGraph.setBaseGraph(graph.getEdges());
            }
            else {
                // Reuse VisibilityGraph object using base graph
                graph = VisibilityGraph.getBaseGraph();
            }
            // Make point2door edges and reflects it to door2door graph
            ArrayList<LineString> start2doorGraph =  makePoint2DoorEdge(startP, cellSpaces.get(startPCellIndex));
            ArrayList<LineString> end2doorGraph =  makePoint2DoorEdge(endP, cellSpaces.get(endPCellIndex));
            graph.addEdges(start2doorGraph);
            graph.addEdges(end2doorGraph);
            // Get point2point shortest path using door2door graph
            if(end2doorGraph.isEmpty()) {
                // In case: a cell that containing the end point is disconnected with a building
                // So, make a point that intersect point between a original line and a cell boundary that containing the start point
                LineString lineString = gf.createLineString(new Coordinate[]{startP.getCoordinate(), endP.getCoordinate()});
                Geometry intersectionP = lineString.intersection(cellSpaces.get(startPCellIndex).getFloorGeometry().getExteriorRing());
                Coordinate newEndPointCoord = intersectionP.getCoordinates()[0];
                resultIndoorPath = gf.createLineString(new Coordinate[]{startP.getCoordinate(), newEndPointCoord});
            }
            else {
                Coordinate[] coords = new Coordinate[]{startP.getCoordinate(), endP.getCoordinate()};
                resultIndoorPath = graph.getShortestRoute(coords);
            }
        }
        else {
            System.out.println("Impossible case: cell Index is same!");
        }

        return resultIndoorPath;
    }

    /**
     * A function that returns all paths from a given point to doors in a target cell spaces.
     *
     * @param point given point
     * @param targetCellSpace target cell space with doors
     * @return all path from point to doors
     * */
    private static ArrayList<LineString> makePoint2DoorEdge(Point point, Room targetCellSpace) {
        ArrayList<LineString> p2dGraph = new ArrayList<>();
        ArrayList<LineString> doors = targetCellSpace.getDoors();
        for (LineString door: doors) {
            for(int i = 0; i < door.getNumPoints(); i++) {
                Point doorP = door.getPointN(i);

                // Exception Case : Ignore If the door's point and given point are the same
                if(!point.equals(doorP)) {
                    LineString point2DoorPath = makeIndoorRouteInCell(point, doorP, targetCellSpace);
                    p2dGraph.add(point2DoorPath);
                    p2dGraph.add((LineString) point2DoorPath.reverse());
                }
            }
        }
        return p2dGraph;
    }

    /**
     * A function to obtain the path between start and end points.
     * This function assumes that the start and end points are in the same cell space.
     *
     * @param startP start point
     * @param endP end point
     * @param cellSpace cell space with geometric information
     * @return Indoor route between start point and end point
     * */
    private static LineString makeIndoorRouteInCell(Point startP, Point endP, Room cellSpace) {
        LineString p2pIndoorPath;
        Polygon cellSpaceGeom = cellSpace.getFloorGeometry();


        if(!cellSpaceGeom.covers(startP)) {
            startP = getNearestPoint(startP, cellSpaceGeom);
        }
        else if(!cellSpaceGeom.covers(endP)) {
            endP = getNearestPoint(endP, cellSpaceGeom);
        }


        Coordinate[] coords = new Coordinate[]{startP.getCoordinate(), endP.getCoordinate()};
        LineString lineString = gf.createLineString(coords);

        // TODO: How to determine that topology relation between a linestring and cell geometry exterior??
        if(cellSpaceGeom.covers(lineString)) {  // in thick model, cover operation is better than contains
            // In case : The Cell contains a straight line between start and end points
            p2pIndoorPath = gf.createLineString(coords);
        }
        else {
            /*
            In case : The Cell doesn't contains a straight line between start and end points
            Make indoor path using the cell's visibility graph that added temp information(start and end points)
            */
            ArrayList<LineString> temporalGraph = cellSpace.addNodeToVGraph(startP.getCoordinate(), endP.getCoordinate());
            VisibilityGraph graph = new VisibilityGraph();
            graph.addEdges(temporalGraph);
            p2pIndoorPath = graph.getShortestRoute(coords);
        }

        return p2pIndoorPath;
    }

    /**
     *
     *
     * @param point
     * @param polygon
     * @return
     */
    private static Point getNearestPoint(Point point, Polygon polygon) {
        Coordinate[] points = Distance3DOp.nearestPoints(polygon, point);
        point = gf.createPoint(points[0]);
        return point;
    }

    /**
     *
     * @param targetCoordinate
     * @param cellSpaces
     * @return
     * */
    public static Integer getRoomIndex(Coordinate targetCoordinate, ArrayList<Room> cellSpaces) {
        int epsilon = EPSILON;
        int cellIndex = getRoomIndex(targetCoordinate, epsilon, cellSpaces);
        while (cellIndex == -1) {
            epsilon *= 2;
            cellIndex = getRoomIndex(targetCoordinate, epsilon, cellSpaces);
        }

        return cellIndex;
    }

    /**
     *
     * @param targetCoordinate
     * @param epsilon
     * @param cellSpaces
     * @return
     * */
    public static Integer getRoomIndex(Coordinate targetCoordinate, double epsilon, ArrayList<Room> cellSpaces) {
        Point point = gf.createPoint(targetCoordinate);
        Polygon bufferedPolygon = (Polygon) point.buffer(epsilon, 2);

        int closestCellIndex = -1;
        HashMap<Integer, Double> resultWithArea = new HashMap<>();
        for (Room cellSpace : cellSpaces) {
            Polygon cellGeometry = cellSpace.getFloorGeometry();
            closestCellIndex++;
            if(cellGeometry.intersects(bufferedPolygon)){
                resultWithArea.put(closestCellIndex, cellGeometry.intersection(bufferedPolygon).getArea());
            }
        }

        double maxArea = 0.0;
        int selectedIndex = -1;
        for(Integer cellIndex : resultWithArea.keySet()) {
            if(maxArea < resultWithArea.get(cellIndex)) {
                maxArea = resultWithArea.get(cellIndex);
                selectedIndex = cellIndex;
            }
        }

        return selectedIndex;
    }

    public static Double get3DLength(LineString lineString) {
        double length3D = 0;
        for(int i = 0; i < lineString.getCoordinates().length - 1; i++) {
            Coordinate from = lineString.getCoordinates()[i];
            Coordinate to = lineString.getCoordinates()[i+1];

            length3D += from.distance3D(to);
        }

        return length3D;
    }
}
