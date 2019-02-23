package simulator.control;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.factories.Factory;
import simulator.model.Body;
import simulator.model.PhysicsSimulator;

public class Controller {
	private PhysicsSimulator sim;
	private Factory<Body> factory;
	
	public Controller(PhysicsSimulator sim, Factory<Body> factory) {
		this.sim=sim;
		this.factory=factory;
	}
	
	public void loadBodies(InputStream in) {
		JSONObject jsonInput=new JSONObject(new JSONTokener(in));
		int i=0;
		JSONObject obj=jsonInput.getJSONArray("bodies").getJSONObject(i);
		while(obj!=null) {
			sim.addBody(factory.createInstance(obj));
			i++;
			obj=jsonInput.getJSONArray("bodies").getJSONObject(i);
		}
	}
	
	public void run(int n, OutputStream out) {
		if(out==null) 
			throw new IllegalArgumentException("Output string is null");
		
		String result= "{ \"states\": ["+sim.toString();
		for(int i=0;i<n;i++) {
			sim.advance();
			result+=","+sim.toString();
		}
		result+="]}";
		PrintStream p=new PrintStream(out);
		p.println(result);
	}
}
