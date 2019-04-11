package view;

import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import simulator.control.Controller;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

public class StatusBar extends JPanel implements SimulatorObserver {

	private static final long serialVersionUID = 2036235232857617800L;
	private JLabel _currTime;
	private JLabel _currLaws;
	private JLabel _numOfBodies;
	
	StatusBar(Controller ctrl) {
		_currTime=new JLabel("0");
		_currLaws=new JLabel();
		_numOfBodies=new JLabel();
		initGUI();
		ctrl.addObserver(this);
	}
	
	
	private void initGUI() {
		this.setLayout( new FlowLayout( FlowLayout.LEFT ));
		this.setBorder( BorderFactory.createBevelBorder( 1 ));
		JLabel timeLabel=new JLabel("Current time: ");
		add(timeLabel);	
		add(_currTime);
		add(new JSeparator(SwingConstants.VERTICAL));
		JLabel lawsLabel=new JLabel("Current laws:");
		add(lawsLabel);
		add(_currLaws);
		add(new JSeparator(SwingConstants.VERTICAL));
		JLabel bodiesLabel=new JLabel("Number of bodies: ");
		add(bodiesLabel);
		add(_numOfBodies);
	}


	@Override
	public void onRegister(List<Body> bodies, double time, double dt, String gLawsDesc) {	
		_numOfBodies.setText(""+bodies.size());
		_currTime.setText(""+time);
		_currLaws.setText(gLawsDesc);
	}

	@Override
	public void onReset(List<Body> bodies, double time, double dt, String gLawsDesc) {
		_currTime.setText("0");
		
	}

	@Override
	public void onBodyAdded(List<Body> bodies, Body b) {
		_numOfBodies.setText(""+bodies.size());
		
	}

	@Override
	public void onAdvance(List<Body> bodies, double time) {
		_currTime.setText(""+time);
		
	}

	@Override
	public void onDeltaTimeChanged(double dt) {
		
	}

	@Override
	public void onGravityLawChanged(String gLawsDesc) {
		_currLaws.setText(gLawsDesc);
	}

}
