package se.liu.ida.tdp024.account.logic.api.facade;
import java.util.ArrayList;

public interface TransactionLogicFacade {
  public boolean addTransaction(String type, long id, Integer amount, boolean status);
  public String getTransactions(long id);
}
