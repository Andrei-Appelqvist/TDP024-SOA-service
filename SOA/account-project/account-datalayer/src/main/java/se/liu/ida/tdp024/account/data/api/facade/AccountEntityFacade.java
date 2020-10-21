package se.liu.ida.tdp024.account.data.api.facade;
import java.util.ArrayList;
import java.util.List;
import se.liu.ida.tdp024.account.data.api.entity.Account;

public interface AccountEntityFacade {
    public boolean create(String accounttype, String personkey, String bankkey);
    public List<Account> findAccounts(String personkey);
    public boolean debitAccount(long id, Integer amount);
    public boolean creditAccount(long id, Integer amount);
    public Account getAccount(long id);
}
