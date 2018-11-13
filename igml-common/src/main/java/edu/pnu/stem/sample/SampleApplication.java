package edu.pnu.stem.sample;

import edu.pnu.stem.binder.Convert2FeatureClass;
import edu.pnu.stem.binder.IndoorGMLMap;
import edu.pnu.stem.binder.Unmashaller;
import edu.pnu.stem.common.IndoorUtils;
import edu.pnu.stem.feature.core.IndoorFeatures;
import net.opengis.indoorgml.core.v_1_0.IndoorFeaturesType;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;

public class SampleApplication {

    public static void main(String[] args) {
         IndoorGMLMap map = new IndoorGMLMap();
         IndoorFeaturesType doc;
         IndoorFeatures indoorFeatures;

         map.setDocId("test");
         try {
             doc = Unmashaller.importIndoorGML("test","igml-common/src/main/resources/navi_thin.gml");
             indoorFeatures = Convert2FeatureClass.change2FeatureClass(map,"test", doc);
             Coordinate start = new Coordinate(80,170,10);
             Coordinate end = new Coordinate(30,110, 20);

             LineString indoorRoute = IndoorUtils.getIndoorRoute(start, end, map, indoorFeatures);
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
             System.out.println("Indoor Distance is " + IndoorUtils.get3DLength(indoorRoute));
         } catch (Exception e) {
             e.printStackTrace();
         }
     }
}
