package simulator.model;

import java.util.ArrayList;
import java.util.List;

public class PhysicsSimulator {
	
	private double realTimePerStep;
	private GravityLaws gravityLaw;
	private double time;
	private List<Body> bodies;
	private List<SimulatorObserver> observers;
	
	
	public PhysicsSimulator(double realTimePerStep, GravityLaws gravityLaw) {
		if(gravityLaw==null) {
			throw new IllegalArgumentException("Gravity law is null");
		}
		this.realTimePerStep=realTimePerStep;
		this.gravityLaw=gravityLaw;
		this.time=0.0;
		this.bodies=new ArrayList<Body>();
		this.observers=new ArrayList<SimulatorObserver>();
	}
	
	public void addObserver(SimulatorObserver o) {
		if(!checkExistence(o)) {
			observers.add(o);
			o.onRegister(bodies, time, realTimePerStep, gravityLaw.toString());
		}
	}
	
	private boolean checkExistence(SimulatorObserver o) {
		for(SimulatorObserver x :observers) {
			if(x.equals(o)) {
				return true;
			}		
		}
		return false;
	}
	
	public void setDeltaTime(double dt) {
		if(dt<=0) {
			throw new IllegalArgumentException("Invalid Delta Time");
		}		
		realTimePerStep=dt;
		
		for(SimulatorObserver o : observers) {
			o.onDeltaTimeChanged(realTimePerStep);
		}
	}
	
	public void setGravityLaws(GravityLaws gravityLaws) {
		if(gravityLaws==null) {
			throw new IllegalArgumentException("Gravity law is null");
		}
		gravityLaw=gravityLaws;
		
		for(SimulatorObserver o : observers) {
			o.onGravityLawChanged(gravityLaw.toString());
		}
	}
	
	public void reset() {
		bodies=new ArrayList<Body>();
		time=0.0;
		for(SimulatorObserver o : observers) {
			o.onReset(bodies, time, realTimePerStep, gravityLaw.toString());
		}
	}
	
	public void advance() {
		gravityLaw.apply(bodies);
		for(Body b : bodies) {
			b.move(realTimePerStep);
		}
		time+=realTimePerStep;
		for(SimulatorObserver o : observers) {
			o.onAdvance(bodies, realTimePerStep);
		}
	}
	
	
	public void addBody(Body b) {
		if(!idInList(b.getId())) {
			bodies.add(b);
		}
		else throw new IllegalArgumentException("Cannot add body. Another body with the same id already in the list.");
		
		for(SimulatorObserver o : observers) {
			o.onBodyAdded(bodies,b);
		}
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
		for(int i=0;i<bodies.size()-1;i++) {
			result+=bodies.get(i).toString()+",";
		}
		result+=bodies.get(bodies.size()-1).toString();
		result+="]}";
		return result;
	}
}
