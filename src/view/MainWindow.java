package view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import simulator.control.Controller;

@SuppressWarnings("serial")
public class MainWindow extends JFrame {

	private Controller _ctrl;
	
	public MainWindow(Controller ctrl) {
		super("Physics Simulator");
		_ctrl = ctrl;
		initGUI();
	}
	
	private void initGUI() {
	JPanel mainPanel = new JPanel(new BorderLayout());
	setContentPane(mainPanel);
	
	JPanel container=new JPanel();
	container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
	
	BodiesTable bodiesTable=new BodiesTable(_ctrl);
	Viewer viewer = new Viewer(_ctrl);
	bodiesTable.setPreferredSize(new Dimension(300,300));
	viewer.setPreferredSize(new Dimension(300,300));
	container.add(bodiesTable);
	container.add(viewer);
	
	ControlPanel cp = new ControlPanel(_ctrl);
	mainPanel.add(cp,BorderLayout.NORTH);
	mainPanel.add(container,BorderLayout.CENTER);
	
	
	StatusBar sb= new StatusBar(_ctrl);
	mainPanel.add(sb,BorderLayout.SOUTH);
	
	}
}
