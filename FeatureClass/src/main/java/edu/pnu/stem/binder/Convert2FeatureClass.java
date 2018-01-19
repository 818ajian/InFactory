package edu.pnu.stem.binder;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;

import edu.pnu.stem.feature.CellSpace;
import edu.pnu.stem.feature.CellSpaceBoundary;
import edu.pnu.stem.feature.Edges;
import edu.pnu.stem.feature.ExternalObjectReference;
import edu.pnu.stem.feature.ExternalReference;
import edu.pnu.stem.feature.IndoorFeatures;
import edu.pnu.stem.feature.InterEdges;
import edu.pnu.stem.feature.InterLayerConnection;
import edu.pnu.stem.feature.MultiLayeredGraph;
import edu.pnu.stem.feature.Nodes;
import edu.pnu.stem.feature.PrimalSpaceFeatures;
import edu.pnu.stem.feature.SpaceLayer;
import edu.pnu.stem.feature.SpaceLayerClassType;
import edu.pnu.stem.feature.SpaceLayers;
import edu.pnu.stem.feature.State;
import edu.pnu.stem.feature.Transition;
import edu.pnu.stem.feature.typeOfTopoExpressionCode;
import edu.pnu.stem.util.GeometryUtil;
import net.opengis.gml.v_3_2_1.AbstractCurveType;
import net.opengis.gml.v_3_2_1.AbstractSolidType;
import net.opengis.gml.v_3_2_1.AbstractSurfaceType;
import net.opengis.gml.v_3_2_1.CompositeCurveType;
import net.opengis.gml.v_3_2_1.CompositeSolidType;
import net.opengis.gml.v_3_2_1.CompositeSurfaceType;
import net.opengis.gml.v_3_2_1.CurveType;
import net.opengis.gml.v_3_2_1.LineStringType;
import net.opengis.gml.v_3_2_1.OrientableCurveType;
import net.opengis.gml.v_3_2_1.OrientableSurfaceType;
import net.opengis.gml.v_3_2_1.PolygonType;
import net.opengis.gml.v_3_2_1.SolidType;
import net.opengis.gml.v_3_2_1.SurfacePropertyType;
import net.opengis.gml.v_3_2_1.SurfaceType;
import net.opengis.indoorgml.core.v_1_0.CellSpaceBoundaryGeometryType;
import net.opengis.indoorgml.core.v_1_0.CellSpaceBoundaryMemberType;
import net.opengis.indoorgml.core.v_1_0.CellSpaceBoundaryPropertyType;
import net.opengis.indoorgml.core.v_1_0.CellSpaceBoundaryType;
import net.opengis.indoorgml.core.v_1_0.CellSpaceGeometryType;
import net.opengis.indoorgml.core.v_1_0.CellSpaceMemberType;
import net.opengis.indoorgml.core.v_1_0.CellSpacePropertyType;
import net.opengis.indoorgml.core.v_1_0.CellSpaceType;
import net.opengis.indoorgml.core.v_1_0.EdgesType;
import net.opengis.indoorgml.core.v_1_0.ExternalObjectReferenceType;
import net.opengis.indoorgml.core.v_1_0.ExternalReferenceType;
import net.opengis.indoorgml.core.v_1_0.IndoorFeaturesType;
import net.opengis.indoorgml.core.v_1_0.InterEdgesType;
import net.opengis.indoorgml.core.v_1_0.InterLayerConnectionMemberType;
import net.opengis.indoorgml.core.v_1_0.InterLayerConnectionType;
import net.opengis.indoorgml.core.v_1_0.MultiLayeredGraphType;
import net.opengis.indoorgml.core.v_1_0.NodesType;
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

/**
 * 
 * @author Hyung-Gyu Ryoo (hyunggyu.ryoo@gmail.com, Pusan National Univeristy)
 *
 */
