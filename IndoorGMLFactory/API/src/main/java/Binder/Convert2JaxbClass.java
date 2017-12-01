package Binder;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

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
import FeatureClassReference.SpaceLayers;
import FeatureClassReference.State;
import FeatureClassReference.Transition;
import net.opengis.indoorgml.core.v_1_0.CellSpaceBoundaryMemberType;
import net.opengis.indoorgml.core.v_1_0.CellSpaceBoundaryPropertyType;
import net.opengis.indoorgml.core.v_1_0.CellSpaceBoundaryType;
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
import net.opengis.indoorgml.core.v_1_0.MultiLayeredGraphPropertyType;
import net.opengis.indoorgml.core.v_1_0.MultiLayeredGraphType;
import net.opengis.indoorgml.core.v_1_0.NodesType;
import net.opengis.indoorgml.core.v_1_0.PrimalSpaceFeaturesPropertyType;
import net.opengis.indoorgml.core.v_1_0.PrimalSpaceFeaturesType;
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


public class Convert2JaxbClass {
	static net.opengis.indoorgml.core.v_1_0.ObjectFactory indoorgmlcoreOF = new net.opengis.indoorgml.core.v_1_0.ObjectFactory();
	net.opengis.gml.v_3_2_1.ObjectFactory gmlOF = new net.opengis.gml.v_3_2_1.ObjectFactory();
	public static CellSpaceType change2JaxbClass(CellSpace feature) throws JAXBException {
		//JAXBContextImpl jc = (JAXBContextImpl) JAXBContextImpl.newInstance(CellSpaceType.class);
		CellSpaceType newFeature = indoorgmlcoreOF.createCellSpaceType();
		StatePropertyType duality = new StatePropertyType();
		duality.setHref(feature.duality);
		//StateType referredState = new StateType();
		//referredState.setId(feature.duality);
		//duality.setState(referredState);

		newFeature.setDuality(duality);
		newFeature.setId(feature.ID);
		
		List<CellSpaceBoundaryPropertyType> partialboundedBy = new ArrayList<CellSpaceBoundaryPropertyType>();
		
		
		for(int i = 0 ; i < feature.partialboundedBy.size() ; i++){
			CellSpaceBoundaryPropertyType tempcsb = indoorgmlcoreOF.createCellSpaceBoundaryPropertyType();
			tempcsb.setHref(feature.partialboundedBy.get(i));
			partialboundedBy.add(tempcsb);
			//CellSpaceBoundaryType tempCsb = new CellSpaceBoundaryType();
			//tempCsb.set(feature.partialboundedBy.get(i));
			//JAXBElement<CellSpaceBoundaryType>  tempCsbJaxb = indoorgmlcoreOF.createCellSpaceBoundary(tempCsb);
			//CellSpaceBoundaryPropertyType tempCsbProperty = new CellSpaceBoundaryPropertyType();			
			//tempCsbProperty.setCellSpaceBoundary(tempCsbJaxb);
			//partialboundedBy.add(tempCsbProperty);
		}
		
		newFeature.setPartialboundedBy(partialboundedBy);
		//newFeature.setPartialboundedBy(feature.partialboundedBy);
		/*
		 * 		if(feature.cellSpaceGeometryObject != null){
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
		}

		 * */
		
		return newFeature;
	}
	
	public static CellSpaceBoundaryType change2JaxbClass(CellSpaceBoundary feature){
		CellSpaceBoundaryType newFeature = indoorgmlcoreOF.createCellSpaceBoundaryType();
		TransitionPropertyType duality = new TransitionPropertyType();
		
		duality.setHref(feature.duality);
		newFeature.setDuality(duality);
		newFeature.setId(feature.ID);
		/*
		 * if(feature.cellSpaceBoundaryGeometry != null){
			if(feature.cellSpaceBoundaryGeometry instanceof CurveType){
				JAXBElement<? extends AbstractCurveType> tempGeometry = (JAXBElement<? extends AbstractCurveType>)feature.cellSpaceBoundaryGeometry;
				CurvePropertyType tempGeometryProperty = new CurvePropertyType();
				tempGeometryProperty.setAbstractCurve(tempGeometry);
				//newFeature.setGeometry2D(tempGeometryProperty);
			}
			else if(feature.cellSpaceBoundaryGeometry instanceof SurfaceType){
				JAXBElement<? extends AbstractSurfaceType> tempGeometry = (JAXBElement<? extends AbstractSurfaceType>)feature.cellSpaceBoundaryGeometry;
				SurfacePropertyType tempGeometryProperty = new SurfacePropertyType();
				tempGeometryProperty.setAbstractSurface(tempGeometry);
				//newFeature.setGeometry3D(tempGeometryProperty);
			}
		}
		 * */
		
		//newFeature.setBoundedBy(feature.);
		
		//if(feature.)
		
		
		return newFeature;
	}
	
