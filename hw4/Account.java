
public class Account {

	private int id;
	private int balance;
	private int numOfTransaction;

	public Account(int id, int balance) {
		this.id = id;
		this.balance = balance;
		this.numOfTransaction = 0;
	}

	public synchronized void makeTransaction(int balanceChange, boolean sign) {
		this.numOfTransaction++;
		this.balance += balanceChange * (sign ? 1 : -1);
	}

	@Override
	public synchronized String toString() {
		return "id: " + id + ", balance: " + balance + ", number of transactions: " + numOfTransaction;
	}

}
