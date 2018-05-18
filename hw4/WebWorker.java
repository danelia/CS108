import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Semaphore;

import javax.swing.*;

public class WebWorker extends Thread {

	private String urlString;
	private Semaphore sem;
	private WebFrame frame;
	private int row;

	public WebWorker(String urlString, Semaphore sem, WebFrame frame, int row) {
		this.urlString = urlString;
		this.sem = sem;
		this.frame = frame;
		this.row = row;
	}
	
	@Override
	public void run(){
		try {
			sem.acquire();
			frame.changeView("Interrupted", row, false, true);
			download();
			frame.changeView("Interrupted", row, false, false);
			sem.release();
		} catch (InterruptedException e) {
			frame.changeView("Interrupted", row, true, true);
		}
	}

	private void download() {
		InputStream input = null;
		StringBuilder contents = null;
		try {
			URL url = new URL(urlString);
			URLConnection connection = url.openConnection();

			// Set connect() to throw an IOException
			// if connection does not succeed in this many msecs.
			connection.setConnectTimeout(5000);

			connection.connect();
			input = connection.getInputStream();

			BufferedReader reader = new BufferedReader(new InputStreamReader(input));

			char[] array = new char[1000];
			int len;
			contents = new StringBuilder(1000);
			while ((len = reader.read(array, 0, array.length)) > 0) {
				contents.append(array, 0, len);
				Thread.sleep(100);
			}

			Date now = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			final String status = sdf.format(now) + " " + contents.length() + "bytes";
			frame.changeView(status, row, true, true);
		}
		// Otherwise control jumps to a catch...
		catch (MalformedURLException ignored) {
			frame.changeView("err", row, true, true);
		} catch (InterruptedException exception) {
			frame.changeView("Interrupted", row, true, true);
		} catch (IOException ignored) {
			frame.changeView("err", row, true, true);
		}
		// "finally" clause, to close the input stream
		// in any case
		finally {
			try {
				if (input != null)
					input.close();
			} catch (IOException ignored) {
			}
		}

	}

}
