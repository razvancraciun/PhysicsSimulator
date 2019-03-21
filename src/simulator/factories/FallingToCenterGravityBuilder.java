package simulator.factories;

import org.json.JSONObject;

import simulator.model.FallingToCenterGravity;
import simulator.model.GravityLaws;

public class FallingToCenterGravityBuilder implements Builder<GravityLaws>{

	private JSONObject _builderInfo;
	
	public FallingToCenterGravityBuilder() {
		_builderInfo=new JSONObject();
		_builderInfo.append("type", new String("ftcg"));
		_builderInfo.append("desc", new String("Falling to center gravity(ftcg)"));
		_builderInfo.append("data", new JSONObject());
	}
	
	@Override
	public GravityLaws createInstance(JSONObject info) {
		if(info.get("type").toString().equals("[\"ftcg\"]")) {
			return new FallingToCenterGravity();
		}
		return null;
	}

	@Override
	public JSONObject getBuilderInfo() {
		
		return _builderInfo;
	}

}
