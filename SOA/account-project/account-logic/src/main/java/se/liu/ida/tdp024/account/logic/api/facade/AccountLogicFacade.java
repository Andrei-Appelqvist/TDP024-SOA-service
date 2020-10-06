package se.liu.ida.tdp024.account.logic.api.facade;
import java.util.ArrayList;


public interface AccountLogicFacade {

    public String test();

    public boolean register(String accounttype,String person,String bank);
    public ArrayList<String> findPerson(String person);
    public boolean debitAccount(String id, Integer amount);
    public boolean creditAccount(String id, Integer amount);
    public ArrayList<String> getTransactions(String id);



}
