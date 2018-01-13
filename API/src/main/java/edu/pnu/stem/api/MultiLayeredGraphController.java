/**
 * 
 */
package edu.pnu.stem.api;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;

import edu.pnu.stem.api.exception.UndefinedDocumentException;
import edu.pnu.stem.binder.IndoorGMLMap;
import edu.pnu.stem.dao.MultiLayeredGraphDAO;
import edu.pnu.stem.feature.MultiLayeredGraph;

/**
 * @author hyung
 *
 */
@RestController
@RequestMapping("/multilayeredgraph")
public class MultiLayeredGraphController {
	
	@Autowired
    private ApplicationContext applicationContext;
	
	@PostMapping(value = "/", produces = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public void createMultiLayeredGraph(@RequestBody ObjectNode json, HttpServletRequest request, HttpServletResponse response) {
		String docId = json.get("docId").asText().trim();
		String parentId = json.get("parentId").asText().trim();
		String id = json.get("id").asText().trim();
		
		if(id == null || id.isEmpty()) {
			id = UUID.randomUUID().toString();
		}
		
		MultiLayeredGraph mg;
		try {
			Container container = applicationContext.getBean(Container.class);
			IndoorGMLMap map = container.getDocument(docId);
			mg = MultiLayeredGraphDAO.createMultiLayeredGraph(map, parentId, id);
		} catch (NullPointerException e) {
			e.printStackTrace();
			throw new UndefinedDocumentException();
		}
		response.setHeader("Location", request.getRequestURL().append(mg.getId()).toString());
	}
	
}