public class Convert2FeatureClass {
	public static IndoorFeatures change2FeatureClass(IndoorGMLMap savedMap, String docId, IndoorFeaturesType feature) throws JAXBException {
		// Creating this feature
		IndoorFeatures newFeature = (IndoorFeatures) savedMap.getFeature(feature.getId());
		if(newFeature == null) {
			newFeature = new IndoorFeatures(savedMap, feature.getId());
			savedMap.setFeature(feature.getId(), "IndoorFeatures", newFeature);
		}
		
		// Creating containing features
		PrimalSpaceFeatures childP = change2FeatureClass(savedMap, feature.getPrimalSpaceFeatures().getPrimalSpaceFeatures(),
				feature.getId());
		newFeature.setPrimalSpaceFeatures(childP);
		
		MultiLayeredGraph childM = change2FeatureClass(savedMap, feature.getMultiLayeredGraph().getMultiLayeredGraph(),
				feature.getId());
		newFeature.setMultiLayeredGraph(childM);
		
		return newFeature;
	}

	public static Object change2FeatureClass(IndoorGMLMap savedMap, String parentId, CellSpaceGeometryType feature) {
		Object newFeature = null;
		
		if (feature.isSetGeometry2D()) {
			AbstractSurfaceType geom = feature.getGeometry2D().getAbstractSurface().getValue();
			if (geom instanceof PolygonType) {
				PolygonType poly = (PolygonType) geom;
				
			} else if (geom instanceof SurfaceType) {
				
			} else if (geom instanceof OrientableSurfaceType) {
				
			} else if (geom instanceof CompositeSurfaceType) {
				CompositeSurfaceType tempGeo = (CompositeSurfaceType) geom;
				List<SurfacePropertyType> surfList = tempGeo.getSurfaceMember();
			} else {
				//TODO : Exception
			}
		} else { //feature.isSetGeometry3D()
			AbstractSolidType geom = feature.getGeometry3D().getAbstractSolid().getValue();
			if (geom instanceof SolidType) {
			
			} else if (geom instanceof CompositeSolidType) {
				
			} else {
				//TODO : Exception
			}
		}
		
		if (newFeature != null) {
			savedMap.setFeature(parentId, "Geometry", newFeature);
		}
		return newFeature;
	}

	public static Object change2FeatureClass(IndoorGMLMap savedMap, String parentId, CellSpaceBoundaryGeometryType feature) {
		Object newFeature = null;
		if (feature.isSetGeometry2D()) {
			AbstractCurveType temp = feature.getGeometry2D().getAbstractCurve().getValue();
			if (temp instanceof CompositeCurveType) {
				newFeature = (CompositeCurveType) temp;
			} else if (temp instanceof CurveType) {
				newFeature = (CurveType) temp;
			} else if (temp instanceof LineStringType) {
				newFeature = (LineStringType) temp;
			} else if (temp instanceof OrientableCurveType) {
				newFeature = (OrientableCurveType) temp;
			} else {
				//Excception
			}
		} else if (feature.isSetGeometry3D()) {
			AbstractSurfaceType temp = feature.getGeometry3D().getAbstractSurface().getValue();
			if (temp instanceof CompositeSurfaceType) {
				newFeature = (CompositeSurfaceType) temp;
			} else if (temp instanceof OrientableSurfaceType) {
				newFeature = (OrientableSurfaceType) temp;
			} else if (temp instanceof PolygonType) {
				newFeature = (PolygonType) temp;
			} else if (temp instanceof SurfaceType) {
				newFeature = (SurfaceType) temp;
			}
		}

		if (newFeature != null) {
			savedMap.setFeature(parentId, "Geometry", newFeature);
		}

		return newFeature;

	}

