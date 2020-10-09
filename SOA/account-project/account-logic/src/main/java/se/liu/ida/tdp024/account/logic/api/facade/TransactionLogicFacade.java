package se.liu.ida.tdp024.account.logic.api.facade;
import java.util.ArrayList;

public interface TransactionLogicFacade {
  public void addTransaction(String type, long id, Integer amount, boolean status);
  public ArrayList<String> getTransactions(long id);
}
