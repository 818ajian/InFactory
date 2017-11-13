import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;

import net.opengis.gml.v_3_2_1.AbstractCurveType;
import net.opengis.gml.v_3_2_1.AbstractFeatureType;
import net.opengis.gml.v_3_2_1.AbstractSolidType;
import net.opengis.gml.v_3_2_1.AbstractSurfaceType;
import net.opengis.gml.v_3_2_1.CompositeCurveType;
import net.opengis.gml.v_3_2_1.CompositeSolidType;
import net.opengis.gml.v_3_2_1.CompositeSurfaceType;
import net.opengis.gml.v_3_2_1.CurvePropertyType;
import net.opengis.gml.v_3_2_1.CurveType;
import net.opengis.gml.v_3_2_1.SolidPropertyType;
import net.opengis.gml.v_3_2_1.SolidType;
import net.opengis.gml.v_3_2_1.SurfacePropertyType;
import net.opengis.gml.v_3_2_1.SurfaceType;
import net.opengis.indoorgml.core.v_1_0.CellSpaceBoundaryType;
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

/**
 * @author jungh
 *	This class is made for exchanging between JAXB classes of OGC-Schemas to feature classes 
 *	that is defined in this module. 
 */
public class Converter {
	AbstractFeatures change2FeatureClass(AbstractFeatureType feature) {
		AbstractFeatures newFeature = new AbstractFeatures();
		Object tempLocation = feature.getLocation().getValue();
		newFeature.ID = feature.getId();
		newFeature.boundedBy = feature.getBoundedBy();
		return newFeature;
	}

	CellSpace change2FeatureClass(CellSpaceType feature) {
		CellSpace newFeature = new CellSpace();
		
		StatePropertyType tempState = feature.getDuality();
		newFeature.duality = tempState.getState().getId();
		newFeature.ID = feature.getId();
		
		
		if(feature.getGeometry2D() != null){
			Object o = feature.getGeometry2D().getAbstractSurface().getValue();
			if(o instanceof SurfaceType){
				newFeature.cellSpaceGeometryObject = (SurfaceType) o;
				newFeature.geometryType = "SurfaceType"; 
			}
			else if(o instanceof CompositeSurfaceType){
				newFeature.cellSpaceGeometryObject = (CompositeSurfaceType) o;
				newFeature.geometryType = "CompositeSurfaceType";
			}
			
		}
		else if(feature.getGeometry3D() != null){
			Object o = feature.getGeometry3D().getAbstractSolid().getValue();
			if(o instanceof SolidType){
				newFeature.cellSpaceGeometryObject = (SolidType) o;
				newFeature.geometryType = "SolidType"; 
			}
			else if(o instanceof CompositeSolidType){
				newFeature.cellSpaceGeometryObject = (CompositeSolidType) o;
				newFeature.geometryType = "CompositeSolidType";
			}
			
		}
		else{
			System.out.println("Converter : There is no Geometry Information");
			
		}
			
			
		newFeature.partialboundedBy = feature.getPartialboundedBy();
		
		return newFeature;
	}
	public CellSpaceType change2JaxbClass(CellSpace feature) {
		//JAXBContextImpl jc = (JAXBContextImpl) JAXBContextImpl.newInstance(CellSpaceType.class);
		CellSpaceType newFeature = new CellSpaceType();
		StatePropertyType duality = new StatePropertyType();
		StateType referredState = new StateType();

		referredState.setId(feature.duality);
		duality.setState(referredState);

		newFeature.setDuality(duality);
		newFeature.setId(feature.ID);
		newFeature.setPartialboundedBy(feature.partialboundedBy);
		
		if(feature.geometryType == "SurfaceType"){
			//newFeature.setGeometry2D((SurfacePropertyType)feature.cellSpaceGeometryObject);
			JAXBElement<? extends AbstractSurfaceType> tempGeometry = (JAXBElement<? extends AbstractSurfaceType>)feature.cellSpaceGeometryObject;			
			SurfacePropertyType tempGeometryProperty = new SurfacePropertyType();
			tempGeometryProperty.setAbstractSurface(tempGeometry);
			
		}
		else if(feature.geometryType == "CompositeSurfaceType"){
			System.out.println("Converter : CompositeSurfaceType is not yet supported");
		}
		else if(feature.geometryType == "SolidType"){
			//newFeature.setGeometry3D((SolidPropertyType)feature.cellSpaceGeometryObject);
			JAXBElement<? extends AbstractSolidType> tempGeometry = (JAXBElement<? extends AbstractSolidType>)feature.cellSpaceGeometryObject;
			// TODO: How to deal from object to JAXBElement 
			SolidPropertyType tempGeometryProperty = new SolidPropertyType();
			tempGeometryProperty.setAbstractSolid(tempGeometry);
			
		}
		else if(feature.geometryType == "CompositeSolidType"){
			System.out.println("Converter : CompositeSolidType is not yet supported");
		}
		
		return newFeature;
	}
	