	public static CellSpace change2FeatureClass(IndoorGMLMap savedMap, CellSpaceType feature, String parentId) {
		// Creating this feature
		CellSpace newFeature = (CellSpace) savedMap.getFeature(feature.getId());
		if(newFeature == null) {
			newFeature = new CellSpace(savedMap, feature.getId());
			savedMap.setFeature(feature.getId(), "CellSpace", newFeature);
		}
		
		// Setting parent 
		PrimalSpaceFeatures parent = (PrimalSpaceFeatures) savedMap.getFeature(parentId);
		newFeature.setParent(parent);
		
		// Creating containing features
		
		// 1. duality
		StatePropertyType stateProp = feature.getDuality();
		if(stateProp != null){
			// Check state is defined as instance or is referenced
			
			if(stateProp.getHref() != null) {
				String dualityId = stateProp.getHref().substring(1);
				
				State duality = (State) savedMap.getFeature(dualityId);
				if(duality != null) {
					newFeature.setDuality(duality);
				} else {
					//TODO
					savedMap.setFeature(dualityId, "State", new State(savedMap, dualityId));
				}
			} else {
				//TODO
			}
		}
		
		// 2. geometry
		CellSpaceGeometryType cellSpaceGeom = feature.getCellSpaceGeometry();
		if (cellSpaceGeom != null) {
			change2FeatureClass(savedMap, feature.getId(), cellSpaceGeom);
		} else {
			//TODO : Exception
			System.out.println("Converter : There is no Geometry Information");
		}

		// 3. connects
		List<CellSpaceBoundaryPropertyType> partialBoundaries = feature.getPartialboundedBy();
		for (CellSpaceBoundaryPropertyType cbpProp : partialBoundaries) {
			if(cbpProp.getHref() != null) {
				String connectsId = cbpProp.getHref().substring(1);
				CellSpaceBoundary connects = (CellSpaceBoundary) savedMap.getFeature(connectsId);
				if(connects != null) {
					newFeature.addPartialBoundedBy(connects);
				} else {
					//TODO
					savedMap.setFeature(connectsId, "CellSpaceBoundary", new CellSpaceBoundary(savedMap, connectsId));
				}
			} else {
				//TODO
			};
		}

		return newFeature;
	}

	public static CellSpaceBoundary change2FeatureClass(IndoorGMLMap savedMap, CellSpaceBoundaryType feature, String parentId) {
		// Creating this feature
		CellSpaceBoundary newFeature = (CellSpaceBoundary) savedMap.getFeature(feature.getId());
		if(newFeature == null) {
			newFeature = new CellSpaceBoundary(savedMap, feature.getId());
			savedMap.setFeature(feature.getId(), "CellSpaceBoundary", newFeature);
		}
		
		// Setting parent 
		PrimalSpaceFeatures parent = (PrimalSpaceFeatures) savedMap.getFeature(parentId);
		newFeature.setParent(parent);
		
		// Creating containing features

		// 1. duality
		TransitionPropertyType transitionProp = feature.getDuality();
		if (transitionProp != null) {
			// Check transition is defined as instance or is referenced
			
			if(transitionProp.getHref() != null) {
				String dualityId = transitionProp.getHref().substring(1);
				
				Transition duality = (Transition) savedMap.getFeature(dualityId);
				if(duality != null) {
					newFeature.setDuality(duality);
				} else {
					//TODO
					savedMap.setFeature(dualityId, "Transition", new Transition(savedMap, dualityId));
				}
			} else {
				//TODO
			}
		}
		
		// 2. geometry
		CellSpaceBoundaryGeometryType cellSpaceBoundaryGeom = feature.getCellSpaceBoundaryGeometry();
		if (cellSpaceBoundaryGeom != null) {
			change2FeatureClass(savedMap, feature.getId(), cellSpaceBoundaryGeom);
		} else {
			//TODO : Exception
			System.out.println("Converter : There is no Geometry Information");
		}
		
		return newFeature;
	}

	ExternalObjectReference change2FeatureClass(ExternalObjectReferenceType feature) {
		ExternalObjectReference newFeature = new ExternalObjectReference();

		newFeature.setUri(feature.getUri());
		return newFeature;
	}

