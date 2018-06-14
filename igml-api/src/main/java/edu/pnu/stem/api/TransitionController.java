/**
 * 
 */
package edu.pnu.stem.api;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vividsolutions.jts.geom.Geometry;

import edu.pnu.stem.api.exception.UndefinedDocumentException;
import edu.pnu.stem.binder.Convert2Json;
import edu.pnu.stem.binder.IndoorGMLMap;
import edu.pnu.stem.dao.TransitionDAO;
import edu.pnu.stem.feature.Transition;

/**
 * @author Hyung-Gyu Ryoo (hyunggyu.ryoo@gmail.com, Pusan National University)
 *
 */
@RestController
@RequestMapping("/documents/{docId}/transition")
public class TransitionController {
	
	@Autowired
    private ApplicationContext applicationContext;
	
	@PostMapping(value = "/{id}", produces = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public void createSpaceLayer(@PathVariable("docId") String docId,@PathVariable("id") String id, @RequestBody ObjectNode json, HttpServletRequest request, HttpServletResponse response) {

		String parentId = json.get("parentId").asText().trim();
		
		String[] connects = new String[2];
		
		String duality = null;
		String geom = json.get("geometry").asText().trim();
	
		Transition t;
		Geometry geometry = null;

		
		if(id == null || id.isEmpty()) {
			id = UUID.randomUUID().toString();
		}
		
		
		if(json.has("properties")){
			if(json.get("properties").has("connects")){
				connects[0] = json.get("properties").get("connects").get(0).asText().trim();
				connects[1] = json.get("properties").get("connects").get(1).asText().trim();
			}
			if(json.get("properties").has("duality")){
				duality = json.get("properties").get("duality").asText().trim();
			}
		}
		else if(json.has("connects")){
			JsonNode connectsNode = json.get("connects");
			if(connectsNode.isArray()) {
				connects[0] = connectsNode.get(0).asText().trim();
				connects[1] = connectsNode.get(1).asText().trim();
			}
		}
		
		if(json.has("geometry")) {
			geometry = Convert2Json.json2Geometry(json.get("geometry"));
		}

		try {
			Container container = applicationContext.getBean(Container.class);
			IndoorGMLMap map = container.getDocument(docId);
			t = TransitionDAO.createTransition(map, parentId, id, geometry, duality, connects);
		} catch (NullPointerException e) {
			e.printStackTrace();
			throw new UndefinedDocumentException();
		}
		response.setHeader("Location", request.getRequestURL().append(t.getId()).toString());
	}
	
	@PutMapping(value = "/{id}", produces = "application/json")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void updateTransition(@PathVariable("docId") String docId,@PathVariable("id") String id, @RequestBody ObjectNode json, HttpServletRequest request, HttpServletResponse response) {
		try {
			Container container = applicationContext.getBean(Container.class);
			IndoorGMLMap map = container.getDocument(docId);
			String duality = null;
			JsonNode geometry = null;
			List<String> connects = null;
			Geometry geom = null;
			String parentId = null;
			String name = null;
			String description = null;
			
			if(json.has("parentId")) {
				parentId = json.get("parentId").asText().trim();
			}
			
			if(json.has("duality")){
				
				duality = json.get("duality").asText().trim();
				
			}
			if(json.has("properties")){
				if(json.get("properties").has("duality")){
					duality = json.get("properties").get("duality").asText().trim();
					
				}
				
			}
			if(json.has("geometry")) {
				geometry = json.get("geometry");
				geom = Convert2Json.json2Geometry(geometry);
			
			}
			
			//TODO : ���߿� ��ġ��!!
			//String properties = json.get("properties").asText().trim();
			//String duality = null;
			
			if(json.has("properties")){
				if(json.get("properties").has("connects")){
					connects = new ArrayList<String>();
					JsonNode partialBoundedByList = json.get("properties").get("connects");
					for(int i = 0 ; i < partialBoundedByList.size() ; i++){
						connects.add(partialBoundedByList.get(i).asText().trim());
					}
				}
			}
			
			String[] arrConnects = new String[2];
			connects.toArray(arrConnects);
			
		TransitionDAO.updateTransition(map, parentId, id, name, description, geom, duality, arrConnects);
			
		}
		catch(NullPointerException e) {
			e.printStackTrace();
			throw new UndefinedDocumentException();
		}
	}
	
}
