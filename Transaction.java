
public class Transaction {
	private int from; //Account number of money sender
	private int to; //Account number of money receiver 
	private int amount; //Transfer amount
	
//	Constructor, initializes class parameters.
	public Transaction(int sender, int receiver, int transAmount) {
		from = sender;
		to = receiver;
		amount = transAmount;
	}
	
//	returns the id number of sender account.
	public int getSender() {
		return from;
	}
	
	
//	returns the id number of receiver account.
	public int getReceiver() {
		return to;
	}
	
//	returns the transaction amount.
	public int getAmount() {
		return amount;
	}
	
	
}
