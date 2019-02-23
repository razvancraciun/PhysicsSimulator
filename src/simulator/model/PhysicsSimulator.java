package simulator.model;

import java.util.ArrayList;
import java.util.List;

public class PhysicsSimulator {
	
	private double realTimePerStep;
	private GravityLaws gravityLaw;
	private double time;
	private List<Body> bodies;
	
	
	public PhysicsSimulator(double realTimePerStep, GravityLaws gravityLaw) {
		if(gravityLaw==null) {
			throw new IllegalArgumentException("Gravity law is null");
		}
		this.realTimePerStep=realTimePerStep;
		this.gravityLaw=gravityLaw;
		this.time=0.0;
		this.bodies=new ArrayList<Body>();
	}
	
	
	public void advance() {
		gravityLaw.apply(bodies);
		for(Body b : bodies) {
			b.move(realTimePerStep);
		}
		time+=realTimePerStep;
	}
	
	
	public void addBody(Body b) {
		if(!idInList(b.getId())) {
			bodies.add(b);
		}
		else throw new IllegalArgumentException("Cannot add body. Another body with the same id already in the list.");
	}
	private boolean idInList(String id) {
		for(Body b: bodies) {
			if(b.getId().equals(id)) {
				return true;
			}
		}
		return false;
	}

	public String toString() {
		String result= "{ \"time\": ";
		result+=time;
		result+=", \"bodies\": [";
		for(Body b:bodies) {
			result+=b.toString()+",";
		}
		result+="]}";
		return result;
	}
}
