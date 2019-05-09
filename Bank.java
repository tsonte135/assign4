import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

public class Bank {
	
	private BlockingQueue<Transaction> transQueue;
	private List<Account> accounts;
	private CountDownLatch latch; 
	private static final int accountNum = 20;
	private static final int initBalance = 1000;
	
//	constructor. Makes Accounts list and initializes parameters, also starts worker threads.
	public Bank(int workerNum) {
		transQueue = new ArrayBlockingQueue<Transaction>(workerNum);
		makeInitAccounts();
		latch = new CountDownLatch(workerNum);
		
		for(int i = 0; i < workerNum; i++) {
			(new Worker()).start();
		}
	}
	
//	worker threads class.
	private class Worker extends Thread {
		
		public Worker() {
			
		}
	
//		if the queue is full, worker threads take the transactions. 
//		if it is empty, they wait for bank class to put transactions in it.
		@Override
		public void run() {
			try {
				
				while(true) {
					Transaction currTransaction = transQueue.take();
					if(currTransaction.getSender() == -1) {
						break;
					}
					makeTransaction(currTransaction.getSender(), currTransaction.getReceiver(),
							currTransaction.getAmount());
				}

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			latch.countDown();
		}
		
//		changes the account balances and increases transaction number for both of counterparts.
		private void makeTransaction(int sender, int receiver, int amount) {
			 accounts.get(sender).makeTransaction(-amount);
			 accounts.get(receiver).makeTransaction(amount);
		}
		
	}
	
//	bank waits for workers to make queue empty.
	public void bankwait() {
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
//	makes list of accounts.
	private void makeInitAccounts() {
		accounts = new ArrayList<Account>();
		for(int i = 0; i < accountNum; i++) {
			Account newAccount = new Account(i, initBalance, 0);
			accounts.add(newAccount);
		}
	}
	
//	at the end of program, prints information about all accounts.
	private void printAccounts() {
		for(int i = 0; i < accounts.size(); i++) {
			System.out.println(accounts.get(i).toString());
		}
	}
	
//	reads the file of transactions. Puts the transactions into the queue (at last, puts null transactions).
//	if the queue is full, bank waits for workers to take all transactions from it.
//	At the end main method prints the information about Accounts.
	public static void main(String[] arg) {
		 int workerNum = Integer.parseInt(arg[1]);
		 Bank bank = new Bank(workerNum);
		 
		 try{
			 BufferedReader reader = new BufferedReader(new FileReader(arg[0]));
			 String currentLine = reader.readLine();
			 
			 while(currentLine != null){
				 String[] transArray = currentLine.split(" ");
				 Transaction trans = new Transaction(Integer.parseInt((transArray[0])), 
						 Integer.parseInt(transArray[1]), Integer.parseInt(transArray[2]));
				 bank.transQueue.put(trans);
				 currentLine = reader.readLine();
			 }
			 
			 Transaction nullTrans = new Transaction(-1, 0, 0);
			 for(int i = 0; i < workerNum; i++) {
				 bank.transQueue.put(nullTrans);
			 }
			 
		     reader.close();
		 }catch (Exception ignored){
			 
		 }
		 bank.bankwait();
			
		 bank.printAccounts();
	}
}
