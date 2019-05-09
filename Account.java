
public class Account {
	private int id; //Account id
	private int currentBalance; //Account Balance
	private int transNum; //Number of completed transactions on account
	
//	constructor, initializes class parameters
	public Account(int newId, int newBalance, int transNumber) {
		id = newId;
		currentBalance = newBalance;
		transNum = transNumber;
		
	}
	
//	returns the id number of account.
	public synchronized int getId() {
		return id;
	}
	
	
//	returns the currentBalance of account.
	public synchronized int getCurrentBalance() {
		return currentBalance;
	}
	
//	returns the number of completed transactions.
	public synchronized int getTransNum() {
		return transNum;
	}
	
//	changes account balance by transaction amount.
	public synchronized void makeTransaction(int transactionAmount) {
		currentBalance += transactionAmount;
		transNum += 1;
	}
	
//	returns information about account in string.
	@Override
	public String toString() {
		String res = "";
		res += "acct:" + id + " bal:" + currentBalance + " trans:" + transNum;
		return res;
	}
}
