package AccountManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;


public class ListOfAccounts extends Observable{
	private ArrayList<String> listOfPeople;
	//private ArrayList<Integer> listOfIDs;
	//private ArrayList<Observer> observers;
	
	public ListOfAccounts(){
		listOfPeople = new ArrayList<String>();
		//listOfIDs = new ArrayList<Integer>();
		//observers    = new ArrayList<Observer>();
	}
	
	public void add(Account a){
		listOfPeople.add(a.ID +" "+ a.name);
		//listOfIDs.add(a.ID);
		setChanged();
		notifyObservers(this.size());
	}
	public void remove(String a){
		listOfPeople.remove(a);
		setChanged();
		notifyObservers(this.size());
	}
	
	public boolean contains(String a) {
		return listOfPeople.contains(a);
	}
	
	public Iterator iterator(){
		return listOfPeople.iterator();
	}
	/*
	public void addObserver(Observer o){
		observers.add(o);
	}
	
	public void removeObserver(Observer o){
		observers.remove(o);
	}
	
	public void notifyObservers(){
		Iterator i = observers.iterator();
		while(i.hasNext()){
			Observer o = (Observer) i.next();
			o.update(this, o);
		}
	}
	*/
	public int size(){
		return listOfPeople.size();
	} 
}
