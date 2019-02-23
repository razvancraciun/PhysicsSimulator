package simulator.factories;

import org.json.JSONObject;

public interface Builder<T> {
	public T createInstance(JSONObject info);
	public JSONObject getBuilderInfo();
}