	public static EdgesType change2JaxbClass(Edges feature) throws JAXBException{
		EdgesType newFeature = indoorgmlcoreOF.createEdgesType();

		newFeature.setId(feature.ID);
		
		
		List<TransitionMemberType>transitionmember = new ArrayList<TransitionMemberType>();
		for(int j = 0 ; j < feature.transitionMember.size();j++){
			TransitionType temptransition = change2JaxbClass((Transition)Convert2FeatureClass.docContainer.getFeature(feature.transitionMember.get(j)));
			TransitionMemberType temptm = indoorgmlcoreOF.createTransitionMemberType();
			temptm.setTransition(temptransition);
			transitionmember.add(temptm);
		}
		newFeature.setTransitionMember(transitionmember); 
		
		//newFeature.setBoundedBy(feature.);
		
		return newFeature;
	}

	ExternalObjectReferenceType change2JaxbClass(ExternalObjectReference feature){
		ExternalObjectReferenceType newFeature = new ExternalObjectReferenceType();
		newFeature.setUri(feature.uri);
		
		return newFeature;
		
	}
	
	
	ExternalReferenceType change2JaxbClass(ExternalReference feature){
		ExternalReferenceType newFeature = new ExternalReferenceType();
		
		newFeature.setExternalObject(change2JaxbClass(feature.externalObject));
		// TODO:change externalObjectReference
		return newFeature;
	}
	
	static public IndoorFeaturesType change2JaxbClass(IndoorFeatures feature) throws JAXBException{
		IndoorFeaturesType newFeature = new IndoorFeaturesType();
		newFeature.setId(feature.ID);
		if(feature.primalSpaceFeatures != null){
			//Convert2FeatureClass.docContainer.
			PrimalSpaceFeatures p = (PrimalSpaceFeatures) Convert2FeatureClass.docContainer.getFeature(feature.primalSpaceFeatures);
			PrimalSpaceFeaturesPropertyType pp = indoorgmlcoreOF.createPrimalSpaceFeaturesPropertyType();
			pp.setPrimalSpaceFeatures(change2JaxbClass(p));
			newFeature.setPrimalSpaceFeatures(pp);
		}
		if(feature.multiLayeredGraph != null){
			MultiLayeredGraph m = (MultiLayeredGraph) Convert2FeatureClass.docContainer.getFeature(feature.multiLayeredGraph);
			MultiLayeredGraphPropertyType mp = indoorgmlcoreOF.createMultiLayeredGraphPropertyType();
			mp.setMultiLayeredGraph(change2JaxbClass(m));
			newFeature.setMultiLayeredGraph(mp);
		}
		
		return newFeature;
	}
	private MultiLayeredGraphType change2JaxbClass(MultiLayeredGraph feature) throws JAXBException {
		MultiLayeredGraphType newFeature = new MultiLayeredGraphType();
		newFeature.setId(feature.ID);
		
		List<SpaceLayersType>spaceLayers = new ArrayList<SpaceLayersType>();
		List<InterEdgesType>interEdges = new ArrayList<InterEdgesType>();
		for(int i = 0 ; i < feature.spaceLayers.size();i++){
			String tempId = feature.spaceLayers.get(i);
			SpaceLayers tempsl = (SpaceLayers)Convert2FeatureClass.docContainer.getFeature(tempId);
			SpaceLayersType temp = change2JaxbClass(tempsl);
			spaceLayers.add(temp);
		}
		
		for(int i = 0 ; i < feature.interEdges.size();i++){
			InterEdges tempie = (InterEdges)Convert2FeatureClass.docContainer.getFeature(feature.interEdges.get(i));
			InterEdgesType temp = change2JaxbClass(tempie);
			interEdges.add(temp);
		}
		newFeature.setInterEdges(interEdges);
		newFeature.setSpaceLayers(spaceLayers);
		
		return newFeature;
	}
	private InterEdgesType change2JaxbClass(InterEdges feature) {
		InterEdgesType newFeature = indoorgmlcoreOF.createInterEdgesType();
		newFeature.setId(feature.ID);
		List<InterLayerConnectionMemberType>interlayerconnectionmember = new ArrayList<InterLayerConnectionMemberType>();
		
		for(int i = 0 ; i < feature.interLayerConnectionMember.size();i++){
			InterLayerConnection tempilc = (InterLayerConnection) Convert2FeatureClass.docContainer.getFeature(feature.interLayerConnectionMember.get(i));
			InterLayerConnectionType temp = change2Jaxb(tempilc);
			InterLayerConnectionMemberType tempmember = indoorgmlcoreOF.createInterLayerConnectionMemberType();
			tempmember.setInterLayerConnection(temp);
			interlayerconnectionmember.add(tempmember);
		}
		newFeature.setInterLayerConnectionMember(interlayerconnectionmember);
		
		return newFeature;
	}
	