	ExternalReference change2FeatureClass(ExternalReferenceType feature) {
		ExternalReference newFeature = new ExternalReference();
		ExternalObjectReference referredObject = new ExternalObjectReference();
		referredObject.setUri(feature.getExternalObject().getUri());

		newFeature.externalObject = referredObject;

		return newFeature;
	}

	public static MultiLayeredGraph change2FeatureClass(IndoorGMLMap savedMap, MultiLayeredGraphType feature, String parentId) {
		// Creating this feature
		MultiLayeredGraph newFeature = (MultiLayeredGraph) savedMap.getFeature(feature.getId());
		if(newFeature == null) {
			newFeature = new MultiLayeredGraph(savedMap, feature.getId());
			savedMap.setFeature(feature.getId(), "MultiLayeredGraph", newFeature);
		}
		
		// Setting parent 
		IndoorFeatures parent = (IndoorFeatures) savedMap.getFeature(parentId);
		newFeature.setParent(parent);

		// Creating containing features
		for (SpaceLayersType slsType : feature.getSpaceLayers()) {
			SpaceLayers sls = change2FeatureClass(savedMap, slsType, newFeature.getId());
			newFeature.addSpaceLayers(sls);
		}
		
		for (InterEdgesType iet : feature.getInterEdges()) {
			InterEdges ie = change2FeatureClass(savedMap, iet, newFeature.getId());
			newFeature.addInterEdges(ie);
		}

		return newFeature;
	}

	public static SpaceLayers change2FeatureClass(IndoorGMLMap savedMap, SpaceLayersType feature, String parentId) {
		// Creating this feature
		SpaceLayers newFeature = (SpaceLayers) savedMap.getFeature(feature.getId());
		if(newFeature == null) {
			newFeature = new SpaceLayers(savedMap, feature.getId());
			savedMap.setFeature(feature.getId(), "SpaceLayers", newFeature);
		}
		
		// Setting parent 
		MultiLayeredGraph parent = (MultiLayeredGraph) savedMap.getFeature(parentId);
		newFeature.setParent(parent);
		
		// Creating containing features
		for (SpaceLayerMemberType slmType : feature.getSpaceLayerMember()) {
			SpaceLayerType slType = slmType.getSpaceLayer();
			SpaceLayer sl = change2FeatureClass(savedMap, slType, newFeature.getId());
			newFeature.addSpaceLayer(sl);
		}

		return newFeature;
	}

	public static InterEdges change2FeatureClass(IndoorGMLMap savedMap, InterEdgesType feature, String parentId) {
		// Creating this feature
		InterEdges newFeature = (InterEdges) savedMap.getFeature(feature.getId());
		if(newFeature == null) {
			newFeature = new InterEdges(savedMap, feature.getId());
			savedMap.setFeature(feature.getId(), "InterEdges", newFeature);
		}
		
		// Setting parent 
		MultiLayeredGraph parent = (MultiLayeredGraph) savedMap.getFeature(parentId);
		newFeature.setParent(parent);
		
		//TODO
		List<InterLayerConnectionMemberType> interLayerConnectionMember = feature.getInterLayerConnectionMember();
		List<InterLayerConnection> interLayerConnection = new ArrayList<InterLayerConnection>();

		for (int i = 0; i < interLayerConnectionMember.size(); i++) {
			InterLayerConnectionType tempILC = new InterLayerConnectionType();
			tempILC = interLayerConnectionMember.get(i).getInterLayerConnection();
			InterLayerConnection temp = new InterLayerConnection(savedMap, tempILC.getId());
			temp.setId(tempILC.getId());
			interLayerConnection.add(temp);
			savedMap.setFeature(tempILC.getId(), "InterLayerConnection",
					change2FeatureClass(savedMap, tempILC, newFeature.getId()));
		}

		newFeature.setInterLayerConnectionMember(interLayerConnection);

		return newFeature;
	}

