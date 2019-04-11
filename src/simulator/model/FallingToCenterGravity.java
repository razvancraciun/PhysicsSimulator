package simulator.model;

import java.util.List;


public class FallingToCenterGravity implements GravityLaws{

		@Override
		public void apply(List<Body> bodies) {
			double g=9.81;
			for(Body b:bodies) {
				b.setAcceleration(b.getPosition().direction().scale(-g));
			}
		}
		
		@Override
		public String toString() {
			return "Falling to center";
		}
}
