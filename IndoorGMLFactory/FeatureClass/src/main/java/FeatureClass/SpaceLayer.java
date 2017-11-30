package FeatureClass;
import java.util.Date;
import java.util.List;

import net.opengis.gml.v_3_2_1.CodeType;

public class SpaceLayer {
	/**
	 * ID of this feature
	 */
	public String ID;
	/**
	 * Name of this feature
	 */
	public String name;
	/**
	 * description of usage of the feature
	 */
	public String usage;
	/**
	 * functionality of the feature
	 */
	public List<CodeType> function;
	/**
	 * time stamp when the SpaceLayer is created
	 */
	public Date createDate;
	/**
	 * time stamp when the SpaceLayer is expired
	 */
	public Date terminationDate;
	/**
	 * Nodes which the SpaceLayer contains
	 */
	public List<Nodes> nodes;
	/**
	 * Edges which the SpaceLayer contains
	 */
	public List<Edges> edges;
	/**
	 * represent Class type of the SpaceLayer
	 */
	//public SpaceLayerClassTypeType classType;
}
