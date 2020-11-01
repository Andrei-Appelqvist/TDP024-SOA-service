package se.liu.ida.tdp024.account.data.api.facade;
import java.util.ArrayList;
import java.util.List;
import se.liu.ida.tdp024.account.data.api.entity.Transaction;
import se.liu.ida.tdp024.account.data.api.entity.Account;


public interface TransactionEntityFacade {
  public boolean addTransaction(String type, Integer amount, boolean status, Account acc);
  public List<Transaction> findTransactions(long id);
}