	public CellSpaceBoundaryType change2JaxbClass(CellSpaceBoundary feature){
		CellSpaceBoundaryType newFeature = new CellSpaceBoundaryType();		
		TransitionPropertyType duality = new TransitionPropertyType();
		TransitionType referredTransition = new TransitionType();
		
		
		referredTransition.setId(feature.duality);
		duality.setTransition(referredTransition);
		newFeature.setDuality(duality);
		newFeature.setId(feature.ID);
		if(feature.cellSpaceBoundaryGeometry instanceof CurveType){
			JAXBElement<? extends AbstractCurveType> tempGeometry = (JAXBElement<? extends AbstractCurveType>)feature.cellSpaceBoundaryGeometry;
			CurvePropertyType tempGeometryProperty = new CurvePropertyType();
			tempGeometryProperty.setAbstractCurve(tempGeometry);
			newFeature.setGeometry2D(tempGeometryProperty);
		}
		else if(feature.cellSpaceBoundaryGeometry instanceof SurfaceType){
			JAXBElement<? extends AbstractSurfaceType> tempGeometry = (JAXBElement<? extends AbstractSurfaceType>)feature.cellSpaceBoundaryGeometry;
			SurfacePropertyType tempGeometryProperty = new SurfacePropertyType();
			tempGeometryProperty.setAbstractSurface(tempGeometry);
			newFeature.setGeometry3D(tempGeometryProperty);
		}
		
		//newFeature.setBoundedBy(feature.);
		
		//if(feature.)
		
		
		return newFeature;
	}
	public CellSpaceBoundary change2FeatureClass(CellSpaceBoundaryType feature) {
		CellSpaceBoundary newFeature = new CellSpaceBoundary();
		
		newFeature.ID = feature.getId();
		TransitionPropertyType tempTransition = feature.getDuality();		
		newFeature.duality = tempTransition.getTransition().getId();
		
		if(feature.getGeometry2D() != null){
			Object o = feature.getGeometry2D().getAbstractCurve().getValue();
			if(o instanceof CurveType){
				newFeature.cellSpaceBoundaryGeometry = (CurveType)o;
			}
			else if(o instanceof CompositeCurveType){
				newFeature.cellSpaceBoundaryGeometry = (CompositeCurveType)o;
			}
						
		}
		else if(feature.getGeometry3D() != null){
			Object o = feature.getGeometry3D().getAbstractSurface().getValue();
			if(o instanceof SurfaceType){
				newFeature.cellSpaceBoundaryGeometry = (SurfaceType)o;
			}
			else if(o instanceof CompositeSurfaceType){
				newFeature.cellSpaceBoundaryGeometry = (CompositeSurfaceType)o;
			}
		}	
		else{
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
	
	public EdgesType change2JaxbClass(Edges feature){
		EdgesType newFeature = new EdgesType();

		newFeature.setId(feature.ID);
		List<String>tempTMIDList = feature.transitionMember;
		List<TransitionMemberType>tempTMList = new ArrayList<TransitionMemberType>();
		for(int i = 0; i< tempTMIDList.size(); i++){
			String tempID = tempTMIDList.get(i);
			TransitionType tempTransition = new TransitionType();
			tempTransition.setId(tempID);
			TransitionMemberType tempTM = new TransitionMemberType();
			tempTM.setTransition(tempTransition);
			tempTMList.add(tempTM);			
		}
		
		//newFeature.setBoundedBy(feature.);
		
		return newFeature;
	}
	
	
	
	Edges change2FeatureClass(EdgesType feature) {
		Edges newFeature = new Edges();
		
		
		newFeature.ID = feature.getId();
		List<TransitionMemberType> tm = feature.getTransitionMember();
		List<TransitionType> tl = new ArrayList<TransitionType>();
		List<String> transitionMemberReference = new ArrayList<String>();
		
		for(int i = 0 ; i < tm.size(); i++){
			TransitionMemberType tempTM = tm.get(i);
			transitionMemberReference.add(tempTM.getTransition().getId());
		}
		newFeature.transitionMember = transitionMemberReference;
		
		return newFeature;
	}

	ExternalObjectReference change2FeatureClass(ExternalObjectReferenceType feature) {
		ExternalObjectReference newFeature = new ExternalObjectReference();
		
		newFeature.uri = feature.getUri();
		return newFeature;
	}

	ExternalReference change2FeatureClass(ExternalReferenceType feature) {
		ExternalReference newFeature = new ExternalReference();
		
		newFeature.externalObject = feature.getExternalObject();
		
		return newFeature;
	}

	IndoorFeatures change2FeatureClass(IndoorFeaturesType feature) {
		IndoorFeatures newFeature = new IndoorFeatures();
		
		newFeature.ID = feature.getId();
		MultiLayeredGraphType tempML = new MultiLayeredGraphType();		
		newFeature.multiLayeredGraph = change2FeatureClass(feature.getMultiLayeredGraph());
		
		PrimalSpaceFeaturesPropertyType tempPSFP = new PrimalSpaceFeaturesPropertyType();
		tempPSFP = feature.getPrimalSpaceFeatures();
		newFeature.primalSpaceFeatures = change2FeatureClass(tempPSFP.getPrimalSpaceFeatures());
		return newFeature;
	}
	SpaceLayers change2FeatureClass(SpaceLayersType feature){
		SpaceLayers newFeature = new SpaceLayers();
		
		newFeature.ID = feature.getId();
		List<SpaceLayerMemberType>tempSLMList = feature.getSpaceLayerMember();
		List<String>spaceLayerMember = new ArrayList<String>();
		
		for(int i = 0 ; i < tempSLMList.size(); i++){
			spaceLayerMember.add(tempSLMList.get(i).getSpaceLayer().getId());
			//SpaceLayerMemberType tempSingleSLM = tempSLMList.get(i);
			// TODO : In SpacelayerMemberType, there is only one SpaceLayer feature. The other member type also has same problem.
			
		}
		newFeature.spaceLayerMemeber = spaceLayerMember;
		
		return newFeature;
		
	}
	InterEdges change2FeatureClass(InterEdgesType feature) {
		InterEdges newFeature = new InterEdges();
		
		newFeature.ID = feature.getId();
		List<InterLayerConnectionMemberType> interLayerConnectionMember = feature.getInterLayerConnectionMember();
		List<String> interLayerConnection = new ArrayList<String>();
		
		
		for(int i = 0 ; i < interLayerConnectionMember.size() ; i++){
			InterLayerConnectionMemberType tempILC = new InterLayerConnectionMemberType();
			tempILC = interLayerConnectionMember.get(i);
			interLayerConnection.add(tempILC.getInterLayerConnection().getId());
		}
		
		newFeature.interLayerConnectionMember = interLayerConnection;
		
		return newFeature;
	}

	InterLayerConnection change2FeatureClass(InterLayerConnectionType feature) {
		InterLayerConnection newFeature = new InterLayerConnection();
		
		newFeature.ID = feature.getId();
		List<SpaceLayerPropertyType> tempSLList = feature.getConnectedLayers();
		List<StatePropertyType> tempILCList = feature.getInterConnects();
		List<String> spacelayerList = new ArrayList<String>();
		List<String> interConnectionList = new ArrayList<String>();
		
		for(int i = 0 ; i < tempSLList.size(); i++){
			SpaceLayerPropertyType tempSingleSL = tempSLList.get(i);
			spacelayerList.add(tempSingleSL.getSpaceLayer().getId());
		}
		
		for(int i = 0 ; i < tempILCList.size(); i++){
			StatePropertyType tempSingleS = tempILCList.get(i);
			interConnectionList.add(tempSingleS.getState().getId());
		}
		
		if(spacelayerList.size() != 2 && interConnectionList.size()!= 2){
			System.out.println("Converter : number of SpaceLayer or InterConnection is not 2 at InterLayerConnection");			
		}
		else{
			newFeature.connectedLayers[0] = spacelayerList.get(0);
			newFeature.connectedLayers[1] = spacelayerList.get(1);
			
			newFeature.interConnects[0] = interConnectionList.get(0);
			newFeature.interConnects[1] = interConnectionList.get(1);
		}
		
		
		return newFeature;
	}

	MultiLayeredGraph change2FeatureClass(MultiLayeredGraphType feature) {
		MultiLayeredGraph newFeature = new MultiLayeredGraph();
		
		newFeature.ID = feature.getId();
		
		List<InterEdgesType> tempIEList = feature.getInterEdges();
		List<SpaceLayersType> tempSLList = feature.getSpaceLayers();
		
		List<InterEdges> interEdges = new ArrayList<InterEdges>();
		List<SpaceLayers> spaceLayers = new ArrayList<SpaceLayers>();
		
		for(int i = 0 ; i < tempIEList.size() ; i++){
			interEdges.add(change2FeatureClass(tempIEList.get(i)));
		}
		for(int i = 0 ; i < tempSLList.size();i++){
			spaceLayers.add(change2FeatureClass(tempSLList.get(i)));
		}
		
		newFeature.interEdges = interEdges;
		newFeature.spaceLayers = spaceLayers;
		
		
		return newFeature;
	}

	Nodes change2FeatureClass(NodesType feature) {
		Nodes newFeature = new Nodes();
		
		newFeature.ID = feature.getId();
		
		//newFeature.stateMember = feature.getStateMember();
		List<StateMemberType>tempML = feature.getStateMember();
		//List<StateType>tempStateList = new ArrayList<StateType>();	
		List<String>stateList = new ArrayList<String>();
		
		for(int i = 0 ; i < tempML.size() ; i++){
			StateMemberType tempSM = tempML.get(i);
			StateType tempState = tempSM.getState();
			//tempStateList.add(tempState);
			stateList.add(tempState.getId());
		}
		newFeature.stateMember = stateList;
		return newFeature;
	}

	PrimalSpaceFeatures change2FeatureClass(PrimalSpaceFeaturesType feature) {
		PrimalSpaceFeatures newFeature = new PrimalSpaceFeatures();
		//List<CellSpaceMemberType>tempSMList= feature.get
		
		return newFeature;
	}

	SpaceLayer change2FeatureClass(SpaceLayerType feature) {
		SpaceLayer newFeature = new SpaceLayer();
		
		newFeature.ID = feature.getId();
		newFeature.function = feature.getFunction();
		//newFeature.createDate = feature.getCreationDate();
		//newFeature.terminateDate = feature.getTerminateDate();
		newFeature.classType = feature.getClazz();
		
		List<Edges>tempEdgeList = new ArrayList<Edges>();		
		List<EdgesType>tempEL = feature.getEdges();
		
		List<Nodes>nodesList = new ArrayList<Nodes>();
		List<NodesType>tempNList = feature.getNodes();
		
		for(int i = 0 ; i < tempEL.size() ; i++){
			tempEdgeList.add(change2FeatureClass(tempEL.get(i)));
		}
		for(int i = 0; i < tempNList.size(); i++){
			nodesList.add(change2FeatureClass(tempNList.get(i)));
		}
		newFeature.edges = tempEdgeList;
		newFeature.nodes = nodesList;
		return newFeature;
	}

	SpaceLayerClassType change2FeatureClass(SpaceLayerClassTypeType feature) {
		return null;
	}

	State change2FeatureClass(StateType feature) {
		State newFeature = new State();
		CellSpacePropertyType tempCS = feature.getDuality();
		List<TransitionPropertyType> tempTransition = feature.getConnects();
		
		
		newFeature.ID = feature.getId();
		newFeature.geometry = feature.getGeometry();
		CellSpaceType tempDuality = (CellSpaceType)tempCS.getCellSpace().getValue();
		//In document, duality is written in ref. So use ID for reference.
		newFeature.duality = tempDuality.getId();
		
		newFeature.connects = feature.getConnects();
		
		return newFeature;
	}

	Transition change2FeatureClass(TransitionType feature) {
		Transition newFeature = new Transition();
		
		newFeature.ID = feature.getId();
		Object geometry = feature.getGeometry().getAbstractCurve().getValue();
		if(geometry instanceof CurveType){
			newFeature.geometry = (CurveType) geometry;
		}
		else{
			System.out.println("Converter to Transition : This is not CurveType geometry");
			
		}
		
		//newFeature.duality = change2FeatureClass((CellSpaceBoundaryType)feature.getDuality().getCellSpaceBoundary().getValue());
		CellSpaceBoundaryType tempBoundary = (CellSpaceBoundaryType)feature.getDuality().getCellSpaceBoundary().getValue();
		newFeature.duality = tempBoundary.getId();
		newFeature.weight = feature.getWeight();
		newFeature.name = feature.getRole();
		return newFeature;
	}

	typeOfTopoExpressionCode change2FeatureClass(TypeOfTopoExpressionCodeEnumerationType feature) {
		typeOfTopoExpressionCode newFeature = new typeOfTopoExpressionCode();
		
	
		
		return null;
	}
}
