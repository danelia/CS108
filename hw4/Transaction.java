
public class Transaction {

	private int from;
	private int to;
	private int amount;

	public Transaction(int from, int to, int amount) {
		this.from = from;
		this.to = to;
		this.amount = amount;
	}

	public int getSource() {
		return from;
	}

	public int getDestination() {
		return to;
	}

	public int getAmount() {
		return amount;
	}
}
