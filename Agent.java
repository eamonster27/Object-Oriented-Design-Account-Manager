package AccountManager;

import java.util.Observable;

public class Agent extends Observable{
	public int accountID;
	public int ID;
	public double amount;
	public double operations;
	public String type;
	public String state = "Stopped";
	public double amount_transferred = 0;
	public double operations_completed = 0;
	
	public void stateChange(String new_state){
		state = new_state;
		setChanged();
		notifyObservers(this.state);
	}
	
	public void amountChange(double amnt){
		amount_transferred += amnt;
		setChanged();
		notifyObservers(this.amount_transferred);
	}
	
	public void operationChange(double operation){
		operations_completed += operation;
		setChanged();
		notifyObservers(this.operations_completed);
	}
}
