package edu.pnu.stem.geojson;


import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTReader;

import edu.pnu.stem.geometry.jts.WKTReader3D;

import com.vividsolutions.jts.geom.Point;

import org.wololo.jts2geojson.*;
import org.wololo.geojson.*;


public class jts2geojson {

	
	public static void main(String[] args) throws Exception {
		/*WKTReader wkt = new WKTReader();
		Geometry polygon = wkt.read("POINT (30 10)");
		GeoJSONWriter writer = new GeoJSONWriter();
		GeoJSON json = writer.write(polygon);
		String jsonstring = json.toString();
		System.out.println(jsonstring);
		GeoJSONReader reader = new GeoJSONReader();
		Geometry geometry = reader.read(json);
		Point point =(Point)geometry;
		System.out.println(point);*/
			
		
		WKTReader3D wkt = new WKTReader3D();
		Geometry solid = wkt.read("SOLID((((40 40, 20 45, 45 30, 40 40)), " + 
				"((20 35, 10 30, 10 10, 30 5, 45 20, 20 35), " + 
				"(30 20, 20 15, 20 25, 30 20))), (((35 10, 45 45, 15 40, 10 20, 35 10), " + 
				"(20 30, 35 35, 30 20, 20 30)))) ");
		GeoJSON3DWriter writer = new GeoJSON3DWriter();
		GeoJSON json = writer.write(solid);
		String jsonstring = json.toString();
		System.out.println(jsonstring);
		jsonstring = "dss";
		System.out.println(jsonstring);
		GeoJSON3DReader reader = new GeoJSON3DReader();
		Geometry geometry = reader.read(json);
		GeoJSON3DWriter writer2 = new GeoJSON3DWriter();
		json = writer2.write(geometry);
		jsonstring = json.toString();
		System.out.println(jsonstring);
		
		
	}
}
