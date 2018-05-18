import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

public class Bank {

	private static final int ACCOUNT_NUMBER = 20;
	private static final int ACCOUNT_BALANCE = 1000;

	private static ArrayList<Account> accs;
	private static LinkedBlockingQueue<Transaction> queue;

	private static final Transaction finalTransaction = new Transaction(-1, 0, 0);

	static CountDownLatch latch;

	public class Worker extends Thread {
		@Override
		public void run() {
			while (true) {
				try {
					Transaction curr = queue.take();

					if (curr.equals(finalTransaction))
						break;

					accs.get(curr.getDestination()).makeTransaction(curr.getAmount(), true);
					accs.get(curr.getSource()).makeTransaction(curr.getAmount(), false);

				} catch (InterruptedException Ignored) { }
			}
			latch.countDown();
		}
	}

	public Bank(int numWorkers) {
		accs = new ArrayList<Account>();
		for (int i = 0; i < ACCOUNT_NUMBER; i++)
			accs.add(new Account(i, ACCOUNT_BALANCE));

		queue = new LinkedBlockingQueue<Transaction>();

		for (int i = 0; i < numWorkers; i++)
			new Worker().start();
	}

	private void readFile(String fileName, int numWorkers) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			String line;
			while ((line = br.readLine()) != null) {
				int[] data = parseLine(line);
				queue.put(new Transaction(data[0], data[1], data[2]));
			}

			br.close();

			for (int i = 0; i < numWorkers; i++)
				queue.put(finalTransaction);

		} catch (Exception e) {
			System.out.println("Problem reading file");
			System.exit(0);
		}
	}

	private int[] parseLine(String line) {
		int[] res = new int[3];

		for (int i = 0; i < 3; i++)
			res[i] = Integer.parseInt(line.split(" ")[i]);

		return res;

	}

	public static void main(String[] args) {
		if (args.length < 2)
			System.out.println("Too few arguments");
		else
			try {
				latch = new CountDownLatch(Integer.parseInt(args[1]));

				Bank bank = new Bank(Integer.parseInt(args[1]));
				bank.readFile(args[0], Integer.parseInt(args[1]));

				try {
					latch.await();
				} catch (InterruptedException Ignored) { }

				for (Account acc : accs)
					System.out.println(acc);

			} catch (NumberFormatException e) {
				System.out.println("Can't parse to int");
			}
	}
}
