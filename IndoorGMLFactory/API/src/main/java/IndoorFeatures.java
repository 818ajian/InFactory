import Binder.IndoorGMLMap;
import Binder.docData;
import FeatureClass.MultiLayeredGraph;
import FeatureClass.PrimalSpaceFeatures;

public class IndoorFeatures {
	/**
	 * Create IndoorFeatures feature instance
	 * @param ID ID of feature
	 * @param psf feature instance of primalSpaceFeatures
	 * @param mlg feature instance of MultiLayeredFeatures
	 * @return created IndoorFeatures feature
	 */
	public FeatureClass.IndoorFeatures createIndoorFeatures(String ID, PrimalSpaceFeatures psf, MultiLayeredGraph mlg) {
		return null;
	};
	public static FeatureClassReference.IndoorFeatures createIndoorFeatures(String docID, String parentID, String ID,
			String primalSpaceFeatures, String multiLayeredGraph) {
		FeatureClassReference.IndoorFeatures newFeature = null;
		if (docData.docs.hasDoc(docID)) {
			newFeature.setID(ID);
			//newFeature.setParentID(parentID);
			if (primalSpaceFeatures!= null) {
				newFeature.setPrimalSpaceFeatures(primalSpaceFeatures);
			}
			if (multiLayeredGraph != null) {
				newFeature.setMultiLayeredGraph(multiLayeredGraph);
			}
			docData.docs.setFeature(docID, ID, "IndoorFeatures", newFeature);
		}
		return newFeature;
	}
	
	/**
	 * Search IndoorFeatures feature instance in document
	 * @param ID ID of target
	 * @return searched feature
	 */
	public FeatureClass.IndoorFeatures readIndoorFeatures(String ID) {
		return null;
	};

	/**
	 * Search IndoorFeatures feature instance and edit it as the parameters
	 * @param ID ID of target
	 * @param psf feature instance of PrimalSpaceFeatures
	 * @param mlg feature instance of MultiLayeredFeatures
	 * @return edited feature
	 */
	public FeatureClass.IndoorFeatures updateIndoorFeatures(String ID, PrimalSpaceFeatures psf, MultiLayeredGraph mlg) {
		return null;
	};

	/**
	 * Search IndoorFeatures feature instance and delete it
	 * @param ID ID of target
	 */
	public void deleteIndoorFeatures(String docId, String Id) {
		if (docData.docs.hasFeature(docId, Id)) {
			IndoorGMLMap doc = docData.docs.getDocument(docId);
			FeatureClassReference.IndoorFeatures target = (FeatureClassReference.IndoorFeatures) docData.docs.getFeature(docId,
					Id);
			// String duality = target.getd;
			doc.getFeatureContainer("IndoorFeatures").remove(Id);
			doc.getFeatureContainer("ID").remove(Id);
			
			
		}
	};

}
