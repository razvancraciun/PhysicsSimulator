package simulator.model;

import simulator.misc.Vector;

public class MassLossingBody extends Body {
	
	private double lossFactor;
	private double lossFrequency;
	
	private double count=0.0;
	
	public MassLossingBody(String id,Vector velocity,Vector acceleration,Vector position,double mass,
			double lossFactor,double lossFrequency) {
		super(id,velocity,acceleration,position,mass);
		this.lossFactor=lossFactor;
		this.lossFrequency=lossFrequency;
	}
	
	@Override
	public void move(double t) {
		super.move(t);
		count+=t;
		if(count>=lossFrequency) {
			mass=mass*(1-lossFactor);
			count=0.0;
		}
	}
	
	public void setLossFactor(double lossFactor) {this.lossFactor=lossFactor;}
	public double getLossFactor() {return lossFactor;}
	public void setLossFrequency(double lossFrequency) {this.lossFrequency=lossFrequency;}
	public double getLossFrequency() {return this.lossFrequency;}
	
}