	static Edges change2FeatureClass(IndoorGMLMap savedMap, EdgesType feature, String parentId) {
		// Creating this feature
		Edges newFeature = (Edges) savedMap.getFeature(feature.getId());
		if(newFeature == null) {
			newFeature = new Edges(savedMap, feature.getId());
			savedMap.setFeature(feature.getId(), "Edges", newFeature);
		}
		
		// Setting parent 
		SpaceLayer parent = (SpaceLayer) savedMap.getFeature(parentId);
		newFeature.setParent(parent);
		
		List<TransitionMemberType> tm = feature.getTransitionMember();
		List<Transition> transitionMemberReference = new ArrayList<Transition>();

		for (int i = 0; i < tm.size(); i++) {
			TransitionType tempTM = tm.get(i).getTransition();
			Transition temp = change2FeatureClass(savedMap, tempTM, newFeature.getId());
			savedMap.setFeature(tempTM.getId(), "Transition", temp);
			// transitionMemberReference.add(change2FeatureClass(tempTM,
			// newFeature.setId()));
			transitionMemberReference.add(temp);
		}
		newFeature.setTransitionMembers(transitionMemberReference);
		return newFeature;
	}

	static InterLayerConnection change2FeatureClass(IndoorGMLMap savedMap, InterLayerConnectionType feature, String parentId) {
		// Creating this feature
		InterLayerConnection newFeature = (InterLayerConnection) savedMap.getFeature(feature.getId());
		if(newFeature == null) {
			newFeature = new InterLayerConnection(savedMap, feature.getId());
			savedMap.setFeature(feature.getId(), "InterLayerConnection", newFeature);
		}
		
		// Setting parent 
		InterEdges parent = (InterEdges) savedMap.getFeature(parentId);
		newFeature.setParent(parent);

		List<SpaceLayerPropertyType> tempSLList = feature.getConnectedLayers();
		List<StatePropertyType> tempILCList = feature.getInterConnects();
		List<SpaceLayer> spacelayerList = new ArrayList<SpaceLayer>();
		List<State> interConnectionList = new ArrayList<State>();

		for (int i = 0; i < tempSLList.size(); i++) {
			SpaceLayerType sl = tempSLList.get(i).getSpaceLayer();
			SpaceLayer temp = new SpaceLayer(savedMap, sl.getId());
			savedMap.setFeature(sl.getId(), "SpaceLayer", change2FeatureClass(savedMap, sl, newFeature.getId()));
			spacelayerList.add(temp);
		}

		for (int i = 0; i < tempILCList.size(); i++) {
			StateType s = tempILCList.get(i).getState();
			State temp = new State(savedMap, s.getId());
			interConnectionList.add(temp);
			savedMap.setFeature(s.getId(), "State", change2FeatureClass(savedMap, s, newFeature.getId()));
		}

		if (spacelayerList.size() != 2 || interConnectionList.size() != 2) {
			System.out.println("Converter : number of SpaceLayer or InterConnection is not 2 at InterLayerConnection");
		} else {
			SpaceLayer[] connectedLayers = null;
			State[] interConnection = null;

			spacelayerList.toArray(connectedLayers);
			interConnectionList.toArray(interConnection);
			newFeature.setConnectedLayers(connectedLayers);
			newFeature.setInterConnects(interConnection);
		}

		return newFeature;
	}

