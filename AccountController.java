package AccountManager;

public class AccountController {
	
	public final static double Euro_Conv = 0.88;
	public final static double Yuan_Conv = 6.47;
	
	public static boolean withdraw(double amount, int index, String currency_type){
		if(amount <= AllAccounts.loadedAccounts[index].amount && amount >= 0){
			if(currency_type == "EURO")
				amount = amount/Euro_Conv;
			if(currency_type == "YUAN")
				amount = amount/Euro_Conv;
			AllAccounts.loadedAccounts[index].amount -= amount;
		}
		else
			return false;
		return true;
	}
	public static boolean deposit(double amount, int index, String currency_type){
		if(amount >= 0){
			if(currency_type == "EURO")
				amount = amount/0.88;
			if(currency_type == "YUAN")
				amount = amount/6.47;
			AllAccounts.loadedAccounts[index].amount += amount;
		}
		else
			return false;
		return true;
	}
	public static void startDepositAgent() {
		// TODO Auto-generated method stub
		
	}
	public static void startWithdrawAgent() {
		// TODO Auto-generated method stub
		
	}
	
}
