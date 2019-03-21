package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import simulator.control.Controller;
import simulator.misc.Vector;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

@SuppressWarnings("serial")
public class Viewer extends JComponent implements SimulatorObserver {
	// ...
	private int _centerX;
	private int _centerY;
	private double _scale;
	private List<Body> _bodies;
	private boolean _showHelp;
	
	
	Viewer(Controller ctrl) {
		initGUI();
		ctrl.addObserver(this);
	}
	
	private void initGUI() {
		// TODO add border with title
		_bodies = new ArrayList<>();
		_scale = 1.0;
		_showHelp = true;
		addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyChar()) {
				case '-':
					_scale = _scale * 1.1;
					break;
				case '+':
					_scale = Math.max(1000.0, _scale / 1.1);
					break;
				case '=':
					autoScale();
					break;
				case 'h':
					_showHelp = !_showHelp;
					break;
					default:
				}
				repaint();
			}
			@Override
			public void keyReleased(KeyEvent arg0) {// Nothing
			}
	
			@Override
			public void keyTyped(KeyEvent arg0) {//Nothing
			}
		});
		addMouseListener(new MouseListener() {
			@Override
			public void mouseEntered(MouseEvent e) {
			requestFocus();
			}
		
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
	
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
	}
	
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D gr = (Graphics2D) g;
		gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		RenderingHints.VALUE_ANTIALIAS_ON);
		gr.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
		RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		//draw white background
		gr.setColor(Color.white);
		gr.fillRect(0, 0, getWidth(), getHeight());
		// calculate the center
		_centerX = getWidth() / 2;
		_centerY = getHeight() / 2;
		//draw a cross at center
		gr.setColor(Color.red);
		gr.drawLine(_centerX, _centerY-5,_centerX, _centerY+5);
		gr.drawLine(_centerX+5, _centerY,_centerX-5, _centerY);
		//draw bodies
		for(Body b: _bodies) {
			int drawX = (int) (b.getPosition().coordinate(0)/_scale);
			int drawY = (int) (b.getPosition().coordinate(1)/_scale);
			gr.setColor(Color.blue);
			gr.fillOval(_centerX+drawX,_centerY+drawY,5,5);
			gr.setColor(Color.black);
			gr.drawString(b.getId(), drawX, drawY);
		}
		// TODO draw help if _showHelp is true
	}
	
	private void autoScale() {
		double max = 1.0;
		for (Body b : _bodies) {
			Vector p=b.getPosition();
			for(int i=0;i<p.dim();i++) {
				max=Math.max(max, Math.abs(b.getPosition().coordinate(i)));
			}
		}
			
		double size = Math.max(1.0, Math.min((double) getWidth(), (double) getHeight()));
		_scale = max > size ? 4.0 * max / size : 1.0;
	}
	
	// SimulatorObserver methods
	// ...
	@Override
	public void onRegister(List<Body> bodies, double time, double dt, String gLawsDesc) {
		_bodies=bodies;
		repaint();
		
	}
	@Override
	public void onReset(List<Body> bodies, double time, double dt, String gLawsDesc) {
		_bodies=bodies;
		repaint();
		
	}
	@Override
	public void onBodyAdded(List<Body> bodies, Body b) {
		_bodies=bodies;
		autoScale();
		repaint();
		
	}
	@Override
	public void onAdvance(List<Body> bodies, double time) {
		_bodies=bodies;
		repaint();
		
	}
	@Override
	public void onDeltaTimeChanged(double dt) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onGravityLawChanged(String gLawsDesc) {
		// TODO Auto-generated method stub
		
	}

}