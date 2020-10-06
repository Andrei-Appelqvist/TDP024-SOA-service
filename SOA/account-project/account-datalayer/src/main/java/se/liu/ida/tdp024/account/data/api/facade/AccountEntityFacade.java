package se.liu.ida.tdp024.account.data.api.facade;
import java.util.ArrayList;

public interface AccountEntityFacade {
    public String test();
    public boolean create(String accounttype, String personkey, String bankkey);
    public ArrayList findAccounts(String personkey);
    public boolean debitAccount(String id, Integer amount);
    public boolean creditAccount(String id, Integer amount);
    public ArrayList getTransactions(String id);
}
