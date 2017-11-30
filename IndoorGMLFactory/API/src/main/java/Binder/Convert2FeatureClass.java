package Binder;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import FeatureClassReference.AbstractFeatures;
import FeatureClassReference.CellSpace;
import FeatureClassReference.CellSpaceBoundary;
import FeatureClassReference.Edges;
import FeatureClassReference.ExternalObjectReference;
import FeatureClassReference.ExternalReference;
import FeatureClassReference.IndoorFeatures;
import FeatureClassReference.InterEdges;
import FeatureClassReference.InterLayerConnection;
import FeatureClassReference.MultiLayeredGraph;
import FeatureClassReference.Nodes;
import FeatureClassReference.PrimalSpaceFeatures;
import FeatureClassReference.SpaceLayer;
import FeatureClassReference.SpaceLayerClassType;
import FeatureClassReference.SpaceLayers;
import FeatureClassReference.State;
import FeatureClassReference.Transition;
import FeatureClassReference.typeOfTopoExpressionCode;
import net.opengis.gml.v_3_2_1.AbstractFeatureType;
import net.opengis.gml.v_3_2_1.CompositeCurveType;
import net.opengis.gml.v_3_2_1.CompositeSolidType;
import net.opengis.gml.v_3_2_1.CompositeSurfaceType;
import net.opengis.gml.v_3_2_1.CurvePropertyType;
import net.opengis.gml.v_3_2_1.CurveType;
import net.opengis.gml.v_3_2_1.SolidType;
import net.opengis.gml.v_3_2_1.SurfaceType;
import net.opengis.indoorgml.core.v_1_0.CellSpaceBoundaryGeometryType;
import net.opengis.indoorgml.core.v_1_0.CellSpaceBoundaryMemberType;
import net.opengis.indoorgml.core.v_1_0.CellSpaceBoundaryPropertyType;
import net.opengis.indoorgml.core.v_1_0.CellSpaceBoundaryType;
import net.opengis.indoorgml.core.v_1_0.CellSpaceGeometryType;
import net.opengis.indoorgml.core.v_1_0.CellSpaceMemberType;
import net.opengis.indoorgml.core.v_1_0.CellSpaceType;
import net.opengis.indoorgml.core.v_1_0.EdgesType;
import net.opengis.indoorgml.core.v_1_0.ExternalObjectReferenceType;
import net.opengis.indoorgml.core.v_1_0.ExternalReferenceType;
import net.opengis.indoorgml.core.v_1_0.IndoorFeaturesType;
import net.opengis.indoorgml.core.v_1_0.InterEdgesType;
import net.opengis.indoorgml.core.v_1_0.InterLayerConnectionMemberType;
import net.opengis.indoorgml.core.v_1_0.InterLayerConnectionType;
import net.opengis.indoorgml.core.v_1_0.MultiLayeredGraphPropertyType;
import net.opengis.indoorgml.core.v_1_0.MultiLayeredGraphType;
import net.opengis.indoorgml.core.v_1_0.NodesType;
import net.opengis.indoorgml.core.v_1_0.PrimalSpaceFeaturesPropertyType;
import net.opengis.indoorgml.core.v_1_0.PrimalSpaceFeaturesType;
import net.opengis.indoorgml.core.v_1_0.SpaceLayerClassTypeType;
import net.opengis.indoorgml.core.v_1_0.SpaceLayerMemberType;
import net.opengis.indoorgml.core.v_1_0.SpaceLayerPropertyType;
import net.opengis.indoorgml.core.v_1_0.SpaceLayerType;
import net.opengis.indoorgml.core.v_1_0.SpaceLayersType;
import net.opengis.indoorgml.core.v_1_0.StateMemberType;
import net.opengis.indoorgml.core.v_1_0.StatePropertyType;
import net.opengis.indoorgml.core.v_1_0.StateType;
import net.opengis.indoorgml.core.v_1_0.TransitionMemberType;
import net.opengis.indoorgml.core.v_1_0.TransitionPropertyType;
import net.opengis.indoorgml.core.v_1_0.TransitionType;
import net.opengis.indoorgml.core.v_1_0.TypeOfTopoExpressionCodeEnumerationType;

