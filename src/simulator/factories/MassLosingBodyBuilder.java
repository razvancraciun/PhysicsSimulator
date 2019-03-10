package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Vector;
import simulator.model.MassLossingBody;

public class MassLosingBodyBuilder implements Builder<MassLossingBody>{

	@Override
	public MassLossingBody createInstance(JSONObject info) {
		if(info.get("type").equals("mlb")) {
			JSONObject data=info.getJSONObject("data");
			Vector vel=new Vector(JSONArrayToDoubleArray(data.getJSONArray("vel")));
			Vector pos=new Vector(JSONArrayToDoubleArray(data.getJSONArray("pos")));
			return new MassLossingBody(data.get("id").toString(),vel,new Vector(2),pos,data.getDouble("mass"),
					data.getDouble("factor"),data.getDouble("freq"));
		}
		return null;
	}

	@Override
	public JSONObject getBuilderInfo() {
		JSONObject result= new JSONObject();
		double[] arr=new double[2];
		arr[0]=arr[1]=0;
		result.append("type", "mlb");
		result.append("data", new JSONObject()
				.append("id", "bodyID")
				.append("pos", arr)
				.append("vel",new JSONArray(arr))
				.append("mass",0));
		return result;
	}
	
	public double[] JSONArrayToDoubleArray(JSONArray js) {
		double[] arr=new double[js.length()];
		for(int i=0;i<js.length();i++) {
			arr[i]=js.getDouble(i);
		}
		return arr;
	} 
}
