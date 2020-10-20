package se.liu.ida.tdp024.account.data.api.facade;
import java.util.ArrayList;
import java.util.List;
import se.liu.ida.tdp024.account.data.api.entity.Transaction;


public interface TransactionEntityFacade {
  public boolean addTransaction(String type, long id, Integer amount, boolean status);
  public List<Transaction> findTransactions(long id);
}
