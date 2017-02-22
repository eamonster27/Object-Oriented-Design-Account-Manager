package AccountManager;

import java.awt.BorderLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.Timer;



public class AccountFrame extends JFrame implements Observer, ActionListener{
	private static final long serialVersionUID = 1704446198984670126L;
	
	private ListOfAccounts alist = new ListOfAccounts();
	private ArrayList<String> strList = new ArrayList<String>();
	private JPanel content_pane;
	private JList jlist_accounts;
	
	private JPanel agent_panel;
	
	//Added
	//-----------------------------------------------------
	
	private final String save = "Save";
	private final String exit = "Exit";
	
	private final String edit_USD = "Edit in USD";
	private final String edit_EURO = "Edit in Euros";
	private final String edit_YUAN = "Edit in Yuan";
	
	private final String withdraw = "Withdraw";
	private final String deposit = "Deposit";
	private final String dismiss = "Dismiss";
	
	private final String deposit_agent = "Create Deposit Agent";
	private final String withdraw_agent = "Create Withdraw Agent";
	
	private final String start_agent = "Start Agent";
	
	private final String stop_agent = "Stop Agent";
	private final String dismiss_agent = "Dismiss Agent";
	
	private JTextField agentIDTextField;
	private JTextField amountTextField;
	private JTextField operationsTextField;
	
	private JButton b_stop_agent;
	private JButton b_dismiss_agent;
	
	private Agent current_agent = new Agent();
	private Account current_account = new Account();
	
	private JTextField stateTextField;
	private JTextField operationsCompletedTextField;
	private JTextField amountTransferredTextField;
	
	//Added
	//-----------------------------------------------------
	
	private JLabel title_label;

	public AccountFrame(ListOfAccounts loa){
		alist = loa;
		alist.addObserver(this);
		initView();
	}
	