	private InterLayerConnectionType change2Jaxb(InterLayerConnection feature) {
		InterLayerConnectionType newFeature = indoorgmlcoreOF.createInterLayerConnectionType();
		
		newFeature.setId(feature.getID());
		List<StatePropertyType>interConnects = new ArrayList<StatePropertyType>();
		List<SpaceLayerPropertyType>connectedLayer = new ArrayList<SpaceLayerPropertyType>();
		for(int i = 0 ; i < feature.getInterConnects().length; i++){
			StatePropertyType temp = indoorgmlcoreOF.createStatePropertyType();
			temp.setHref(feature.getInterConnects()[i]);
			interConnects.add(temp);
		}
		for(int i = 0 ; i < feature.getConnectedLayers().length;i++){
			SpaceLayerPropertyType temp = indoorgmlcoreOF.createSpaceLayerPropertyType();
			temp.setHref(feature.getConnectedLayers()[i]);
			connectedLayer.add(temp);
		}
		
		newFeature.setConnectedLayers(connectedLayer);
		newFeature.setInterConnects(interConnects);
				
		return newFeature;
	}

	static SpaceLayersType change2JaxbClass(SpaceLayers feature) throws JAXBException{
		SpaceLayersType newFeature = new SpaceLayersType();
		
		newFeature.setId(feature.ID);
		List<SpaceLayerMemberType> spaceLayerMember = new ArrayList<SpaceLayerMemberType>(); 
		for(int i = 0 ; i < feature.spaceLayerMemeber.size(); i++){
			String tempId = feature.spaceLayerMemeber.get(i);
			SpaceLayer tempsl = (SpaceLayer) Convert2FeatureClass.docContainer.getFeature(tempId);
			SpaceLayerType temp = change2JaxbClass(tempsl);
			SpaceLayerMemberType tempsm = new SpaceLayerMemberType();
			tempsm.setSpaceLayer(temp);
			spaceLayerMember.add(tempsm);
		}
		
		newFeature.setSpaceLayerMember(spaceLayerMember);
		
		return newFeature;
	}
	private static SpaceLayerType change2JaxbClass(SpaceLayer feature) throws JAXBException {
		SpaceLayerType newFeature = new SpaceLayerType();
		newFeature.setId(feature.ID);

		List<EdgesType>edgesTypeList = new ArrayList<EdgesType>();
		
		//node ���ͼ�
		//node ���� statemember ã�Ƽ�
		//�� ������ association����.

		List<NodesType>nodesTypeList = new ArrayList<NodesType>();
		
		for(int i = 0 ; i < feature.nodes.size() ; i++){
			Nodes tempnodes = (Nodes) Convert2FeatureClass.docContainer.getFeature(feature.nodes.get(i));
			NodesType tempnodestype = change2JaxbClass(tempnodes);
			nodesTypeList.add(tempnodestype);
		}
		newFeature.setNodes(nodesTypeList);
		
		
		for(int i = 0 ; i < feature.edges.size() ; i++){
			Edges tempEdge = (Edges) Convert2FeatureClass.docContainer.getFeature(feature.edges.get(i));
			EdgesType tempEdgesType = change2JaxbClass(tempEdge);						
			edgesTypeList.add(tempEdgesType);
		}
		newFeature.setEdges(edgesTypeList);
		
		return newFeature;
	}

