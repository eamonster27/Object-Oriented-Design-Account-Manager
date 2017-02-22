package AccountManager;

import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;


public class AccountsCounter implements Observer{
	private ListOfAccounts alist;
	
	public AccountsCounter(ListOfAccounts loa){
		alist = loa;
		alist.addObserver(this);
	}

	public void update(Observable o, Object arg) {
		if(o == alist){
			System.out.println("List of Accounts has changed. ");
			System.out.println(alist.size()); // if you do not need the name, this will do
			int counter = 0;
			Iterator i = alist.iterator();
			while(i.hasNext()){
				String line = (String)i.next();
				counter++;
			}
			System.out.println("Total number of Accounts is: " + counter);
			System.out.println("Total number of Accounts is: " + arg);
		}
	}
}
