package se.liu.ida.tdp024.account.logic.test.facade;

import org.junit.After;
import org.junit.Test;
import se.liu.ida.tdp024.account.data.api.util.StorageFacade;
import se.liu.ida.tdp024.account.logic.api.facade.TransactionLogicFacade;
import se.liu.ida.tdp024.account.logic.impl.facade.TransactionLogicFacadeImpl;
import se.liu.ida.tdp024.account.logic.api.facade.AccountLogicFacade;
import se.liu.ida.tdp024.account.logic.impl.facade.AccountLogicFacadeImpl;
import org.junit.Assert;
import se.liu.ida.tdp024.account.data.api.entity.Account;
import se.liu.ida.tdp024.account.data.impl.db.entity.AccountDB;
import se.liu.ida.tdp024.account.data.api.entity.Transaction;
import se.liu.ida.tdp024.account.data.impl.db.facade.AccountEntityFacadeDB;
import se.liu.ida.tdp024.account.data.api.facade.TransactionEntityFacade;
import se.liu.ida.tdp024.account.data.impl.db.entity.TransactionDB;
import se.liu.ida.tdp024.account.data.impl.db.facade.TransactionEntityFacadeDB;
import se.liu.ida.tdp024.account.data.api.util.StorageFacade;
import se.liu.ida.tdp024.account.data.impl.db.util.StorageFacadeDB;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TransactionLogicFacadeTest {
    //--- Unit under test ---//
    public TransactionLogicFacade transactionLogicFacade = new TransactionLogicFacadeImpl(new TransactionEntityFacadeDB());
    public AccountLogicFacade accountLogicFacade = new AccountLogicFacadeImpl(new AccountEntityFacadeDB());
    public StorageFacade storageFacade = new StorageFacadeDB();
    public String person = "3";
    public String bank = "NORDEA";
    public String accounttype = "CHECK";
    public String type = "CREDIT";
    public Integer amount = 200;
    public boolean status = true;
    public long id = 1;


    @After
    public void tearDown() {
      if (storageFacade != null)
        storageFacade.emptyStorage();
    }

    @Test
    public void testAdd() {
      boolean reg = accountLogicFacade.register(person, bank, accounttype);
      Assert.assertEquals(reg, true);
      boolean add = transactionLogicFacade.addTransaction(type, id, amount, status);
      Assert.assertEquals(add, true);
      add = transactionLogicFacade.addTransaction(type, id, amount, false);
      Assert.assertEquals(add, true);
      add = transactionLogicFacade.addTransaction("HEIST", id, amount, status);
      Assert.assertEquals(add, false);
      add = transactionLogicFacade.addTransaction(type, 30, amount, status);
      Assert.assertEquals(add, false);
      add = transactionLogicFacade.addTransaction(type, id, -300, status);
      Assert.assertEquals(add, false);
    }

    @Test
    public void testGet() {
      boolean reg = accountLogicFacade.register(person, bank, accounttype);
      Assert.assertEquals(reg, true);
      boolean add = transactionLogicFacade.addTransaction(type, id, amount, status);
      Assert.assertEquals(add, true);
      add = transactionLogicFacade.addTransaction("DEBIT", id, amount, status);
      Assert.assertEquals(add, true);
      add = transactionLogicFacade.addTransaction("DEBIT", id, 350, status);
      Assert.assertEquals(add, true);
      add = transactionLogicFacade.addTransaction(type, id, 322, false);
      Assert.assertEquals(add, true);

      List<Transaction> transactions = transactionLogicFacade.getTransactions(id);
      Assert.assertEquals(transactions.get(0).getType(), "CREDIT");
      Assert.assertEquals(transactions.get(0).getStatus(), "OK");
      Assert.assertEquals((long)transactions.get(0).getAmount(), 200);

      Assert.assertEquals(transactions.get(1).getType(), "DEBIT");
      Assert.assertEquals(transactions.get(1).getStatus(), "OK");
      Assert.assertEquals((long)transactions.get(1).getAmount(), 200);

      Assert.assertEquals(transactions.get(2).getType(), "DEBIT");
      Assert.assertEquals(transactions.get(2).getStatus(), "OK");
      Assert.assertEquals((long)transactions.get(2).getAmount(), 350);

      Assert.assertEquals(transactions.get(3).getType(), "CREDIT");
      Assert.assertEquals(transactions.get(3).getStatus(), "FAILED");
      Assert.assertEquals((long)transactions.get(3).getAmount(), 322);
    }
}
