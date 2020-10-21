package se.liu.ida.tdp024.account.logic.api.facade;
import java.util.ArrayList;


public interface AccountLogicFacade {
    public boolean register(String accounttype,String person,String bank);
    public String findPerson(String person);
    public boolean debitAccount(long id, Integer amount);
    public boolean creditAccount(long id, Integer amount);



}
