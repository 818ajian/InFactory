package edu.pnu.stem.feature;

import java.util.ArrayList;
import java.util.List;

import edu.pnu.stem.binder.IndoorGMLMap;

/**
 * @author jungh Implements SpaceLayersType of IndoorGML 1.0.3
 */
public class SpaceLayers extends AbstractFeature {
	
	/**
	 * List of ID in String Type of SpaceLayers in spaceLayerMember for
	 * reference
	 */
	private List<String> spaceLayerMember;
	private String parentId;

	public SpaceLayers(IndoorGMLMap doc, String id){
		super(doc, id);
		spaceLayerMember = new ArrayList<String>();
	}
	
	public void setParent(MultiLayeredGraph parent) {
		MultiLayeredGraph found = null;
		found = (MultiLayeredGraph)indoorGMLMap.getFeature(parent.getId());
		if(found == null){
			indoorGMLMap.setFutureFeature(parent.getId(), parent);
		}
		this.parentId = parent.getId(); 
	}

	public MultiLayeredGraph getParent() {
		MultiLayeredGraph found = null;
		found = (MultiLayeredGraph)indoorGMLMap.getFeature(this.parentId);
		return found;
	}

	public void setSpaceLayerMember(List<SpaceLayer> spaceLayerMember) {
		for(int i = 0 ; i < spaceLayerMember.size(); i++){
			SpaceLayer found = null;
			if(found == null){
				indoorGMLMap.setFutureFeature(spaceLayerMember.get(i).getId(), spaceLayerMember.get(i));
			}
			if(!this.spaceLayerMember.contains(spaceLayerMember.get(i).getId())){
				this.spaceLayerMember.add(spaceLayerMember.get(i).getId());
			}
		}
	}
	
	public void addSpaceLayer(SpaceLayer sl) {
		if(!this.spaceLayerMember.contains(sl.getId())){
			this.spaceLayerMember.add(sl.getId());
			indoorGMLMap.setFeature(sl.getId(), "SpaceLayer", sl);
		}
	}

	public List<SpaceLayer> getSpaceLayerMember() {
		List<SpaceLayer> spaceLayerMember = new ArrayList<SpaceLayer>();
		if(this.spaceLayerMember.size() != 0){
			for(int i = 0 ; i < this.spaceLayerMember.size() ; i++){
				SpaceLayer found = (SpaceLayer)indoorGMLMap.getFeature(this.spaceLayerMember.get(i));
				spaceLayerMember.add(found);
			}
		}
		return spaceLayerMember;
	}

}