	private void initView(){	
		setBounds(100, 100, 900, 500);
		content_pane = new JPanel();
		content_pane.setBorder(new EmptyBorder(5, 5, 5, 5));
		content_pane.setLayout(new BorderLayout(0, 0));
		setContentPane(content_pane);
		
		title_label = new JLabel("Total number of Accounts: " + String.valueOf(alist.size()));
		content_pane.add(title_label, BorderLayout.NORTH);
		
		setAccountsText();
		jlist_accounts = new JList(strList.toArray());
		jlist_accounts.setSize(300,300);
		
		JScrollPane jsp = new JScrollPane(jlist_accounts);
		jsp.setSize(300, 300);
		
		content_pane.add(jsp, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		content_pane.add(panel, BorderLayout.SOUTH);

		//Added
		//-----------------------------------------------------
		
		JButton b_save = new JButton(save);
		b_save.addActionListener(this);
		panel.add(b_save);
		
		JButton b_exit = new JButton(exit);
		b_exit.addActionListener(this);
		panel.add(b_exit);
		
		JButton b_edit_USD = new JButton(edit_USD);
		b_edit_USD.addActionListener(this);
		panel.add(b_edit_USD);
		
		JButton b_edit_EURO = new JButton(edit_EURO);
		b_edit_EURO.addActionListener(this);
		panel.add(b_edit_EURO);
		
		JButton b_edit_YUAN = new JButton(edit_YUAN);
		b_edit_YUAN.addActionListener(this);
		panel.add(b_edit_YUAN);
		
		JButton b_deposit_agent = new JButton(deposit_agent);
		b_deposit_agent.addActionListener(this);
		panel.add(b_deposit_agent);
		
		JButton b_withdraw_agent = new JButton(withdraw_agent);
		b_withdraw_agent.addActionListener(this);
		panel.add(b_withdraw_agent);
		
		//Added
		//-----------------------------------------------------
		
		
		this.setTitle("Accounts");
     	this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
	}
	
	private void setAccountsText() {
		String ordered[] = new String[alist.size()];
		Iterator itr = alist.iterator();
		
		for(int i = 0; i < alist.size(); ++i)
			if(itr.hasNext())
				ordered[i] = (String)itr.next();
		
		String data[] = null;
		for(int min = 0; min < alist.size(); ++min) {
			for(int j = min + 1; j < alist.size(); ++j){
				data = ordered[min].split(" ");
				int min_value = Integer.valueOf(data[0]);
				
				data = ordered[j].split(" ");
				if(min_value > Integer.valueOf(data[0])) {	
					String temp = ordered[min];
					ordered[min] = ordered[j];
					ordered[j] = temp;
				}
			}
		}

		for(int i = 0; i < alist.size(); ++i)
			strList.add(ordered[i]);
		/*
		Iterator i = alist.iterator();
		while(i.hasNext())
			strList.add((String)i.next());
		*/
	}

	public void update(Observable o, Object arg) {
		if(alist == o){
			strList.clear();
			Iterator i = alist.iterator();
			while(i.hasNext())
				strList.add((String)i.next());
			jlist_accounts.setListData(strList.toArray());
			title_label.setText("Total number of Accounts: " + String.valueOf(alist.size()));
		}
		stateTextField = new JTextField(current_agent.state, 15);
	}

	public void actionPerformed(ActionEvent ae) {
		String command = ae.getActionCommand();
		if(command.equals(save)){
			try {
				File file = new File("C:\\textfile.HW4.txt");
				if(!file.exists())
					file.createNewFile();
			
				FileWriter fw = new FileWriter(file.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				
				for(int i = 0; i < AllAccounts.total_accounts; ++i) {
					bw.write(AllAccounts.loadedAccounts[i].name+"\t");
					bw.write(AllAccounts.loadedAccounts[i].ID+"\t");
					bw.write(AllAccounts.loadedAccounts[i].amount+"\t");
					bw.newLine();
				}
				bw.write("endAccounts\n");
				bw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		else if(command.equals(exit)){
			//If current state of accounts has been modified since last save. 
			try {
				File file = new File("C:\\textfile.HW4.txt");
				if(!file.exists())
					file.createNewFile();
			
				FileWriter fw = new FileWriter(file.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				
				for(int i = 0; i < AllAccounts.total_accounts; ++i) {
					bw.write(AllAccounts.loadedAccounts[i].name+"\t");
					bw.write(AllAccounts.loadedAccounts[i].ID+"\t");
					bw.write(AllAccounts.loadedAccounts[i].amount+"\t");
					bw.newLine();
				}
				bw.write("endAccounts\n");
				bw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			//EXIT PROGRAM
			System.exit(0);
			
		}
		else if(command.equals(edit_USD)){
			int sindex = jlist_accounts.getSelectedIndex();
			if(sindex >= 0){
				String a = strList.get(sindex);
				popupEditDialog(a, "USD");
			}
		}
		else if(command.equals(edit_EURO)){
			int sindex = jlist_accounts.getSelectedIndex();
			if(sindex >= 0){
				String a = strList.get(sindex);
				popupEditDialog(a, "EURO");
			}
		}
		else if(command.equals(edit_YUAN)){
			int sindex = jlist_accounts.getSelectedIndex();
			if(sindex >= 0){
				String a = strList.get(sindex);
				popupEditDialog(a, "YUAN");
			}
		}
		else if(command.equals(deposit_agent)){
			
			int sindex = jlist_accounts.getSelectedIndex();
			if(sindex >= 0){
				String a = strList.get(sindex);
				
				//Decipher account string to load current_account
				String data[] = null;
				data = a.split(" ");
				double amount;
				int index = 999999999;
				for(int i = 0; i < AllAccounts.total_accounts; ++i)
					if(AllAccounts.loadedAccounts[i].ID == Integer.valueOf(data[0]))
						index = i;
				current_account = AllAccounts.loadedAccounts[index];
				current_agent.type = "deposit";
				current_agent.accountID = current_account.ID;
				popupStartAgentDialog();
			}
		}
		else if(command.equals(withdraw_agent)){
			
			int sindex = jlist_accounts.getSelectedIndex();
			if(sindex >= 0){
				String a = strList.get(sindex);
				
				//Decipher account string to load current_account
				String data[] = null;
				data = a.split(" ");
				double amount;
				int index = 999999999;
				for(int i = 0; i < AllAccounts.total_accounts; ++i)
					if(AllAccounts.loadedAccounts[i].ID == Integer.valueOf(data[0]))
						index = i;
				current_account = AllAccounts.loadedAccounts[index];                      
				current_agent.type = "withdraw";
				current_agent.accountID = current_account.ID;
				popupStartAgentDialog();
			}
		}
		else if(command.equals(start_agent)){
			double op_input = 0;
			double amnt_input = 0;
			int id_input = 0;
			boolean unique_id = true;
			String test_input = operationsTextField.getText();
			try {
				//Operations digit only check
				if(Double.valueOf(test_input) instanceof Double || Integer.valueOf(test_input) instanceof Integer){
					op_input = Double.valueOf(operationsTextField.getText());
					test_input = amountTextField.getText();
					//Amount digit only check
					if(Double.valueOf(test_input) instanceof Double || Integer.valueOf(test_input) instanceof Integer){
						amnt_input = Double.valueOf(amountTextField.getText());
						test_input = agentIDTextField.getText();
						//Agent ID digit only check
						if(Integer.valueOf(test_input) instanceof Integer){
							id_input = Integer.valueOf(agentIDTextField.getText());
							//Unique Agent ID check
							for(int i = 0; i < AllAccounts.total_agents; ++i){
								if(Integer.valueOf(id_input) == AllAccounts.loadedAgents[i].ID){
									JOptionPane.showMessageDialog(null, "Agent ID not unique.");
									unique_id = false;
								}
							}
							if(unique_id){
								current_agent.ID = id_input;
								current_agent.amount = amnt_input;
								current_agent.operations = op_input;
								current_agent.amount_transferred = 0;
								current_agent.operations_completed = 0;
								current_agent.state = "Running";
								popupAgentDialog();
							}
						}		
					}	
				}
			}catch(NumberFormatException ex){
				JOptionPane.showMessageDialog(null, "Check Operations or Amount field(numbers and digits only)");
			}
		}
		else if(command.equals(stop_agent)){
			b_dismiss_agent.setEnabled(true);
			current_agent.stateChange("Stopped");
			stateTextField = new JTextField(current_agent.state, 15);
			amountTransferredTextField = new JTextField(String.valueOf(current_agent.amount_transferred), 15);
			operationsCompletedTextField = new JTextField(String.valueOf(current_agent.operations_completed), 15);
			
			agent_panel.add(stateTextField, BorderLayout.LINE_END);
			agent_panel.add(amountTransferredTextField, BorderLayout.SOUTH);
			//agent_panel.add(operationsCompletedTextField, BorderLayout.SOUTH);
			stateTextField.setEditable(false);
			amountTransferredTextField.setEditable(false);
			//operationsCompletedTextField.setEditable(false);
			agent_panel.updateUI();
		}
		else if(command.equals(dismiss_agent)){
			Window w = SwingUtilities.getWindowAncestor(b_dismiss_agent);
			if(w != null)
				w.setVisible(false);
		}
		
	}
	
	private void popupEditDialog(String account, String currency_type) {
		String data[] = null;
		data = account.split(" ");
		double amount;
		int index = 999999999;
		for(int i = 0; i < AllAccounts.total_accounts; ++i)
			if(AllAccounts.loadedAccounts[i].ID == Integer.valueOf(data[0]))
				index = i;
		
		String title = data[1] + " " + data[2] + " " + data[0];
		amount = AllAccounts.loadedAccounts[index].amount;
		
		if(currency_type == "EURO")
			amount = amount * AccountController.Euro_Conv;
		if(currency_type == "YUAN")
			amount = amount * AccountController.Yuan_Conv;
		
		Object[] options = { "Withdraw", "Deposit", "Dismiss" };
		
		JPanel panel = new JPanel();
		panel.setVisible(true);
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		panel.setLayout(new BorderLayout(5, 5));
		
		panel.add(new JLabel("Operations in " + currency_type), BorderLayout.NORTH);
		panel.add(new JLabel("Available Funds: " + amount), BorderLayout.CENTER);
		panel.add(new JLabel("Enter Amount in " + currency_type), BorderLayout.SOUTH);
		JTextField textField = new JTextField("0.0", 10);
		panel.add(textField, BorderLayout.SOUTH);

		int option = JOptionPane.showOptionDialog(
				null, 
				panel, 
				title,
				JOptionPane.DEFAULT_OPTION,
				JOptionPane.PLAIN_MESSAGE,
				null, 
				options, 
				null);
		String test_input = textField.getText();
		double input = 0;
		try {
			if(Double.valueOf(test_input) instanceof Double || Integer.valueOf(test_input) instanceof Integer){
				input = Double.valueOf(textField.getText());
				//Withdraw
				if(option == 0){
					if(AccountController.withdraw(input, index, currency_type))
						JOptionPane.showMessageDialog(null, input + " withdrawn.");
					else{
						double difference = input - AllAccounts.loadedAccounts[index].amount;
						JOptionPane.showMessageDialog(null, "Insufficient funds: amount to " +
								"withdraw is " + difference + " greater than available funds: " + AllAccounts.loadedAccounts[index].amount);
					}
					popupEditDialog(account, currency_type);
				}
				//Deposit
				else if(option == 1){
					if(AccountController.deposit(input, index, currency_type))
						JOptionPane.showMessageDialog(null, input + " deposited.");
					else{
						JOptionPane.showMessageDialog(null, "Deposit Failed!");
					}
					popupEditDialog(account, currency_type);
				}
				//Dismiss
				else{
					panel.setVisible(false);
				}
			}
		}catch(NumberFormatException ex){
			JOptionPane.showMessageDialog(null, "Check amount format(numbers and digits only)");
			popupEditDialog(account, currency_type);
		}
	}	
	
	private void popupStartAgentDialog(){
		
		String title = null;
		if(current_agent.type == "deposit")
			title = "Start deposit agent for account: " + current_account.ID;
		else if(current_agent.type == "withdraw")
			title = "Start withdraw agent for account: " + current_account.ID;
		
		final JButton b_start_agent = new JButton(start_agent);
		b_start_agent.addActionListener(this);
		
		Object[] options = { b_start_agent };
		
		JPanel panel = new JPanel();
		panel.setVisible(true);
		panel.setBorder(new EmptyBorder(5, 5, 50, 50));
		panel.setLayout(new BorderLayout(15, 15));
		
		panel.add(new JLabel("Agent ID:                                             " +
				"Amount in $:                                      " +
				"Operations per second: "), BorderLayout.NORTH);
		agentIDTextField = new JTextField(15);
		panel.add(agentIDTextField, BorderLayout.LINE_START);
		
		amountTextField = new JTextField(15);
		panel.add(amountTextField);
		
		operationsTextField = new JTextField("0.0", 15);
		panel.add(operationsTextField, BorderLayout.LINE_END);
		
		int option = JOptionPane.showOptionDialog(
				null, 
				panel, 
				title,
				JOptionPane.DEFAULT_OPTION,
				JOptionPane.PLAIN_MESSAGE,
				null, 
				options, 
				null);	
	}
	
	private void popupAgentDialog(){
		current_agent.addObserver(this);
		
		//Append new agent to loadedAgents
		Agent temp[] = new Agent[AllAccounts.total_agents];
		
		for(int i = 0; i < AllAccounts.total_agents; ++i)
			temp[i] = AllAccounts.loadedAgents[i];
		
		AllAccounts.loadedAgents = new Agent[AllAccounts.total_agents + 1];
		
		for(int i = 0; i < AllAccounts.total_agents; ++i)
			AllAccounts.loadedAgents[i] = temp[i];
		
		AllAccounts.loadedAgents[AllAccounts.total_agents] = current_agent;
		AllAccounts.total_agents += 1;
		
		String title = null;
		if(current_agent.type == "deposit"){
			title = "Deposit agent " + current_agent.ID + " for account " + current_account.ID;
			int index = 0;
			for(int i = 0; i < AllAccounts.total_accounts; ++i)
				if(AllAccounts.loadedAccounts[i].ID == current_agent.accountID)
					index = i;
			AccountController.deposit(current_agent.amount, index, "USD");
			current_agent.amountChange(current_agent.amount);
			current_agent.operationChange(current_agent.operations);
		}
		else if(current_agent.type == "withdraw"){
			title = "Withdraw agent " + current_agent.ID + " for account " + current_account.ID;
			int index = 0;
			for(int i = 0; i < AllAccounts.total_accounts; ++i)
				if(AllAccounts.loadedAccounts[i].ID == current_agent.accountID)
					index = i;
			if(AccountController.withdraw(current_agent.amount, index, "USD")){
				current_agent.amountChange(current_agent.amount);
				current_agent.operationChange(current_agent.operations);
			}
			else{
				current_agent.stateChange("Blocked");
				stateTextField = new JTextField(current_agent.state, 15);
				agent_panel.add(stateTextField, BorderLayout.LINE_END);
				stateTextField.setEditable(false);
				agent_panel.updateUI();
			}
		}
		
		b_stop_agent = new JButton(stop_agent);
		b_stop_agent.addActionListener(this);
		
		b_dismiss_agent = new JButton(dismiss_agent);
		b_dismiss_agent.addActionListener(this);
		b_dismiss_agent.setEnabled(false);
		
		agent_panel = new JPanel();
		agent_panel.setBorder(new EmptyBorder(5, 5, 50, 400));
		agent_panel.setLayout(new BorderLayout(15, 15));
		
		agent_panel.add(new JLabel("Amount in $:                                      " +
				"Operations per second:                 " +
				"State:                  "), BorderLayout.NORTH);
		JTextField amountTextField = new JTextField(String.valueOf(current_agent.amount), 15);
		amountTextField.setEditable(false);
		agent_panel.add(amountTextField, BorderLayout.LINE_START);
		
		JTextField operationsTextField = new JTextField(String.valueOf(current_agent.operations), 15);
		operationsTextField.setEditable(false);
		agent_panel.add(operationsTextField);
		
		stateTextField = new JTextField(current_agent.state, 15);
		stateTextField.setEditable(false);
		agent_panel.add(stateTextField, BorderLayout.LINE_END);
		
		//agent_panel.add(new JLabel("Amount in $ transferred:                 " +
				//"Operations completed:                 "), BorderLayout.AFTER_LAST_LINE);
		
		//Count not get text fields to append to end of window without overwriting.
		
		operationsCompletedTextField = new JTextField(String.valueOf(current_agent.operations_completed), 15);
		operationsCompletedTextField.setEditable(false);
		agent_panel.add(operationsCompletedTextField, BorderLayout.SOUTH);
		
		amountTransferredTextField = new JTextField(String.valueOf(current_agent.amount_transferred), 15);
		amountTransferredTextField.setEditable(false);
		//agent_panel.add(amountTransferredTextField, BorderLayout.SOUTH);
		
		JButton[] options = {b_stop_agent, b_dismiss_agent};
		
		Object option = JOptionPane.showOptionDialog(
				null, 
				agent_panel, 
				title,
				JOptionPane.DEFAULT_OPTION,
				JOptionPane.PLAIN_MESSAGE,
				null, 
				options,
				null);
	}
}




















