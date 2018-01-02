package edu.pnu.stem.feature;
/**
 * @author jungh
 *	Implements InteraLayerConnectionType of IndoorGML 1.0.3
 */
public class InterLayerConnection {
	/**
	 * ID of this feature
	 */
	public String ID;
	/**
	 * Name of this feature
	 */
	public String name;
	/**
	 * save the topology type value
	 */
	public typeOfTopoExpressionCode typeOfTopoExpression;
	/**
	 * describe characteristic of this instance
	 */
	public String comment;
	/**
	 * save list of ID of States which are related with each others as this InterLayerConnection
	 */
	public String[] interConnects;
	/**
	 * save list of ID of SpaceLayers which are related with each others as this InterLayerConnection
	 */
	public String[] connectedLayers;
	
	public Boolean checkInterConnectsNumber(){
		//Boolean flag = false;
		if(this.interConnects.length != 2 || this.connectedLayers.length != 2){
			return false;			
		}
		return true; 
					
	}
	public String parentID;
	public void setID(String id){this.ID = id;};
	public String getID(){return this.ID;};
	public void setParentID(String id){this.parentID = id;}
	public String getParentID(){return this.parentID;}
	
	public void setConnectedLayers(String[] cl){this.connectedLayers = cl;}
	public String[] getConnectedLayers(){return this.connectedLayers;}
	
	public void setInterConnects(String[] ic){this.interConnects = ic;}
	public String[] getInterConnects(){return this.interConnects;}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the typeOfTopoExpression
	 */
	public typeOfTopoExpressionCode getTypeOfTopoExpression() {
		return typeOfTopoExpression;
	}
	/**
	 * @param typeOfTopoExpression the typeOfTopoExpression to set
	 */
	public void setTypeOfTopoExpression(typeOfTopoExpressionCode typeOfTopoExpression) {
		this.typeOfTopoExpression = typeOfTopoExpression;
	}
	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}
	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

}
