package view;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;

import simulator.control.Controller;

@SuppressWarnings("serial")
public class BodiesTable extends JPanel {
	
	private BodiesTableModel _bodiesTM;
	private JTable _table;
	
	
	BodiesTable(Controller ctrl) {
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.black, 2),"Bodies", TitledBorder.LEFT, TitledBorder.TOP));
		
		_bodiesTM=new BodiesTableModel(ctrl);
		_table = new JTable(_bodiesTM);
		add(new JScrollPane(_table));
	}
	
	
}