public class Convert2FeatureClass {
	static IndoorGMLMap docContainer = new IndoorGMLMap();
	static AbstractFeatures change2FeatureClass(AbstractFeatureType feature, String parentID) {
		AbstractFeatures newFeature = new AbstractFeatures();
		Object tempLocation = feature.getLocation().getValue();
		newFeature.ID = feature.getId();
		newFeature.boundedBy = feature.getBoundedBy();
		return newFeature;
	}
	
	public static CellSpace change2FeatureClass(CellSpaceType feature, String parentID) {
		CellSpace newFeature = new CellSpace();

		StatePropertyType tempState = feature.getDuality();
		if(tempState != null){
	
			newFeature.duality = tempState.getHref();
		}
		
		newFeature.ID = feature.getId();
		if (feature.getCellSpaceGeometry() != null) {
			CellSpaceGeometryType geo = feature.getCellSpaceGeometry();
			if (geo.getGeometry2D() != null) {
				Object o = geo.getGeometry2D().getAbstractSurface().getValue();
				if (o instanceof SurfaceType) {
					newFeature.cellSpaceGeometryObject = ((SurfaceType) o).getId();
					newFeature.geometryType = "SurfaceType";
				} else if (o instanceof CompositeSurfaceType) {
					newFeature.cellSpaceGeometryObject = ((CompositeSurfaceType) o).getId();
					newFeature.geometryType = "CompositeSurfaceType";
				}

			} else if (geo.getGeometry3D() != null) {
				Object o = geo.getGeometry3D().getAbstractSolid().getValue();
				if (o instanceof SolidType) {
					newFeature.cellSpaceGeometryObject = ((SolidType) o).getId();
					newFeature.geometryType = "SolidType";
				} else if (o instanceof CompositeSolidType) {
					newFeature.cellSpaceGeometryObject = ((CompositeSolidType) o).getId();
					newFeature.geometryType = "CompositeSolidType";
				}

			}
		} else {
			System.out.println("Converter : There is no Geometry Information");

		}

		List<CellSpaceBoundaryPropertyType> boundList = feature.getPartialboundedBy();
		List<String> csbList = new ArrayList<String>();

		for (int i = 0; i < boundList.size(); i++) {
			csbList.add(boundList.get(i).getCellSpaceBoundary().getValue().getId());
		}
		newFeature.partialboundedBy = csbList;
		// newFeature.partialboundedBy = feature.getPartialboundedBy();

		return newFeature;
	}

	public CellSpaceBoundary change2FeatureClass(CellSpaceBoundaryType feature) {
		CellSpaceBoundary newFeature = new CellSpaceBoundary();
		
		newFeature.ID = feature.getId();
		TransitionPropertyType tempTransition = feature.getDuality();
		newFeature.duality = tempTransition.getTransition().getId();

		CellSpaceBoundaryGeometryType geo = feature.getCellSpaceBoundaryGeometry();
		if (geo != null) {
			if (geo.getGeometry2D() != null) {
				Object o = geo.getGeometry2D().getAbstractCurve().getValue();
				if (o instanceof CurveType) {
					newFeature.cellSpaceBoundaryGeometry = ((CurveType) o).getId();
				} else if (o instanceof CompositeCurveType) {
					newFeature.cellSpaceBoundaryGeometry = ((CompositeCurveType) o).getId();
				}

			} else if (geo.getGeometry3D() != null) {
				Object o = geo.getGeometry3D().getAbstractSurface().getValue();
				if (o instanceof SurfaceType) {
					newFeature.cellSpaceBoundaryGeometry = ((SurfaceType) o).getId();
				} else if (o instanceof CompositeSurfaceType) {
					newFeature.cellSpaceBoundaryGeometry = ((CompositeSurfaceType) o).getId();
				}
			}
		} else {
			System.out.println("Warning : There is no geometry at CellSpaceBoundary : " + feature.getId());
		}

		return newFeature;
	}

	/*
	 * 
	 * CellSpaceBoundaryGeometry
	 * change2FeatureClass(CellSpaceBoundaryGeometryType feature) { return null;
	 * } CellSpaceGeometry change2FeatureClass(CellSpaceGeometryType feature) {
	 * return null; }
	 */
	ExternalObjectReference change2FeatureClass(ExternalObjectReferenceType feature) {
		ExternalObjectReference newFeature = new ExternalObjectReference();

		newFeature.uri = (String) feature.getUri();
		return newFeature;
	}

