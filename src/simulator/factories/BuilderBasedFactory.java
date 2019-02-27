package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class BuilderBasedFactory<T> implements Factory<T> {
	
	private List<Builder<T>> builders;

	public BuilderBasedFactory (List<Builder<T>> builders) {
		this.builders=builders;
	}
	
	@Override
	public T createInstance(JSONObject info) {
		for(int i=0;i<builders.size();i++) {
			T obj=builders.get(i).createInstance(info);
			if(obj!=null) {
				return obj;
			}
				
		}
		throw new IllegalArgumentException();
	}

	@Override
	public List<JSONObject> getInfo() {
		List<JSONObject> info = new ArrayList<JSONObject>();
		for(Builder<?> b : builders) {
			info.add(b.getBuilderInfo());
		}
		return info;
	}

}
