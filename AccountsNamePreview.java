package AccountManager;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
public class AccountsNamePreview implements Observer{
	private ListOfAccounts alist;
	
	public AccountsNamePreview(ListOfAccounts loa){
		alist = loa;
		alist.addObserver(this);
	}

	public void update(Observable o, Object arg) {
		if(o == alist){
			System.out.println("Accounts: ");
			Iterator i = alist.iterator();
			while(i.hasNext()){
				String line = (String)i.next();
				System.out.println("\t" + line);
			}
		}
	}
}