	ExternalReference change2FeatureClass(ExternalReferenceType feature) {
		ExternalReference newFeature = new ExternalReference();
		ExternalObjectReference referredObject = new ExternalObjectReference();
		referredObject.uri = feature.getExternalObject().getUri();

		newFeature.externalObject = referredObject;

		return newFeature;
	}

	public static IndoorFeatures change2FeatureClass(IndoorFeaturesType feature) throws JAXBException {
		IndoorFeatures newFeature = new IndoorFeatures();
		newFeature.ID = feature.getId();
		MultiLayeredGraphType tempML = new MultiLayeredGraphType();
		// newFeature.multiLayeredGraph =
		// change2FeatureClass(feature.getMultiLayeredGraph());
		docContainer.inputID(feature.getId(), "IndoorFeatures");
		MultiLayeredGraph childM = change2FeatureClass(feature.getMultiLayeredGraph(), feature.getId());
		docContainer.setFeature(childM.ID, "MultiLayeredGraph", childM);
		newFeature.multiLayeredGraph = feature.getMultiLayeredGraph().getMultiLayeredGraph().getId();
		PrimalSpaceFeaturesPropertyType tempPSFP = new PrimalSpaceFeaturesPropertyType();
		tempPSFP = feature.getPrimalSpaceFeatures();
		PrimalSpaceFeatures childP = change2FeatureClass(tempPSFP.getPrimalSpaceFeatures(), feature.getId());
		// newFeature.primalSpaceFeatures =
		// change2FeatureClass(tempPSFP.getPrimalSpaceFeatures());
		docContainer.setFeature(childP.ID, "PrimalSpaceFeatures", childP);

		return newFeature;
	}

	public static MultiLayeredGraph change2FeatureClass(MultiLayeredGraphPropertyType feature, String id) {
		MultiLayeredGraph newFeature = new MultiLayeredGraph();
		newFeature.ID = feature.getMultiLayeredGraph().getId();
		newFeature.parentID = id;
		List<String> interEdges = new ArrayList<String>();
		List<String> spaceLayer = new ArrayList<String>();

		List<SpaceLayersType> spaceLayerInstanceList = new ArrayList<SpaceLayersType>();
		List<InterEdgesType> interEdgesInstanceList = new ArrayList<InterEdgesType>();

		for (int i = 0; i < feature.getMultiLayeredGraph().getInterEdges().size(); i++) {
			InterEdgesType temp = feature.getMultiLayeredGraph().getInterEdges().get(i);
			// interEdgesInstanceList.add(temp);
			interEdges.add(temp.getId());
			docContainer.setFeature(temp.getId(), "InterEdges", change2FeatureClass(temp, newFeature.ID));
		}
		for (int i = 0; i < feature.getMultiLayeredGraph().getSpaceLayers().size(); i++) {
			SpaceLayersType temp = feature.getMultiLayeredGraph().getSpaceLayers().get(i);
			// spaceLayerInstanceList.add(temp);
			spaceLayer.add(temp.getId());
			docContainer.setFeature(temp.getId(), "SpaceLayers", change2FeatureClass(temp, newFeature.ID));
		}

		newFeature.spaceLayers = spaceLayer;
		newFeature.interEdges = interEdges;

		return newFeature;
	}

	static SpaceLayers change2FeatureClass(SpaceLayersType feature, String parentID) {
		SpaceLayers newFeature = new SpaceLayers();

		newFeature.ID = feature.getId();
		newFeature.setParentID(parentID);
		List<SpaceLayerMemberType> tempSLMList = feature.getSpaceLayerMember();
		// List<String>spaceLayerMember = new ArrayList<String>();
		List<String> slm = new ArrayList<String>();

		for (int i = 0; i < tempSLMList.size(); i++) {
			SpaceLayerType s = tempSLMList.get(i).getSpaceLayer();

			slm.add(s.getId());
			docContainer.setFeature(s.getId(), "SpaceLayer", change2FeatureClass(s, newFeature.ID));
		}
		newFeature.spaceLayerMemeber = slm;

		return newFeature;

	}

