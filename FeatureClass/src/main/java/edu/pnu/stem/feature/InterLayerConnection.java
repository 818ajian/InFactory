package edu.pnu.stem.feature;

/**
 * @author jungh Implements InteraLayerConnectionType of IndoorGML 1.0.3
 */
public class InterLayerConnection extends AbstractFeature {
	
	String parentId;
	
	typeOfTopoExpressionCode typeOfTopoExpression;
	/**
	 * describe characteristic of this instance
	 */
	String comment;
	/**
	 * save list of ID of States which are related with each others as this
	 * InterLayerConnection
	 */
	String[] interConnects;
	/**
	 * save list of ID of SpaceLayers which are related with each others as this
	 * InterLayerConnection
	 */
	String[] connectedLayers;

	public Boolean checkInterConnectsNumber() {
		// Boolean flag = false;
		if (this.interConnects.length != 2 || this.connectedLayers.length != 2) {
			return false;
		}
		return true;

	}
	
	public void setParent(InterEdges parent){
		InterEdges found = null;
		found = (InterEdges)IndoorGMLMap.getFeature(IndoorGMLMap.getFeatureContainer("InterEdges"), parent.getId());
		if(found == null){
			IndoorGMLMap.setFeature(parent.getId(), "InterEdges", parent);
		}
		this.parentId = parent.getId();
	}
	
	public InterEdges getParent(){
		InterEdges found = null;
		found = (InterEdges)IndoorGMLMap.getFeature(IndoorGMLMap.getFeatureContainer("InterEdges"), this.parentId);
		return found;
	}

	public void setConnectedLayers(SpaceLayer[] connectedLayers){
		SpaceLayer[]found = new SpaceLayer[2];
		found[0] = (SpaceLayer)IndoorGMLMap.getFeature(IndoorGMLMap.getFeatureContainer("SpaceLayer"), connectedLayers[0].getId());
		found[1] = (SpaceLayer)IndoorGMLMap.getFeature(IndoorGMLMap.getFeatureContainer("SpaceLayer"), connectedLayers[1].getId());
		if(found[0] == null){
			IndoorGMLMap.setFeature(connectedLayers[0].getId(), "SpaceLayer", connectedLayers[0]);
		}
		if(found[1] == null){
			IndoorGMLMap.setFeature(connectedLayers[1].getId(), "SpaceLayer", connectedLayers[1]);
		}
		this.connectedLayers[0] = connectedLayers[0].getId();
		this.connectedLayers[1] = connectedLayers[1].getId();
	}
	
	public SpaceLayer[] getConnectedLayers() {
		SpaceLayer[] found = new SpaceLayer[2];
		found[0] = (SpaceLayer)IndoorGMLMap.getFeature(IndoorGMLMap.getFeatureContainer("SpaceLayer"), this.connectedLayers[0]);
		found[1] = (SpaceLayer)IndoorGMLMap.getFeature(IndoorGMLMap.getFeatureContainer("SpaceLayer"), this.connectedLayers[1]);
		return found;
	}

	public void setInterConnects(String[] ic) {
		this.interConnects = ic;
	}

	public String[] getInterConnects() {
		return this.interConnects;
	}

	/**
	 * @return the typeOfTopoExpression
	 */
	public typeOfTopoExpressionCode getTypeOfTopoExpression() {
		return typeOfTopoExpression;
	}

	/**
	 * @param typeOfTopoExpression
	 *            the typeOfTopoExpression to set
	 */
	public void setTypeOfTopoExpression(typeOfTopoExpressionCode typeOfTopoExpression) {
		this.typeOfTopoExpression = typeOfTopoExpression;
	}

	public void setComment(String comment) {
		this.comment = comment;
		
	}


}
