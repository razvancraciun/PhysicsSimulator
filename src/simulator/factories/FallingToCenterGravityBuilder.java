package simulator.factories;

import org.json.JSONObject;

import simulator.model.FallingToCenterGravity;
import simulator.model.GravityLaws;

public class FallingToCenterGravityBuilder implements Builder<GravityLaws>{

	@Override
	public GravityLaws createInstance(JSONObject info) {
		if(info.get("type").toString().equals("[\"ftcg\"]")) {
			return new FallingToCenterGravity();
		}
		return null;
	}

	@Override
	public JSONObject getBuilderInfo() {
		JSONObject result=new JSONObject();
		result.append("type", new String("ftcg"));
		result.append("desc", new String("Falling to center gravity"));
		result.append("data", new JSONObject());
		return result;
	}

}
