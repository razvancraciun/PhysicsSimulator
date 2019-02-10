package simulator.model;

import java.util.List;

import simulator.misc.Vector;

public class FallingToCenterGravity implements GravityLaws{

		@Override
		public void apply(List<Body> bodies) {
			double g=9.81;
			Vector zero=new Vector(2);
			for(Body b:bodies) {
				b.setAcceleration(zero.minus(b.getPosition().direction().scale(g)));
			}
		}
}
