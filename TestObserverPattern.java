package AccountManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class TestObserverPattern {

	public static void main(String[] args) throws IOException {
		ListOfAccounts alist= new ListOfAccounts();
		
		File file = new File("C:\\textfile.HW4.txt");
		BufferedReader in = new BufferedReader(new FileReader(file));
		
		String data[] = null;
		String line;
		AllAccounts.loadedAccounts = new Account[AllAccounts.total_accounts];
		
		boolean id_digit_only = false;
		boolean unique_id = true;
		boolean amnt_digit_only = false;
		boolean amnt_min = false;
		
		try {
			while(!(line = in.readLine()).equals("endAccounts")){
				data = line.split("\t");

				//New Account Class Object
				Account a = new Account();
				
				//NAME: Letters only(Names can be repeated)
				a.name = data[0];
				
				//ID: Digits only(IDs are unique)
				//Digits only
				if(Integer.valueOf(data[1]) instanceof Integer) {
					a.ID = Integer.valueOf(data[1]);
					id_digit_only = true;
					//Unique IDs only
					for(int i = 0; i < AllAccounts.total_accounts; ++i) {
						if(a.ID == AllAccounts.loadedAccounts[i].ID){
							unique_id = false;
							System.out.println("Skipping duplicate ID: " + a.ID);
						}
					}
				}
				
				//AMOUNT: Digits and Decimal only >= 0.0
				//Digits only
				if(Double.valueOf(data[2]) instanceof Double || Integer.valueOf(data[2]) instanceof Integer) {
					a.amount = Double.valueOf(data[2]);
					amnt_digit_only = true;
					//Greater than 0.0
					if(a.amount >= 0.0){
						amnt_min = true;
					}	
				}
				//If all value conditions are met, add account
				if(id_digit_only && unique_id && amnt_digit_only && amnt_min) {
					//Initializing temp Account array
					Account[] temp = new Account[AllAccounts.total_accounts];
					//Copying current contents of AllAccounts.loadedAccounts to temp
					for(int i = 0; i < AllAccounts.total_accounts; ++i)
						temp[i] = AllAccounts.loadedAccounts[i];
					
					//Making room for new Account in AllAccounts.loadedAccounts
					AllAccounts.loadedAccounts = new Account[AllAccounts.total_accounts + 1];
					//Copying old contents into newly allocated AllAccount.loadedAccounts
					for(int i = 0; i < AllAccounts.total_accounts; ++i)
						AllAccounts.loadedAccounts[i] = temp[i];
					//Adding new account
					AllAccounts.loadedAccounts[AllAccounts.total_accounts] = new Account();
					AllAccounts.loadedAccounts[AllAccounts.total_accounts].name = a.name;
					AllAccounts.loadedAccounts[AllAccounts.total_accounts].ID = a.ID;
					AllAccounts.loadedAccounts[AllAccounts.total_accounts].amount = a.amount;
					//Resizing AllAccounts.total_accounts;
					AllAccounts.total_accounts++;
					alist.add(a);
				}
			}	
		}catch(NumberFormatException ex){
			System.out.println("Amounts or IDs with non-digits found.");
			ex.printStackTrace();
		}
		
		AccountFrame af = new AccountFrame(alist);
		af.setVisible(true);
		
		AccountsCounter a_counter = new AccountsCounter(alist);
		AccountsNamePreview a_preview = new AccountsNamePreview(alist);
	}
}
