import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class JCount extends JPanel {

	private static final int TEXTFIELF_SIZE = 10;
	private static final int NUMBER_OF_COUNTS = 4;

	private JTextField textField;
	private JLabel label;
	private JButton start;
	private JButton stop;

	private Worker thread;

	public JCount() {
		super();

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		textField = new JTextField("100000000", TEXTFIELF_SIZE);
		label = new JLabel("0");
		start = new JButton("Start");
		stop = new JButton("Stop");

		add(textField);
		add(label);
		add(start);
		add(stop);
		add(Box.createRigidArea(new Dimension(0, 40)));

		start.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (thread != null)
					thread.interrupt();

				thread = new Worker();
				thread.start();
			}
		});

		stop.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (thread != null) {
					thread.interrupt();
					thread = null;
				}
			}
		});
	}

	private class Worker extends Thread {
		@Override
		public void run() {
			for (int i = 0; i <= Integer.parseInt(textField.getText()); i++) {
				if (isInterrupted())
					break;

				if (i % 1000 == 0) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						break;
					}
					
					final int count = i;
					
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							label.setText("" + count);
						}
					});	
				}
			}
		}
	}

	private static void createAndShowGUI() {
		JFrame frame = new JFrame("Count");

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		frame.setResizable(false);

		for (int i = 0; i < NUMBER_OF_COUNTS; i++)
			frame.add(new JCount());

		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});

	}
}
