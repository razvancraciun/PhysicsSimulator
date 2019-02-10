package simulator.model;

import simulator.misc.Vector;

public class Body {
	private String id;
	private Vector velocity;
	private Vector acceleration;
	private Vector position;
	protected double mass;
	
	public Body() {
		this.id="default";
		this.velocity=new Vector(2);
		this.acceleration=new Vector(2);
		this.position=new Vector(2);
		this.mass=0;
	}
	
	public Body(String id,Vector velocity,Vector acceleration,Vector position,double mass) {
		this.id=id;
		this.velocity=velocity;
		this.acceleration=acceleration;
		this.position=position;
		this.acceleration=acceleration;
		this.mass=mass;
	}
	
	public String toString() {
		return "{ \"id\": "+id+ ", \"mass\":" + mass+", \"pos\":"+position+", \"vel\":"+velocity+", \"acc\":"+acceleration+"}";
	}
	
	public String getId() {
		return id;
	}
	
	public Vector getVelocity() {
		return new Vector(this.velocity);
	}
	public Vector getAcceleration() {
		return new Vector(this.acceleration);
	}
	public Vector getPosition() {
		return new Vector(this.position);
	}
	public double getMass() {
		return mass;
	}
	void setVelocity(Vector v) {
		this.velocity=new Vector(v);
	}
	void setAcceleration(Vector a) {
		this.acceleration=new Vector(a);
	}
	void setPosition(Vector p) {
		this.position=new Vector(p);
	}
	public void move(double t) {
		this.position=this.position.plus(this.velocity.scale(t));
		this.position=this.position.plus(this.acceleration.scale(1/2*t*t));
		
		this.velocity=this.velocity.plus(this.acceleration.scale(t));
	}
}
