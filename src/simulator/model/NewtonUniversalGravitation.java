package simulator.model;

import java.util.List;

import simulator.misc.Vector;

public class NewtonUniversalGravitation implements GravityLaws {

	@Override
	public void apply(List<Body> bodies) {
		for(Body b1 : bodies) {
			if(b1.getMass()==0.0) {
				b1.setAcceleration(new Vector(b1.getAcceleration().dim()));
				b1.setVelocity(new Vector(b1.getAcceleration().dim()));
			}
			else {
				Vector F=new Vector(b1.getPosition().dim());
				for(Body b2:bodies) {
					if(b1!=b2) {
						F=F.plus(getFij(b1,b2));
					}
				}
				b1.setAcceleration(F.scale(1/b1.getMass()));
			}
			
		}
	}
	private Vector getFij(Body b1, Body b2) {
		Vector F=new Vector(b1.getPosition().dim());
		double f=6.67E-11* b1.getMass()*b2.getMass()/Math.pow(b1.getPosition().distanceTo(b2.getPosition()),2);
		F= b2.getPosition().minus(b1.getPosition()).direction().scale(f);
		return F;
	}
	
	@Override
	public String toString() {
		return "This law states that two bodies Bi and Bj apply gravitational force on each other, i.e., pull each other.";
	}
}