	public static PrimalSpaceFeatures change2FeatureClass(IndoorGMLMap savedMap, PrimalSpaceFeaturesType feature, String parentId)
			throws JAXBException {
		// Creating this feature
		PrimalSpaceFeatures newFeature = (PrimalSpaceFeatures) savedMap.getFeature(feature.getId());
		if(newFeature == null) {
			newFeature = new PrimalSpaceFeatures(savedMap, feature.getId());
			savedMap.setFeature(feature.getId(), "PrimalSpaceFeatures", newFeature);
		}
		
		// Setting parent 
		IndoorFeatures parent = (IndoorFeatures) savedMap.getFeature(parentId);
		newFeature.setParent(parent);
		
		// Creating containing features
		for (CellSpaceMemberType csm : feature.getCellSpaceMember()) {
			CellSpaceType cs = csm.getCellSpace().getValue();
			CellSpace c = change2FeatureClass(savedMap, cs, newFeature.getId());
			newFeature.addCellSpaceMember(c);
		}
		
		for (CellSpaceBoundaryMemberType csbm : feature.getCellSpaceBoundaryMember()) {
			CellSpaceBoundaryType cs = csbm.getCellSpaceBoundary().getValue();
			CellSpaceBoundary cb = change2FeatureClass(savedMap, cs, newFeature.getId());
			newFeature.addCellSpaceBoundaryMember(cb);
		}
		
		return newFeature;
	}

	static Nodes change2FeatureClass(IndoorGMLMap savedMap, NodesType feature, String parentId) {
		// Creating this feature
		Nodes newFeature = (Nodes) savedMap.getFeature(feature.getId());
		if(newFeature == null) {
			newFeature = new Nodes(savedMap, feature.getId());
			savedMap.setFeature(feature.getId(), "Nodes", newFeature);
		}
		
		// Setting parent
		SpaceLayer parent = (SpaceLayer) savedMap.getFeature(parentId);
		newFeature.setParent(parent);
		
		List<StateMemberType> tempML = feature.getStateMember();
		List<State> stateList = new ArrayList<State>();

		for (int i = 0; i < tempML.size(); i++) {
			StateType tempState = tempML.get(i).getState();
			State temp = change2FeatureClass(savedMap, tempState, newFeature.getId());
			savedMap.setFeature(tempState.getId(), "State", temp);
			stateList.add(temp);
		}
		newFeature.setStateMember(stateList);
		return newFeature;
	}

	static SpaceLayer change2FeatureClass(IndoorGMLMap savedMap, SpaceLayerType feature, String parentId) {
		// Creating this feature
		SpaceLayer newFeature = (SpaceLayer) savedMap.getFeature(feature.getId());
		if(newFeature == null) {
			newFeature = new SpaceLayer(savedMap, feature.getId());
			savedMap.setFeature(feature.getId(), "SpaceLayer", newFeature);
		}
		
		// Setting parent
		SpaceLayers parent = (SpaceLayers) savedMap.getFeature(parentId);
		newFeature.setParent(parent);
		
		// newFeature.createDate = feature.getCreationDate();
		// newFeature.terminateDate = feature.getTerminateDate();
		newFeature.setClassType(feature.getClazz());

		// Creating containing features
		for (NodesType nodesType : feature.getNodes()) {
			Nodes ns = change2FeatureClass(savedMap, nodesType, newFeature.getId());
			newFeature.addNodes(ns);
		}
		
		for (EdgesType edgesType : feature.getEdges()) {
			Edges ns = change2FeatureClass(savedMap, edgesType, newFeature.getId());
			newFeature.addEdges(ns);
		}
		return newFeature;
	}

	SpaceLayerClassType change2FeatureClass(SpaceLayerClassTypeType feature) {
		return null;
	}

