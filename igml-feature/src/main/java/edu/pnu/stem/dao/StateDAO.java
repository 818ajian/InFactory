package edu.pnu.stem.dao;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

import edu.pnu.stem.binder.IndoorGMLMap;
import edu.pnu.stem.feature.CellSpace;
import edu.pnu.stem.feature.Nodes;
import edu.pnu.stem.feature.State;
import edu.pnu.stem.feature.Transition;

public class StateDAO {
	/*
	public static State createState(IndoorGMLMap map, String parentId, String Id,
			String duality, List<String> connects, String geometry, String externalReference) {
		State newFeature = null;
		newFeature = new State(map);
		newFeature.setId(Id);
		
		Nodes parent = (Nodes) map.getFeature(parentId);
		parent.addStateMember(newFeature);
		newFeature.setParent(parent);
		
		if (duality != null) {
			CellSpace tempDuality = new CellSpace(map);
			tempDuality.setId(duality);
			newFeature.setDuality(tempDuality);
			if(map.getFeatureContainer("Reference").containsKey(duality)){
				int count = (Integer)map.getFeatureContainer("Reference").get(duality);
				count++;
				map.setFeature(duality, "Reference", count);
			}
			else{
				map.setFeature(duality, "Reference", 1);
			}
			map.setFeature(Id, "Reference", 1);
		}
		if (geometry != null) {
			// newFeature.set
		}
		if (connects != null) {
			List<Transition>tempConnects = new ArrayList<Transition>();	
			for(int i = 0 ; i < connects.size() ; i++){
				Transition temp = new Transition(map);
				temp.setId(connects.get(i));
				tempConnects.add(temp);
			}
			newFeature.setConnects(tempConnects);				
		}
		if (externalReference != null) {
			newFeature.setExternalReference(externalReference);
		}
		map.setFeature(Id, "CellSpace", newFeature);
		return newFeature;
	}
	*/
	public static State createState(IndoorGMLMap map, String parentId, String id, JsonNode geometry, String duality, List<String> connected) {
		if(id == null) {
			id = UUID.randomUUID().toString();
		}
		
		State newFeature = new State(map, id);
		if(map.hasFutureID(id)){
			newFeature = (State)map.getFutureFeature(id);
			//map.removeFutureID(id);
		}
		else{
			map.setFutureFeature(id, newFeature);
		}		
		map.setFeature(id, "State", newFeature);
		
		Nodes parent = (Nodes) map.getFeature(parentId);
		if(parent == null){
			if(map.hasFutureID(parentId)){
				parent = (Nodes)map.getFutureFeature(parentId);
			}
			else{
				parent = new Nodes(map,parentId);
			}
		}
		parent.addStateMember(newFeature);
		newFeature.setParent(parent);
		
		if (geometry != null) {
			WKTReader wkt = new WKTReader();
			Point p = null;
			if(geometry.has("coordinates")){
				try{
					p = (Point)wkt.read(geometry.get("coordinates").asText().trim());
				}catch (ParseException e) {
					e.printStackTrace();
				}
			}
			else{
				try{
				p = (Point)wkt.read(geometry.asText().trim());
				}catch (ParseException e) {
					e.printStackTrace();
				}
			}
			
			String geometryId = null;
			if(geometry.has("properties")){
				if(geometry.get("properties").has("id")){
					geometryId = geometry.get("properties").get("id").asText().trim();
				}
			}
			if(geometryId == null){
				geometryId = UUID.randomUUID().toString();
			}
			
			if(p != null){
				map.setFeature4Geometry(geometryId, p);
				newFeature.setGeometry(p);
			}
		}
		
		if(connected != null){
			List<Transition> realConnected = new ArrayList<Transition>();
			for(String t : connected){
				Transition ct = (Transition)map.getFeature(t);
				if(ct == null){
					ct = new Transition(map, t);
				}
				realConnected.add(ct);
			}
			newFeature.setConnects(realConnected);
		}
		
		if(duality != null){
			CellSpace dualityFeature = (CellSpace) map.getFeature(duality);
			
			if(dualityFeature == null){
				dualityFeature = new CellSpace(map, duality);
			}
			
			dualityFeature.setDuality(newFeature);
			newFeature.setDuality(dualityFeature);

		}

		map.removeFutureID(id);
		return newFeature;
	}	
	public static State createState(IndoorGMLMap map, String parentId, String id, String geometry, List<String> connected) {
		if(id == null) {
			id = UUID.randomUUID().toString();
		}
		
		State newFeature = new State(map, id);
		if(map.hasFutureID(id)){
			newFeature = (State)map.getFutureFeature(id);
		}
		
		
		Nodes parent = (Nodes) map.getFeature(parentId);
		if(parent == null){
			if(map.hasFutureID(parentId)){
				parent = (Nodes)map.getFutureFeature(parentId);
			}
			else{
				parent = new Nodes(map,parentId);
			}
		}
		parent.addStateMember(newFeature);
		newFeature.setParent(parent);
		
		if (geometry != null) {
			WKTReader wkt = new WKTReader();
			try {
				Point p = (Point) wkt.read(geometry);
				map.setFeature4Geometry(UUID.randomUUID().toString(), p);
				newFeature.setGeometry(p);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(connected != null){
			List<Transition> realConnected = new ArrayList<Transition>();
			for(String t : connected){
				Transition ct = (Transition)map.getFeature(t);
				if(ct == null){
					ct = new Transition(map, t);
				}
				realConnected.add(ct);
			}
			newFeature.setConnects(realConnected);
		}

		map.setFeature(id, "State", newFeature);
		return newFeature;
	}
	
	public State readState(IndoorGMLMap map, String id) {
		State target = null;
		try {
			target = (State)map.getFeature(id);
		}
		catch(NullPointerException e) {}
		return target;
	};
	
	public static void deleteState(IndoorGMLMap map, String id) {
		State target = (State)map.getFeature(id);
		Nodes parent = target.getParent();
		parent.deleteStateMember(target);
		
		if(target.hasDuality()) {
			CellSpace duality = target.getDuality();
			duality.resetDuality();
		}
		map.remvoeFeature(id);
	}
	
	public static State updateState(IndoorGMLMap map,String parentId, String id, String name, String description, Geometry geometry, String duality, List<String>connects) {
		State result = new State(map, id);
		State target = (State)map.getFeature(id);
		
		Nodes parent = target.getParent();
		if(parent.getId() != parentId) {
			Nodes newParent = new Nodes(map, parentId);
			parent.deleteStateMember(target);
			result.setParent(newParent);
		}
		
		
		if(name != null) {
			result.setName(name);
		}
	
		
		if(description != null) {
			result.setDescription(description);
		}
		
		if(geometry != null) {
			result.setGeometry(geometry);
		}
		
		
		if(connects != null) {
			List<Transition> cnts = new ArrayList<Transition>();
			
			for(String t : connects) {
				Transition temp = new Transition(map,t);
				cnts.add(temp);
			}
			result.setConnects(cnts);
		}
		
		if(duality == null) {
			CellSpace d = target.getDuality();
			d.resetDuality();
		}
		else {
			if(target.getDuality() != null) {
				if(target.getDuality().getId() != duality) {
					CellSpace	oldDuality = new CellSpace(map,duality);
					oldDuality.resetDuality();
				}
			}
			
			CellSpace newDuality = new CellSpace(map,duality);
			result.setDuality(newDuality);
		}
		
		map.getFeatureContainer("State").remove(id);
		map.getFeatureContainer("State").put(id, result);
		
		return result;
	}
	/**
	 * Search State feature and edit it as the parameters
	 * @param ID ID of target 
	 * @param d feature of CellSpace which has duality relationship with this state
	 * @param t feature instance of Transition which is connected with this feature
	 * @param geo Geometry instance of Point which represent this state
	 * @return edited State feature instance
	 */
/*	public State updateState(String ID, CellSpaceDAO d, TransitionDAO t, PointType geo) {
		return null;
	}
	public State updateState(String docId, String Id, String attributeType, String updateType, Object o) {
		State target = null;
		if (Container.getInstance().hasFeature(docId, Id)) {
			IndoorGMLMap map = Container.getInstance().getDocument(docId);
			target = (State) Container.getInstance().getFeature(docId, Id);
			if (attributeType.equals("geometry")) {
				// TODO: need to implement geometry class at IndoorGMLAPI
			} else if (attributeType.equals("connects")) {
				List<String>connects = new ArrayList<String>();
				List<Transition>tempConnects = target.getConnects();
				List<Transition>newConnects = new ArrayList<Transition>();
				if(o instanceof List<?>){
					connects = (List<String>)o;
				}
				else if(o instanceof String){
					connects.add((String)o);
				}
				for(int i = 0 ; i < connects.size() ; i++){
					Transition temp = new Transition(map);
					temp.setId(connects.get(i));
					newConnects.add(temp);
				}
				
				if(updateType.equals("add")){
					for(int i = 0 ; i < newConnects.size(); i++){
						if(!tempConnects.contains(newConnects.get(i))){
							tempConnects.add(newConnects.get(i));
						}
					}
				}
				else if(updateType.equals("remove")){
					for(int i = 0 ; i < newConnects.size(); i++){
						if(tempConnects.contains(newConnects.get(i))){
							tempConnects.remove(newConnects.get(i));
						}
					}
					target.clearConnects();
				}
				target.setConnects(tempConnects);				
			} else if (attributeType.equals("duality")) {
				CellSpace tempDuality = new CellSpace(map);
				tempDuality.setId((String)o);
				target.setDuality(tempDuality);
			} else if (attributeType.equals("externalReference")) {
				//target.setExternalReference(attributeId);
				//map.setFeature(attributeId, "ExternaReference", o);
			} else {
				System.out.println("update error in cellSpaceType : there is no such attribute name");
			}
		} else {
			System.out.println("there is no name with Id :" + Id + " in document Id : " + docId);
		}
		return target;
	}
	*/
	/**
	 * Search State feature and delete it
	 * @param ID ID of target 
	 */
/*	public static void deleteState(String docId, String Id, Boolean deleteDuality) {
		if (Container.getInstance().hasFeature(docId, Id)) {
			IndoorGMLMap doc = Container.getInstance().getDocument(docId);
			State target = (State) Container.getInstance().getFeature(docId,
					Id);
			// String duality = target.getd;
			doc.getFeatureContainer("State").remove(Id);
			doc.getFeatureContainer("ID").remove(Id);
			
			List<Transition> connects = target.getConnects();
			if(deleteDuality){
				//State.deleteState(target.getDuality());
				if(doc.hasID(target.getDuality().getId())){
					CellSpaceDAO.deleteCellSpace(docId,target.getDuality().getId(),false);
				}
				
			}
			
			for(int i = 0 ; i < connects.size();i++){
				int count = (Integer) doc.getFeatureContainer("Reference").get(connects.get(i));
				if(count == 1){
					TransitionDAO.deleteTransition(docId, connects.get(i).getId(),deleteDuality);
					doc.getFeatureContainer("Reference").remove(connects.get(i));
				}
				else{
					doc.setFeature(connects.get(i).getId(), "Reference", (count-1));
				}
				
			}
			
			// ExdeleteExternalReference()

			for (int i = 0; i < connects.size(); i++) {
				int count = (Integer) doc.getFeatureContainer("Reference").get(connects.get(i));
				if ( count == 1) {
					CellSpaceBoundaryDAO.deleteCellSpaceBoundary(docId, connects.get(i).getId(), deleteDuality);
				}
				else{
					doc.setFeature(connects.get(i).getId(), "Reference", (count-1));
				}
			}

			doc.getFeatureContainer("ExternalReference").remove(target.getExternalReference());
			doc.getFeatureContainer("ID").remove(target.getExternalReference());
			
		}

	};
*/
}
