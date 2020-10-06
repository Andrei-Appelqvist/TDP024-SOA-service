package se.liu.ida.tdp024.account.data.impl.db.facade;
import java.util.ArrayList;
import se.liu.ida.tdp024.account.data.api.facade.AccountEntityFacade;

public class AccountEntityFacadeDB implements AccountEntityFacade {

  @Override
  public String test(){
    return "Omaaahghaaaad it works!";
  }

  @Override
  public boolean create(String accounttype, String personkey, String bankkey){
    System.out.printf("%s, %s, %s",accounttype, personkey, bankkey);
    return true;
  }

  @Override
  public ArrayList findAccounts(String personkey){
    System.out.printf("%s", personkey);
    ArrayList accounts = new ArrayList();
    accounts.add(personkey);
    return accounts;
  }

  @Override
  public boolean debitAccount(String id, Integer amount){
    System.out.printf("%s, %d", id, amount);
    return true;
  }

  @Override
  public boolean creditAccount(String id, Integer amount){
    System.out.printf("%s, %d", id, amount);
    return true;
  }

  @Override
  public ArrayList getTransactions(String id){
    System.out.printf("%s", id);
    ArrayList transactions = new ArrayList();
    transactions.add(id);
    return transactions;
  }
  
}
