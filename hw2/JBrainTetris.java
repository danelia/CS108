package tetris;

import java.awt.*;

import javax.swing.*;
import java.util.*;

public class JBrainTetris extends JTetris{
	
	private JCheckBox brainMode;
	private JCheckBox animateFalling;
	private JPanel little;
	private JSlider adversary;
	private int currCount;
	private Brain.Move best;
	private DefaultBrain brain;
	private Random rnd;
	private JPanel panel;
	private JLabel adversaryText;

	
	public JBrainTetris(int pixels) {
		super(pixels);
	
		currCount = super.count;
		brain = new DefaultBrain();
		rnd = new Random();
		
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public JComponent createControlPanel() {
		panel = (JPanel) super.createControlPanel();
		
		panel.add(new JLabel("Brain:")); 
		brainMode = new JCheckBox("Brain active"); 
		panel.add(brainMode);
		
		animateFalling = new JCheckBox("Animate Falling");
		animateFalling.setSelected(true);
		panel.add(animateFalling);
		
		little = new JPanel(); 
		little.add(new JLabel("Adversary:")); 
		adversary = new JSlider(0, 100, 0); // min, max, current 
		adversary.setPreferredSize(new Dimension(100,15)); 
		little.add(adversary);
		panel.add(little);
		
		adversaryText = new JLabel("Ok");
		panel.add(adversaryText);
		
		return panel;
	}
	
	@Override
	public void tick(int verb){
		if(!brainMode.isSelected())
			super.tick(verb);
		else{
			if(super.currentPiece != null){
				super.board.undo();
			}
			
			if(currCount != super.count){
				currCount = super.count;
				best = brain.bestMove(super.board, super.currentPiece, super.board.getHeight(), best);
			}
			
			if(!best.piece.equals(super.currentPiece))
				super.tick(ROTATE);
			
			if(best.x > super.currentX)
				super.tick(RIGHT);
			else if(best.x < super.currentX)
				super.tick(LEFT);
			else if(best.piece.equals(super.currentPiece)
					&& !animateFalling.isSelected()){
				super.tick(DROP);
				super.tick(DOWN);
			}
			
			super.tick(DOWN);
			
		}
	}
	
	@Override
	public Piece pickNextPiece() {
		int randInt = rnd.nextInt(adversary.getMaximum() - 1) + 1;// so it will be in 1 - 99 range not in 0-100
		Piece ret = super.pickNextPiece();
		
		if(adversary.getValue() == 0 || 
				randInt >= adversary.getValue())
			adversaryText.setText("Ok");
		else{
			adversaryText.setText("*Ok*");
			double worst = -1;
			ret = brain.bestMove(super.board, super.pieces[0], super.board.getHeight(), null).piece;
			for(int i = 0; i < super.pieces.length; i++){
				Brain.Move worstMove = new Brain.Move();
				brain.bestMove(super.board, super.pieces[i], super.board.getHeight(), worstMove);
				if(worstMove.score > worst){
					worst = worstMove.score;
					ret = worstMove.piece;
				}
			}
		}
		
		return ret;
		
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) { }
		
		JBrainTetris tetris = new JBrainTetris(16);
		JFrame frame = JBrainTetris.createFrame(tetris);
		frame.setVisible(true);
	}

}
