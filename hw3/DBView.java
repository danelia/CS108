package assign3;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

public class DBView extends JFrame {

	private static final int TEXT_FIELD_DEFAULT_SIZE = 10;
	
	protected JTextField metropolisField;
	protected JTextField continentField;
	protected JTextField populationField;
	
	protected JButton add;
	protected JButton search;
	
	protected JComboBox populationBox;
	protected JComboBox matchBox;
	
	protected JTable data;
	protected TableModel dataModel;
	
	protected String[] populationBoxStrings = {"Population larger than", "Population smaller than or equal to"};
	protected String[] matchBoxStrings = {"Exact match", "Partial match"};
	
	public DBView(DBModel model) {
		super("Metropolis Viewer");

		setLayout(new BorderLayout());

		//top
		JPanel top = new JPanel();
		top.add(new JLabel("Metropolis:"));
		metropolisField = new JTextField(TEXT_FIELD_DEFAULT_SIZE);
		top.add(metropolisField);
		top.add(new JLabel("Continent:"));
		continentField = new JTextField(TEXT_FIELD_DEFAULT_SIZE);
		top.add(continentField);
		top.add(new JLabel("Population:"));
		populationField = new JTextField(TEXT_FIELD_DEFAULT_SIZE);
		top.add(populationField);
		
		
		//table
		dataModel = model;
		data = new JTable(dataModel);
		JScrollPane scrollpane = new JScrollPane(data);

		
		//left
		Box leftBox = Box.createVerticalBox();
		
		add = new JButton("Add");
		search = new JButton("Search");
		
		add.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		search.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		
		Box inside = Box.createVerticalBox();
		inside.setBorder(new TitledBorder("Search options"));
		
		populationBox = new JComboBox(populationBoxStrings);
		matchBox = new JComboBox(matchBoxStrings);
		
		populationBox.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		matchBox.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		
		matchBox.setMaximumSize(new Dimension(matchBox.getMaximumSize().width , matchBox.getMinimumSize().height));
        populationBox.setMaximumSize(new Dimension(matchBox.getMaximumSize().width , matchBox.getMinimumSize().height));
		
		inside.add(populationBox);
		inside.add(matchBox);
		
		leftBox.add(add);
		leftBox.add(search);
		leftBox.add(inside);
		
		add(top, BorderLayout.NORTH);
		add(scrollpane, BorderLayout.CENTER);
		add(leftBox, BorderLayout.EAST);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}
}