	private static NodesType change2JaxbClass(Nodes feature) throws JAXBException {
		NodesType newFeature = new NodesType();
		
		newFeature.setId(feature.ID);
		
		List<StateMemberType>smTypeList = new ArrayList<StateMemberType>();
		for(int i = 0 ; i < feature.stateMember.size();i++){
			State tempstate = (State)Convert2FeatureClass.docContainer.getFeature(feature.stateMember.get(i));
			StateType tempstatetype = change2JaxbClass(tempstate);
			StateMemberType tempstatemember = indoorgmlcoreOF.createStateMemberType();
			tempstatemember.setState(tempstatetype);
			smTypeList.add(tempstatemember);
		}
		
		newFeature.setStateMember(smTypeList);
		
	
		
		return newFeature;
	}

	static PrimalSpaceFeaturesType change2JaxbClass(PrimalSpaceFeatures feature) throws JAXBException {
		PrimalSpaceFeaturesType newFeature = new PrimalSpaceFeaturesType();
		newFeature.setId(feature.ID);
		
		List<CellSpaceMemberType>cellspacemember = new ArrayList<CellSpaceMemberType>();
		List<CellSpaceBoundaryMemberType>cellspaceboundarymember = new ArrayList<CellSpaceBoundaryMemberType>();
		for(int i = 0 ; i < feature.cellSpaceMember.size();i++){
			CellSpace tempcellspace = (CellSpace)Convert2FeatureClass.docContainer.getFeature(feature.cellSpaceMember.get(i));
			CellSpaceMemberType tempcellspacemember = indoorgmlcoreOF.createCellSpaceMemberType();			
			tempcellspacemember.setCellSpace(indoorgmlcoreOF.createCellSpace(change2JaxbClass(tempcellspace)));
			cellspacemember.add(tempcellspacemember);
		}

		for(int i = 0 ; i < feature.cellSpaceBoundaryMember.size();i++){
			CellSpaceBoundary tempcellspace = (CellSpaceBoundary)Convert2FeatureClass.docContainer.getFeature(feature.cellSpaceMember.get(i));
			CellSpaceBoundaryMemberType tempcellspacemember = indoorgmlcoreOF.createCellSpaceBoundaryMemberType();
			tempcellspacemember.setCellSpaceBoundary(indoorgmlcoreOF.createCellSpaceBoundary(change2JaxbClass(tempcellspace)));
			cellspaceboundarymember.add(tempcellspacemember);
		}
		
		newFeature.setCellSpaceBoundaryMember(cellspaceboundarymember);
		newFeature.setCellSpaceMember(cellspacemember);
		
		
		
		//newFeature.set
		// TODO Auto-generated method stub
		return newFeature;
	}

	
	
	static StateType change2JaxbClass(State feature) throws JAXBException{
		StateType newFeature = new StateType();
		
		List<TransitionPropertyType>connects = new ArrayList<TransitionPropertyType>();
		
		for(int i = 0 ; i < feature.connects.size(); i++){
			TransitionPropertyType tempTransitionPropertyType = new TransitionPropertyType();
			tempTransitionPropertyType.setHref(feature.connects.get(i));
			connects.add(tempTransitionPropertyType);		
		}
		CellSpacePropertyType duality = indoorgmlcoreOF.createCellSpacePropertyType();
		duality.setHref(feature.getDuality());
		newFeature.setDuality(duality);
		//feature.geometry
		newFeature.setConnects(connects);
		newFeature.setId(feature.ID);
		
		return newFeature;
	}

	static TransitionType change2JaxbClass(Transition feature) throws JAXBException{
		TransitionType newFeature = new TransitionType();
		//CurveType tempCurve = feature.geometry;
		newFeature.setId(feature.ID);
		
		List<StatePropertyType>connects = new ArrayList<StatePropertyType>();
		
		for(int i = 0 ; i < feature.connects.length;i++){
			StatePropertyType temp = indoorgmlcoreOF.createStatePropertyType();
			temp.setHref(feature.connects[i]);
			connects.add(temp);
		}
		newFeature.setConnects(connects);
		
		CellSpaceBoundaryPropertyType duality = indoorgmlcoreOF.createCellSpaceBoundaryPropertyType();
		duality.setHref(feature.getDuality());
		
		newFeature.setDuality(duality);
		newFeature.setWeight(feature.weight);
		
		
		return newFeature;
		
	}
}