package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class BuilderBasedFactory<T> implements Factory<T> {
	
	private List<Builder<T>> _builders;
	private List<JSONObject> _info;

	public BuilderBasedFactory (List<Builder<T>> builders) {
		_builders=builders;
		
		//building info
		List<JSONObject> info = new ArrayList<JSONObject>();
		for(Builder<?> b : builders) {
			info.add(b.getBuilderInfo());
		}
		_info=info;
	}
	
	@Override
	public T createInstance(JSONObject info) {
		for(int i=0;i<_builders.size();i++) {
			T obj=_builders.get(i).createInstance(info);
			if(obj!=null) {
				return obj;
			}
				
		}
		throw new IllegalArgumentException();
	}

	@Override
	public List<JSONObject> getInfo() {	
		return _info;
	}

}
