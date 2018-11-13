package edu.pnu.stem.feature;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Polygon;

import java.util.ArrayList;

/**
 * Created by STEM_KTH on 2018-11-03.
 * @author Taehoon Kim, Pusan National University, STEM Lab.
 */
public class Room {
    public static double tolerance = 0.2;
    private String label;   // Name to represent space
    private Polygon floorGeometry;   // Geometry expressing floor space
    private Polygon ceilingGeometry;   // Geometry expressing ceiling space
    private ArrayList<LineString> doors;    // An array that stores the geometry expressing door. The door is assumed to be represented by a LineString
    private ArrayList<LineString> visibilityEdges;  // A collection of edges of the graph that represent visibility from point to point in geometry that represents space
    private ArrayList<LineString> door2doorEdges;   // An array that stores the path between doors
    private GeometryFactory gf;

    public Room() {
        doors = new ArrayList<>();
        visibilityEdges = new ArrayList<>();
        door2doorEdges = new ArrayList<>();
    }

    public Room(Polygon floorGeometry) {
        this();
        setFloorGeometry(floorGeometry);
    }

    public ArrayList<LineString> getDoor2doorEdges() {
        return door2doorEdges;
    }

    public ArrayList<LineString> getVisibilityEdges() {
        return visibilityEdges;
    }

    public ArrayList<LineString> getDoors() {
        return doors;
    }

    public Polygon getFloorGeometry() {
        return floorGeometry;
    }

    public Polygon getCeilingGeometry() {
        return ceilingGeometry;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setFloorGeometry(Polygon floorGeometry) {
        this.floorGeometry = floorGeometry;
        setVisibilityGraphEdges();
        // TODO : Make d2d distance when add holes
    }

    public void setCeilingGeometry(Polygon ceilingGeometry) {
        this.ceilingGeometry = ceilingGeometry;
    }

    /**
     * Add a new door.
     * If there are more than two doors, creating and storing the path between doors.
     *
     * @param newDoor new door to add
     *
     * TODO : How to reuse visibility graph? need optimization
     * */
    public void addDoors(LineString newDoor) {
        GeometryFactory gf = new GeometryFactory();
        doors.add(newDoor);

        if(visibilityEdges.isEmpty()) {
            setVisibilityGraphEdges();
        }

        for(Coordinate coord : newDoor.getCoordinates()) {
            addNodeToVGraph(coord, visibilityEdges);
        }

        if(doors.size() > 1) {
            VisibilityGraph graph = new VisibilityGraph();
            graph.addEdges(visibilityEdges);

            for (LineString existDoor : doors) {
                if(existDoor.equals(newDoor) || existDoor.reverse().equals(newDoor))
                    continue;
                for(Coordinate from : existDoor.getCoordinates()) {
                    for(Coordinate to : newDoor.getCoordinates()) {
                        Coordinate[] coords = new Coordinate[]{from, to};
                        LineString directPath = gf.createLineString(coords);

                        if(floorGeometry.buffer(tolerance).covers(directPath)) {
                            door2doorEdges.add(directPath);
                            door2doorEdges.add((LineString) directPath.reverse());
                        }
                        else {
                            LineString d2dPath = graph.getShortestRoute(coords);
                            if(d2dPath !=null && floorGeometry.buffer(tolerance).covers(d2dPath)) {
                                door2doorEdges.add(d2dPath);
                                door2doorEdges.add((LineString) d2dPath.reverse());
                            }
                            else
                                System.out.println("d2d path isn't covered by floorGeometry");
                        }
                    }
                }
            }
        }
    }

    /**
     * Make visibilityEdges.
     * visibilityEdges is a collection of edges of the graph that represent visibility from point to point in geometry that represents space.
     * */
    private void setVisibilityGraphEdges() {
        visibilityEdges.clear();
        Coordinate[] coords = floorGeometry.getCoordinates();
        for (Coordinate from : coords) {
            visibilityEdges = addNodeToVGraph(from, visibilityEdges);
        }
    }

    /**
     * Add a node to VisibilityGraph.
     * Determine if the straight line from "from" to "to" coordinates is included in the geometry of the space.
     * If it included in room geometry, add it to visibilityEdgeList.
     *
     * @param from start coordinate of straight line
     * @param visibilityEdgeList List of edges of visible graph
     * @return added visibilityEdgeList
     * */
    private ArrayList<LineString> addNodeToVGraph(Coordinate from, ArrayList<LineString> visibilityEdgeList){
        GeometryFactory gf = new GeometryFactory();
        Coordinate[] coords = floorGeometry.getCoordinates();
        for (Coordinate to : coords) {
            if (from.equals(to)) continue;

            Coordinate[] edgeCoords = new Coordinate[] {from, to};
            LineString edge = gf.createLineString(edgeCoords);

            // When room geometry covers the straight line (visibility line)
            if (floorGeometry.buffer(tolerance).covers(edge)) {
                if(!visibilityEdgeList.contains(edge))
                    visibilityEdgeList.add(edge);
            }
        }

        return visibilityEdgeList;
    }

    /**
     * Temporarily adding nodes to visible graph.
     * Add edges if the straight line from "startP(or endP)" to coordinates of space geometry is included in the geometry of the space.
     * This function is mainly used when creating a visible graph to find the indoor path from startP to endP in Room.
     *
     * @param startP start point of indoor path
     * @param endP end point of indoor path
     * @return Edge list of visible graphs reflecting temporarily added nodes
     * */
    public ArrayList<LineString> addNodeToVGraph(Coordinate startP, Coordinate endP){
        ArrayList<LineString> temporalResult = (ArrayList<LineString>) visibilityEdges.clone();
        temporalResult = addNodeToVGraph(startP, temporalResult);
        temporalResult = addNodeToVGraph(endP, temporalResult);

        return temporalResult;
    }
}
