package se.liu.ida.tdp024.account.logic.api.facade;
import java.util.ArrayList;
import java.util.*;
import se.liu.ida.tdp024.account.data.api.entity.Account;


public interface AccountLogicFacade {
    public boolean register(String accounttype,String person,String bank);
    public List<Account> findPerson(String person);
    public boolean debitAccount(long id, Integer amount);
    public boolean creditAccount(long id, Integer amount);



}
