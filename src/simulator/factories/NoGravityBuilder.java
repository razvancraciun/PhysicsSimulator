package simulator.factories;

import org.json.JSONObject;

import simulator.model.GravityLaws;
import simulator.model.NewtonUniversalGravitation;

public class NoGravityBuilder implements Builder<GravityLaws> {

	@Override
	public GravityLaws createInstance(JSONObject info) {
		if(info.get("type").equals("ng")) {
			return new NewtonUniversalGravitation();
		}
		return null;
	}

	@Override
	public JSONObject getBuilderInfo() {
		JSONObject result=new JSONObject();
		result.append("type", new String("ng"));
		result.append("desc", new String("No Gravity"));
		result.append("data", new JSONObject());
		return result;
	}

}