	public static InterEdges change2FeatureClass(InterEdgesType feature, String parentID) {
		InterEdges newFeature = new InterEdges();

		newFeature.ID = feature.getId();
		newFeature.parentID = parentID;
		List<InterLayerConnectionMemberType> interLayerConnectionMember = feature.getInterLayerConnectionMember();
		List<String> interLayerConnection = new ArrayList<String>();

		for (int i = 0; i < interLayerConnectionMember.size(); i++) {
			InterLayerConnectionType tempILC = new InterLayerConnectionType();
			tempILC = interLayerConnectionMember.get(i).getInterLayerConnection();
			interLayerConnection.add(tempILC.getId());
			docContainer.setFeature(tempILC.getId(), "InterLayerConnection",
					change2FeatureClass(tempILC, newFeature.ID));
		}

		newFeature.interLayerConnectionMember = interLayerConnection;

		return newFeature;
	}

	static Edges change2FeatureClass(EdgesType feature, String parentID) {
		Edges newFeature = new Edges();

		newFeature.ID = feature.getId();
		newFeature.setParentID(parentID);
		List<TransitionMemberType> tm = feature.getTransitionMember();
		List<Transition> transitionMemberReference = new ArrayList<Transition>();

		for (int i = 0; i < tm.size(); i++) {
			TransitionType tempTM = tm.get(i).getTransition();
			docContainer.setFeature(tempTM.getId(), "Transition", change2FeatureClass(tempTM, newFeature.ID));
			transitionMemberReference.add(change2FeatureClass(tempTM, newFeature.ID));
		}
		newFeature.transitionMember = transitionMemberReference;
		return newFeature;
	}

	static InterLayerConnection change2FeatureClass(InterLayerConnectionType feature, String parentID) {
		InterLayerConnection newFeature = new InterLayerConnection();

		newFeature.ID = feature.getId();
		newFeature.setParentID(parentID);

		List<SpaceLayerPropertyType> tempSLList = feature.getConnectedLayers();
		List<StatePropertyType> tempILCList = feature.getInterConnects();
		List<String> spacelayerList = new ArrayList<String>();
		List<String> interConnectionList = new ArrayList<String>();

		for (int i = 0; i < tempSLList.size(); i++) {
			SpaceLayerType sl = tempSLList.get(i).getSpaceLayer();
			docContainer.setFeature(sl.getId(), "SpaceLayer", change2FeatureClass(sl, newFeature.ID));
			spacelayerList.add(sl.getId());
		}

		for (int i = 0; i < tempILCList.size(); i++) {
			StateType s = tempILCList.get(i).getState();
			interConnectionList.add(s.getId());
			docContainer.setFeature(s.getId(), "State", change2FeatureClass(s, newFeature.ID));
		}

		if (spacelayerList.size() != 2 || interConnectionList.size() != 2) {
			System.out.println("Converter : number of SpaceLayer or InterConnection is not 2 at InterLayerConnection");
		} else {
			newFeature.connectedLayers[0] = spacelayerList.get(0);
			newFeature.connectedLayers[1] = spacelayerList.get(1);

			newFeature.interConnects[0] = interConnectionList.get(0);
			newFeature.interConnects[1] = interConnectionList.get(1);
		}

		return newFeature;
	}

	public static PrimalSpaceFeatures change2FeatureClass(PrimalSpaceFeaturesType feature, String parentID)
			throws JAXBException {
		PrimalSpaceFeatures newFeature = new PrimalSpaceFeatures();
		//JAXBContext context = JaxbUtil.createIndoorGMLContext();
		// net.opengis.indoorgml.core.v_1_0.ObjectFactory objectFactory = new
		// ObjectFactory();
		newFeature.ID = feature.getId();
		newFeature.setParentID(parentID);
		for (int i = 0; i < feature.getCellSpaceBoundaryMember().size(); i++) {
			CellSpaceBoundaryMemberType temp = feature.getCellSpaceBoundaryMember().get(i);
			CellSpaceBoundaryType cs = temp.getCellSpaceBoundary().getValue();
			docContainer.setFeature(cs.getId(), "CellSpaceBoundary", change2FeatureClass(cs, newFeature.ID));

		}
		for (int i = 0; i < feature.getCellSpaceMember().size(); i++) {
			CellSpaceMemberType temp = feature.getCellSpaceMember().get(i);
			CellSpaceType cs = temp.getCellSpace().getValue();
			docContainer.setFeature(cs.getId(), "CellSpace", change2FeatureClass(cs, newFeature.ID));
			// CellSpaceType cellSpace =
			// objectFactory.createCellSpace(temp.getCellSpace().getValue());

		}

		return newFeature;
	}

