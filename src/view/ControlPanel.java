package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.json.JSONObject;

import simulator.control.Controller;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

@SuppressWarnings("serial")
public class ControlPanel extends JPanel implements SimulatorObserver {
	
	private Controller _ctrl;
	private boolean _stopped;
	private JFileChooser _chooser;
	private JButton _open;
	private JButton _gravityLaws;
	private JButton _start;
	private JButton _stop;
	private JSpinner _steps;
	private JTextField _deltaTime;
	private JButton _exit;
	
	
	ControlPanel(Controller ctrl) {
		_ctrl = ctrl;
		_stopped = true;
		initGUI();
		_ctrl.addObserver(this);
		_chooser=new JFileChooser();
	}
	private void initGUI() {
		_open = new  JButton(new ImageIcon("resources/icons/open.png"));
			
		_gravityLaws = new JButton(new ImageIcon("resources/icons/physics.png"));

		_start = new JButton(new ImageIcon("resources/icons/run.png"));
				
		_stop = new JButton(new ImageIcon("resources/icons/stop.png"));
		
		JLabel stepsLabel= new JLabel("Steps:");
		_steps = new JSpinner();
		_steps.setValue(10000);
		
		JLabel dtLabel= new JLabel("Delta-Time:");
		_deltaTime= new JTextField("10000");
		
		_exit=new JButton(new ImageIcon("resources/icons/exit.png"));
		addListeners();
		
		add(_open);
		add(_gravityLaws);
		add(_start);
		add(_stop);
		add(stepsLabel);
		add(_steps);
		add(dtLabel);
		add(_deltaTime);
		add(_exit);
	}
	
	private void addListeners() {
		_open.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int returnVal = _chooser.showOpenDialog(ControlPanel.this);

		        if (returnVal == JFileChooser.APPROVE_OPTION) {
		            File file = _chooser.getSelectedFile();
		            _ctrl.reset();
		            try {
		            	_ctrl.loadBodies(new FileInputStream(file));
		            }
		            catch(Exception exc) {
		            	System.out.println("Invalid file");
		            }
		        }
			}
		});
		_gravityLaws.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				//TODO: make a string array from their description and the iterate and find which object matches
				Object[] possibilities = _ctrl.getGravityLawsFactory().getInfo().toArray();
				JSONObject chosen = (JSONObject) JOptionPane.showInputDialog(ControlPanel.this,"Select gravity laws to be used",
						"Gravity Laws Selector",
						JOptionPane.PLAIN_MESSAGE,null,possibilities,possibilities[0]);				
				if(chosen!=null) {
					_ctrl.setGravityLaws(chosen);
				}
			}
			
		});
		_start.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				_open.setEnabled(false);
				_gravityLaws.setEnabled(false);
				_start.setEnabled(false);
				_deltaTime.setEnabled(false);
				_steps.setEnabled(false);
				_stopped=false;
				//TODO: open a dialog if parsedouble doesnt't work
				_ctrl.setDeltaTime(Double.parseDouble(_deltaTime.getText()));
				run_sim((int)_steps.getValue());
			}
			
		});
		_stop.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				_stopped=true;
			}
			
		});
		
		_exit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
			
		});
	}
	
	private void run_sim(int n) {
		if ( n>0 && !_stopped ) {
			try {
				_ctrl.run(1);
			} 
			catch (Exception e) {
				JOptionPane.showMessageDialog(this.getParent(), e,"Error",JOptionPane.ERROR_MESSAGE);
				enableButtons();
				_stopped = true;
				return;
			}
			SwingUtilities.invokeLater( new Runnable() {
				@Override
				public void run() {
					run_sim(n-1);
				}
			});
		} 
		else {
			enableButtons();
		}
	}
	
	private void enableButtons(){
		_stopped = true;
		_start.setEnabled(true);
		_open.setEnabled(true);
		_gravityLaws.setEnabled(true);
		_steps.setEnabled(true);
		_deltaTime.setEnabled(true);
	}
		// SimulatorObserver methods
		// ...
	@Override
	public void onRegister(List<Body> bodies, double time, double dt, String gLawsDesc) {
		_deltaTime.setText(""+dt);
		
	}
	@Override
	public void onReset(List<Body> bodies, double time, double dt, String gLawsDesc) {
		_deltaTime.setText(""+dt);
		
	}
	@Override
	public void onBodyAdded(List<Body> bodies, Body b) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onAdvance(List<Body> bodies, double time) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onDeltaTimeChanged(double dt) {
		_deltaTime.setText(""+dt);
		
	}
	@Override
	public void onGravityLawChanged(String gLawsDesc) {
		// TODO Auto-generated method stub
		//System.out.println("Gravity laws changed"+gLawsDesc);
		
	}
}
