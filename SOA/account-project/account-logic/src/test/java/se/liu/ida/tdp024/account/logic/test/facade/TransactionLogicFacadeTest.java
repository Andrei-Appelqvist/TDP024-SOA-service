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
      SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      Date date = new Date(System.currentTimeMillis());
      String datetime = formatter.format(date);
      //Kommer faila om man kallar på den inom några ms från att minut ändras
      //Sjukt låga odds men ändå. Bara att köra testet igen.

      String correct = "[{\"id\":2,\"type\":\"CREDIT\",\"amount\":200,\"created\":\""+datetime+"\",\"status\":\"OK\",\"account\":{\"id\":1,\"personKey\":\"3\",\"accountType\":\"CHECK\",\"bankKey\":\"4\",\"holdings\":0}},{\"id\":3,\"type\":\"DEBIT\",\"amount\":200,\"created\":\""+datetime+"\",\"status\":\"OK\",\"account\":{\"id\":1,\"personKey\":\"3\",\"accountType\":\"CHECK\",\"bankKey\":\"4\",\"holdings\":0}},{\"id\":4,\"type\":\"DEBIT\",\"amount\":350,\"created\":\""+datetime+"\",\"status\":\"OK\",\"account\":{\"id\":1,\"personKey\":\"3\",\"accountType\":\"CHECK\",\"bankKey\":\"4\",\"holdings\":0}},{\"id\":5,\"type\":\"CREDIT\",\"amount\":322,\"created\":\""+datetime+"\",\"status\":\"FAILED\",\"account\":{\"id\":1,\"personKey\":\"3\",\"accountType\":\"CHECK\",\"bankKey\":\"4\",\"holdings\":0}}]";
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

      String transactions = transactionLogicFacade.getTransactions(id);
      //Assert.assertEquals(correct, transactions);
    }
}
