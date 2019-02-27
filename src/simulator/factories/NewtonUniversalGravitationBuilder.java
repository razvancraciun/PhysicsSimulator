package simulator.factories;

import org.json.JSONObject;

import simulator.model.GravityLaws;
import simulator.model.NewtonUniversalGravitation;

public class NewtonUniversalGravitationBuilder implements Builder<GravityLaws> {

	@Override
	public GravityLaws createInstance(JSONObject info) {
		if(info.get("type").toString().equals("[\"nlug\"]")) {
			return new NewtonUniversalGravitation();
		}
		return null;
	}

	@Override
	public JSONObject getBuilderInfo() {
		JSONObject result=new JSONObject();
		result.append("type", new String("nlug"));
		result.append("desc", new String("Newton's law of universal gravitation"));
		result.append("data", new JSONObject());
		return result;
	}

}
