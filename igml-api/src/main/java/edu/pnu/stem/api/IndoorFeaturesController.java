/**
 * 
 */
package edu.pnu.stem.api;

import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;

import edu.pnu.stem.api.exception.DuplicatedFeatureException;
import edu.pnu.stem.api.exception.UndefinedDocumentException;
import edu.pnu.stem.binder.IndoorGMLMap;
import edu.pnu.stem.dao.IndoorFeaturesDAO;
import edu.pnu.stem.feature.IndoorFeatures;

/**
 * @author Hyung-Gyu Ryoo (hyunggyu.ryoo@gmail.com, Pusan National University)
 *
 */
@RestController
@RequestMapping("/indoorfeatures")
public class IndoorFeaturesController {
	
	@Autowired
    private ApplicationContext applicationContext;
	
	@PostMapping(value = "/{id}", produces = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public void createIndoorFeatures(@PathVariable("id") String id, @RequestBody ObjectNode json, HttpServletRequest request, HttpServletResponse response) {
		String docId = json.get("docId").asText().trim();
		
		if(id == null || id.isEmpty()) {
			id = UUID.randomUUID().toString();
		}
		
		IndoorFeatures f;
		try {
			Container container = applicationContext.getBean(Container.class);
			IndoorGMLMap map = container.getDocument(docId);
			f = IndoorFeaturesDAO.createIndoorFeatures(map, id);
		} catch (NullPointerException e) {
			e.printStackTrace();
			throw new UndefinedDocumentException();
		}
		response.setHeader("Location", request.getRequestURL().append(f.getId()).toString());
	}
	
}
