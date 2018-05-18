package assign3;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;

public class SudokuFrame extends JFrame {

	private JTextArea puzzleArea;
	private JTextArea solutionArea;
	
	private JButton check;
	private JCheckBox autoCheck;
	
	public SudokuFrame() {
		super("Sudoku Solver"); 
		
		setLayout(new BorderLayout(4, 4));
		
		puzzleArea = new JTextArea(15, 20);
		puzzleArea.setBorder(new TitledBorder("Puzzle"));
		
		add(puzzleArea, BorderLayout.WEST);
		
		solutionArea = new JTextArea(15, 20);
		solutionArea.setBorder(new TitledBorder("Solution"));
		solutionArea.setEditable(false);
		
		add(solutionArea, BorderLayout.EAST);
		
		Box controls = Box.createHorizontalBox();
		check = new JButton("Check");
		autoCheck = new JCheckBox("Auto Check");
		autoCheck.setSelected(true);
		
		controls.add(check);
		controls.add(autoCheck);
		add(controls, BorderLayout.SOUTH);
		
		check.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				solveCurrentPuzzle();
			}
		});
		
		
		puzzleArea.getDocument().addDocumentListener(new DocumentListener(){

			@Override
			public void changedUpdate(DocumentEvent e) {
				if(autoCheck.isSelected())
					solveCurrentPuzzle();
				
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				if(autoCheck.isSelected())
					solveCurrentPuzzle();
				
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				if(autoCheck.isSelected())
					solveCurrentPuzzle();
				
			}
			
		});
		
		// Could do this:
		// setLocationByPlatform(true);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}

	private void solveCurrentPuzzle() {
		Sudoku sudoku;
		
		try {
			sudoku = new Sudoku(Sudoku.textToGrid(puzzleArea.getText()));
		} catch (Exception e) {
			solutionArea.setText("Parsing Problem");
			return;
		}
		
		int count = sudoku.solve();
		solutionArea.setText(sudoku.getSolutionText() + "\n" 
							+ "solutions:" + count + "\n" 
							+ "elapsed:" + sudoku.getElapsed() + "ms");
		
	}

	public static void main(String[] args) {
		// GUI Look And Feel
		// Do this incantation at the start of main() to tell Swing
		// to use the GUI LookAndFeel of the native platform. It's ok
		// to ignore the exception.
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) {
		}

		SudokuFrame frame = new SudokuFrame();
	}

}
