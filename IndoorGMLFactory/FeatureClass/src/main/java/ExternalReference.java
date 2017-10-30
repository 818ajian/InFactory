import net.opengis.indoorgml.core.v_1_0.ExternalObjectReferenceType;

/**
 * @author jungh
 *	Implements ExternalReferenceType of IndoorGML 1.0.3
 */
public class ExternalReference {
	/**
	 * ID of this feature
	 */
	public String ID;
	/**
	 * Name of this feature
	 */
	public String name;
	/**
	 * describe information of this ExternalRefence
	 */
	public String informationSystem;
	/**
	 * instance of External Reference
	 */
	public ExternalObjectReferenceType externalObject;

}
