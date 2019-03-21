package simulator.control;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.factories.Factory;
import simulator.model.Body;
import simulator.model.GravityLaws;
import simulator.model.PhysicsSimulator;
import simulator.model.SimulatorObserver;

public class Controller {
	private PhysicsSimulator sim;
	private Factory<Body> bodyFactory;
	private Factory<GravityLaws> gravityLawsFactory;
	
	public Controller(PhysicsSimulator sim, Factory<Body> bodyFactory,Factory<GravityLaws> gravityLawsFactory) {
		this.sim=sim;
		this.bodyFactory=bodyFactory;
		this.gravityLawsFactory=gravityLawsFactory;
	}
	
	public void loadBodies(InputStream in) {
		JSONObject jsonInput=new JSONObject(new JSONTokener(in));
		int length=jsonInput.getJSONArray("bodies").length();	
		for(int i=0;i<length;i++) {
			JSONObject obj=jsonInput.getJSONArray("bodies").getJSONObject(i);
			sim.addBody(bodyFactory.createInstance(obj));
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
	
	public void run(int n) {
		for(int i=0;i<n;i++) {
			sim.advance();
		}
	}
	
	public void reset() {
		sim.reset();
	}
	public void setDeltaTime(double dt) {
		sim.setDeltaTime(dt);
	}
	public void addObserver(SimulatorObserver o) {
		sim.addObserver(o);
	}
	public Factory<GravityLaws> getGravityLawsFactory() {
		return gravityLawsFactory;
	}
	public void setGravityLaws(JSONObject info) {
		GravityLaws gl=gravityLawsFactory.createInstance(info);
		sim.setGravityLaws(gl);
	}
}
