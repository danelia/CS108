package assign3;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DBController implements ActionListener{
	
	private DBView view;
	private DBModel model;
	
	public DBController(DBModel model, DBView view){
		
		this.view = view;
		this.model = model;
		
		view.add.addActionListener(this);
		view.search.addActionListener(this);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(view.add)){
			model.add(view.metropolisField.getText(), view.continentField.getText(), view.populationField.getText());
		}
		
		if(e.getSource().equals(view.search)){
			boolean first = view.populationBox.getSelectedItem().equals("Population larger than");
			boolean second = view.matchBox.getSelectedItem().equals("Exact match");
			String one = view.metropolisField.getText();
			String two = view.continentField.getText();
			String three = view.populationField.getText();
			model.search(one, two , three, !first, second);
		}
		
	}

}
