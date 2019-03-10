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
		int length=jsonInput.getJSONArray("bodies").length();	
		for(int i=0;i<length;i++) {
			JSONObject obj=jsonInput.getJSONArray("bodies").getJSONObject(i);
			sim.addBody(factory.createInstance(obj));
		}
	}
	
	public void run(int n, OutputStream out) {
		if(out==null) 
			throw new IllegalArgumentException("Output stream is null");
		
		PrintStream p=new PrintStream(out);
		p.println( "{ \"states\": [");
		
		for(int i=0;i<n-1;i++) {
			p.println(sim.toString()+",");
			sim.advance();
		}
		p.println(sim.toString());
		p.println("]}");
	}
}
