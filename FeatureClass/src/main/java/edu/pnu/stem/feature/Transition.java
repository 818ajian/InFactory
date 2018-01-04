package edu.pnu.stem.feature;

import net.opengis.gml.v_3_2_1.CurveType;

/**
 * @author jungh Implements TransitionType of IndoorGML 1.0.3
 */
public class Transition extends AbstractFeature {
	/**
	 * geometry of transition
	 */
	CurveType geometry;
	/**
	 * value of CellSpaceBoundary feature which has duality relationship with
	 * this feature
	 */
	String duality;
	/**
	 * value of weight which usally is used for transfering cost in road
	 * network, etc.
	 */
	double weight;

	/**
	 * Array of connected States. minimum and maximum of the number of element
	 * needs to be 2
	 */
	String[] connects = new String[2];

	String parentId;

	String externalReference;

	public boolean hasDuality() {
		if (this.duality == null) {
			return false;
		} else {
			return true;
		}
	}
	
	public void setExternalReference(String er) {
		this.externalReference = er;
	}

	public String getExternalReference() {
		return new String(this.externalReference);
	}

	public void setParent(Edges parent) {		
		this.parentId = parent.getId();
	}

	public Edges getParent() {
		Edges feature = null;
		feature = (Edges) IndoorGMLMap.getFeature(IndoorGMLMap.getFeatureContainer("Edges"), this.parentId);
		return feature;
	}

	public CellSpaceBoundary getDuality() {
		CellSpaceBoundary found = null;
		if(hasDuality()){
			found = (CellSpaceBoundary)IndoorGMLMap.getFeature(IndoorGMLMap.getFeatureContainer("CellSpaceBoundary"), this.duality);
		}
		return found;
	}

	public void setDuality(CellSpaceBoundary duality) {
		
	}
	public CurveType getGeometry() {
		return this.geometry;
	}

	public void setGeometry(CurveType g) {
		this.geometry = g;
	}

	public void setConnects(String[] connects) {
		if (connects.length != 2) {
			System.out.println("FeatureClass.Transition.setConnects : The size of input is not 2");
		} else {
			this.connects[0] = connects[0];
			this.connects[1] = connects[1];
		}

	}

	public String[] getConnects() {	
		return this.connects.clone();
	}

	public Object[] getConnectsInstance() {
		Object[] feature = new Object[2];

		feature[0] = IndoorGMLMap.getFeature(IndoorGMLMap.getFeatureContainer("State"), this.connects[0]);
		feature[1] = IndoorGMLMap.getFeature(IndoorGMLMap.getFeatureContainer("State"), this.connects[1]);

		return feature;

	}

	/**
	 * @return the weight
	 */
	public double getWeight() {
		return new Double(weight);
	}

	/**
	 * @param weight
	 *            the weight to set
	 */
	public void setWeight(double weight) {
		this.weight = weight;
	}

}
