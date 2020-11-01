package se.liu.ida.tdp024.account.logic.api.facade;
import java.util.ArrayList;
import java.util.*;
import se.liu.ida.tdp024.account.data.api.entity.Transaction;
import se.liu.ida.tdp024.account.data.api.entity.Account;

public interface TransactionLogicFacade {
  public boolean addTransaction(String type, Integer amount, boolean status, Account acc);
  public List<Transaction> getTransactions(long id);
}