	static State change2FeatureClass(IndoorGMLMap savedMap, StateType feature, String parentId) {
		// Creating this feature
		State newFeature = (State) savedMap.getFeature(feature.getId());
		if(newFeature == null) {
			newFeature = new State(savedMap, feature.getId());
			savedMap.setFeature(feature.getId(), "State", newFeature);
		}
		
		// Setting parent
		Nodes parent = (Nodes) savedMap.getFeature(parentId);
		newFeature.setParent(parent);
		
		// 1. duality
		CellSpacePropertyType cellSpaceProp = feature.getDuality();
		if(cellSpaceProp != null){
			// Check state is defined as instance or is referenced
			
			if(cellSpaceProp.getHref() != null) {
				String dualityId = cellSpaceProp.getHref().substring(1);
				
				CellSpace duality = (CellSpace) savedMap.getFeature(dualityId);
				if(duality != null) {
					newFeature.setDuality(duality);
				} else {
					//TODO
					savedMap.setFeature(dualityId, "CellSpace", new CellSpace(savedMap, dualityId));
				}
			} else {
				//TODO
			}
		}
		
		// 2. geometry
		if(feature.isSetGeometry()){
			Point geom = Convert2JTSGeometry.convert2Point(feature.getGeometry().getPoint());
			GeometryUtil.setMetadata(geom, "id", feature.getGeometry().getPoint().getId());
			newFeature.setGeometry(geom);
		}
		
		// 3. connects
		List<TransitionPropertyType> featureConnects =  feature.getConnects();
		for (TransitionPropertyType tProp : featureConnects) {
			if(tProp.getHref() != null) {
				String connectsId = tProp.getHref().substring(1);
				Transition connects = (Transition) savedMap.getFeature(connectsId);
				if(connects != null) {
					newFeature.addConnects(connects);
				} else {
					//TODO
					savedMap.setFeature(connectsId, "Transition", new Transition(savedMap, connectsId));
				}
			} else {
				//TODO
			};
		}

		return newFeature;
	}

	static Transition change2FeatureClass(IndoorGMLMap savedMap, TransitionType feature, String parentId) {
		// Creating this feature
		Transition newFeature = (Transition) savedMap.getFeature(feature.getId());
		if(newFeature == null) {
			newFeature = new Transition(savedMap, feature.getId());
			savedMap.setFeature(feature.getId(), "Transition", newFeature);
		}
		
		// Setting parent
		Edges parent = (Edges) savedMap.getFeature(parentId);
		newFeature.setParent(parent);
		
		// 2. geometry
		if(feature.isSetGeometry()){
			LineString geom = Convert2JTSGeometry.convert2LineString((LineStringType)feature.getGeometry().getAbstractCurve().getValue());
			GeometryUtil.setMetadata(geom, "id", feature.getGeometry().getAbstractCurve().getValue().getId());
			newFeature.setGeometry(geom);
		}

		// 3. connects
		List<StatePropertyType> connects = feature.getConnects();
		State[] sArr = new State[2];
		
		String connects1Id = connects.get(0).getHref().substring(1);
		State connects1 = (State) savedMap.getFeature(connects1Id);
		if(connects == null) {
			savedMap.setFeature(connects1Id, "State", new Transition(savedMap, connects1Id));
		}
		sArr[0] = connects1;
		
		String connects2Id = connects.get(1).getHref().substring(1);
		State connects2 = (State) savedMap.getFeature(connects2Id);
		if(connects == null) {
			savedMap.setFeature(connects2Id, "State", new Transition(savedMap, connects2Id));
		}
		sArr[1] = connects2;
		newFeature.setConnects(sArr);
		
		// 4. duality
		CellSpaceBoundaryPropertyType cellSpaceBoundaryProp = feature.getDuality();
		if(cellSpaceBoundaryProp != null){
			// Check state is defined as instance or is referenced
			
			if(cellSpaceBoundaryProp.getHref() != null) {
				String dualityId = cellSpaceBoundaryProp.getHref().substring(1);
				
				CellSpaceBoundary duality = (CellSpaceBoundary) savedMap.getFeature(dualityId);
				if(duality != null) {
					newFeature.setDuality(duality);
				} else {
					//TODO
					savedMap.setFeature(dualityId, "CellSpaceBoundary", new CellSpaceBoundary(savedMap, dualityId));
				}
			} else {
				//TODO
			}
		}

		newFeature.setWeight(feature.getWeight());
		newFeature.setName(feature.getRole());
		return newFeature;
	}

	typeOfTopoExpressionCode change2FeatureClass(TypeOfTopoExpressionCodeEnumerationType feature) {
		typeOfTopoExpressionCode newFeature = new typeOfTopoExpressionCode();

		return null;
	}

}
