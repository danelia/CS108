import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class WebFrame extends JFrame {

	private static final int TEXTFIELD_SIZE = 6;
	private static final String links = "links.txt";
	private static final String links2 = "links2.txt";
	
	private int completedThreads;
	private int runningThreads;

	private DefaultTableModel model;
	private JTable table;
	private JPanel panel;

	private JButton singleThreadButton;
	private JButton concurrentButton;

	private JTextField threadCount;
	private JLabel runningLabel;
	private JLabel completedLabel;
	private JLabel elapsedLabel;

	private JProgressBar progressBar;

	private JButton stopButton;

	private Launcher launcher;
	
	private long elapsedStart;
	private long elapsedEnd;

	public WebFrame() {
		super("WebLoader");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		panel = new JPanel();

		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		model = new DefaultTableModel(new String[] { "url", "status" }, 0);
		fillTable(links);
		table = new JTable(model);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		JScrollPane scrollpane = new JScrollPane(table);
		scrollpane.setPreferredSize(new Dimension(600, 300));

		singleThreadButton = new JButton("Single Thread Fetch");
		concurrentButton = new JButton("Concurrent Fetch");

		threadCount = new JTextField("4", TEXTFIELD_SIZE);
		threadCount.setMaximumSize(threadCount.getPreferredSize());
		runningLabel = new JLabel("Running: 0");
		completedLabel = new JLabel("Completed: 0");
		elapsedLabel = new JLabel("Elapsed: ");

		progressBar = new JProgressBar(0, model.getRowCount());
		progressBar.setValue(0);
		progressBar.setStringPainted(true);

		stopButton = new JButton("Stop");
		stopButton.setEnabled(false);

		panel.add(scrollpane);
		panel.add(singleThreadButton);
		panel.add(concurrentButton);
		panel.add(threadCount);
		panel.add(runningLabel);
		panel.add(completedLabel);
		panel.add(elapsedLabel);
		panel.add(progressBar);
		panel.add(stopButton);

		singleThreadButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				changeButtons(false);
				resetView();
				
				elapsedStart = System.currentTimeMillis();
				completedThreads = 0;
				runningThreads = 0;
				
				launcher = new Launcher(1);
				launcher.start();
			}
		});

		concurrentButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				changeButtons(false);
				resetView();
				
				elapsedStart = System.currentTimeMillis();
				completedThreads = 0;
				runningThreads = 0;
				
				launcher = new Launcher(Integer.parseInt(threadCount.getText()));
				launcher.start();
			}
		});

		stopButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				changeButtons(true);
				
				elapsedEnd = System.currentTimeMillis();
				launcher.interrupt();
			}
		});

		add(panel);

		pack();
		setVisible(true);
	}
	
	public synchronized void changeView(final String data, final int row, boolean change, boolean increase){
		if(change){
			completedThreads++;
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					completedLabel.setText("Completed: " + completedThreads);
					progressBar.setValue(completedThreads);
					model.setValueAt(data, row, 1);
				}
			});
		}else{
			runningThreads += increase ? 1 : -1;
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					runningLabel.setText("Running: " + runningThreads);
				}
			});
		}
	}
	
	private void resetView(){
		for (int i = 0; i < model.getRowCount(); i++)
			model.setValueAt("", i, 1);
		
		progressBar.setValue(0);
		completedLabel.setText("Completed: 0");
		runningLabel.setText("Running: 0");
		elapsedLabel.setText("Elapsed: ");
		
	}

	private class Launcher extends Thread {
		private Semaphore sem;

		public Launcher(int numThreads) {
			sem = new Semaphore(numThreads);
		}

		@Override
		public void run() {
			ArrayList<Thread> workers = new ArrayList<Thread>();
			for (int i = 0; i < model.getRowCount(); i++) {
				WebWorker worker = new WebWorker((String) model.getValueAt(i, 0), sem, WebFrame.this, i);
				worker.start();
				
				workers.add(worker);
			}
			
			for(Thread worker : workers){
				try {
					worker.join();
				} catch (InterruptedException e) {
					
					for(Thread w : workers)
						w.interrupt();
					
					break;
					
				}
			}
			elapsedEnd = System.currentTimeMillis();
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					final double time = (elapsedEnd - elapsedStart) / 1000.0;
					elapsedLabel.setText("Elapsed: " + time);
				}
			});
			changeButtons(true);
			
		}
	}

	private void changeButtons(boolean b){
		stopButton.setEnabled(!b);
		singleThreadButton.setEnabled(b);
		concurrentButton.setEnabled(b);
	}
	
	private void fillTable(String link) {
		try {
			BufferedReader rd = new BufferedReader(new FileReader(link));

			String line;
			while ((line = rd.readLine()) != null) {
				Object[] data = { line, "" };
				model.addRow(data);
			}

			rd.close();

		} catch (IOException e) {
			System.out.println("Unable to read file");
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new WebFrame();
			}
		});
	}
}
