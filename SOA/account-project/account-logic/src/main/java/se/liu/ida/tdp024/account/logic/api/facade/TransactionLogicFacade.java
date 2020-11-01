package se.liu.ida.tdp024.account.logic.api.facade;
import java.util.ArrayList;
import java.util.*;
import se.liu.ida.tdp024.account.data.api.entity.Transaction;

public interface TransactionLogicFacade {
  public boolean addTransaction(String type, long id, Integer amount, boolean status);
  public List<Transaction> getTransactions(long id);
}