	static Nodes change2FeatureClass(NodesType feature, String parentID) {
		Nodes newFeature = new Nodes();

		newFeature.ID = feature.getId();
		newFeature.parentID = parentID;
		List<StateMemberType> tempML = feature.getStateMember();
		List<String> stateList = new ArrayList<String>();

		for (int i = 0; i < tempML.size(); i++) {
			StateType tempState = tempML.get(i).getState();
			docContainer.setFeature(tempState.getId(), "State", change2FeatureClass(tempState, newFeature.ID));
			stateList.add(tempState.getId());
		}
		newFeature.stateMember = stateList;
		return newFeature;
	}

	static SpaceLayer change2FeatureClass(SpaceLayerType feature, String parentID) {
		SpaceLayer newFeature = new SpaceLayer();

		newFeature.ID = feature.getId();
		newFeature.function = feature.getFunction();
		newFeature.setParentID(parentID);
		// newFeature.createDate = feature.getCreationDate();
		// newFeature.terminateDate = feature.getTerminateDate();
		newFeature.classType = feature.getClazz();

		List<String> edgeList = new ArrayList<String>();
		List<EdgesType> tempEL = feature.getEdges();

		List<String> nodesList = new ArrayList<String>();
		List<NodesType> tempNL = feature.getNodes();

		for (int i = 0; i < tempEL.size(); i++) {
			edgeList.add(tempEL.get(i).getId());
			docContainer.setFeature(tempEL.get(i).getId(), "Edges", change2FeatureClass(tempEL.get(i), newFeature.ID));
			// tempEdgeList.add(change2FeatureClass(tempEL.get(i)));

		}
		for (int i = 0; i < tempNL.size(); i++) {
			nodesList.add(tempNL.get(i).getId());
			docContainer.setFeature(tempNL.get(i).getId(), "Nodes", change2FeatureClass(tempNL.get(i), newFeature.ID));
		}
		newFeature.edges = edgeList;
		newFeature.nodes = nodesList;
		return newFeature;
	}

	SpaceLayerClassType change2FeatureClass(SpaceLayerClassTypeType feature) {
		return null;
	}
	static State change2FeatureClass(StateType feature, String parentID){
		State newFeature = new State();
		newFeature.setID(feature.getId());
		newFeature.setParentID(parentID);
		//newFeature.geometry = 
		
		if(feature.getDuality() == null){
			System.out.println("Convert to State : There is no Duality");
		}
		else{
			newFeature.setDuality(feature.getDuality().getHref());					
		}
		/*
		 * if(feature.getHref() != null){
			newFeature.setDuality(feature.getHref());
		}
		 * */
		return newFeature;
	}
	static Transition change2FeatureClass(TransitionType feature, String parentID) {
		Transition newFeature = new Transition();

		newFeature.ID = feature.getId();
		newFeature.parentID = parentID;

		Object geometry = feature.getGeometry().getAbstractCurve().getValue();
		if (geometry instanceof CurvePropertyType) {
			//newFeature.geometry = (CurveType) geometry;
			System.out.println("Converter to Transition : Not yet support");

		} else {
			System.out.println("Converter to Transition : This is not CurveType geometry");

		}
		List<StatePropertyType> tempConnect = feature.getConnects();
		for (int i = 0; i < tempConnect.size(); i++) {
			//StateType tempState = tempConnect.get(i).getState();
			newFeature.connects[i] = tempConnect.get(i).getHref();
			//newFeature.connects[i] = tempState.getId();
		}
		// newFeature.duality =
		// change2FeatureClass((CellSpaceBoundaryType)feature.getDuality().getCellSpaceBoundary().getValue());
		Object duality = feature.getDuality();
		if(duality == null){
			System.out.println("Converter to Transition : There is no Duality");
		}
		else{
			CellSpaceBoundaryType tempBoundary = feature.getDuality().getCellSpaceBoundary().getValue();
			newFeature.duality = tempBoundary.getId();
		}
		newFeature.weight = feature.getWeight();
		newFeature.name = feature.getRole();
		return newFeature;
	}

	typeOfTopoExpressionCode change2FeatureClass(TypeOfTopoExpressionCodeEnumerationType feature) {
		typeOfTopoExpressionCode newFeature = new typeOfTopoExpressionCode();

		return null;
	}

}
