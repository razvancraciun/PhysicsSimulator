package simulator.model;

import java.util.List;

import simulator.misc.Vector;

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
			return "All bodies fall towards the “center of the universe”, i.e., they have a fixed acceleration"
					+ "g= 9.81 towards the origin";
		}
}
