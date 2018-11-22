package edu.pnu.stem.sample;

import edu.pnu.stem.binder.Convert2FeatureClass;
import edu.pnu.stem.binder.IndoorGMLMap;
import edu.pnu.stem.binder.Unmashaller;
import edu.pnu.stem.common.CommonIndoorUtils;
import edu.pnu.stem.feature.core.IndoorFeatures;
import net.opengis.indoorgml.core.v_1_0.IndoorFeaturesType;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;

public class SampleApplication {

    public static void main(String[] args) {
        String indoorGMLDataURL;
        Coordinate startPoint;
        Coordinate endPoint;

        if(args.length != 0) {
            indoorGMLDataURL = args[0];
            startPoint = new Coordinate(Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]));
            endPoint = new Coordinate(Double.parseDouble(args[4]), Double.parseDouble(args[5]), Double.parseDouble(args[6]));

            IndoorGMLMap map = new IndoorGMLMap();
            IndoorFeaturesType doc;
            IndoorFeatures indoorFeatures;

            map.setDocId("test");
            try {
                doc = Unmashaller.importIndoorGML("test",indoorGMLDataURL);
                indoorFeatures = Convert2FeatureClass.change2FeatureClass(map,"test", doc);
                Coordinate start = startPoint;//new Coordinate(80,170,10);
                Coordinate end = endPoint;//new Coordinate(30,110, 20);

                LineString indoorRoute = CommonIndoorUtils.getIndoorRoute(start, end, map, indoorFeatures);
                System.out.print("Indoor Route: ");
                for(Coordinate coord: indoorRoute.getCoordinates()) {
                    System.out.print(coord.toString());
                    if(!coord.equals3D(indoorRoute.getEndPoint().getCoordinate())) {
                        System.out.print(" -> ");
                    }
                    else{
                        System.out.println();
                    }
                }
                System.out.println("Indoor Distance is " + CommonIndoorUtils.get3DLength(indoorRoute));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println("This CLI application need seven parameters");
            System.out.println("Please input parameters as follow [IndoorGML data location] [Start point x y z] [End point x y z]");
        }
     }
}
