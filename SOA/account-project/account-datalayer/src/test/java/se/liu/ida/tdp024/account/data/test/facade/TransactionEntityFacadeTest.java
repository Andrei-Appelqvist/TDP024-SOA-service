package se.liu.ida.tdp024.account.data.test.facade;

import org.junit.After;
import org.junit.Test;
import org.junit.Assert;
import se.liu.ida.tdp024.account.data.api.facade.AccountEntityFacade;
import se.liu.ida.tdp024.account.data.api.entity.Account;
import se.liu.ida.tdp024.account.data.impl.db.entity.AccountDB;
import se.liu.ida.tdp024.account.data.impl.db.facade.AccountEntityFacadeDB;
import se.liu.ida.tdp024.account.data.api.facade.TransactionEntityFacade;
import se.liu.ida.tdp024.account.data.api.entity.Transaction;
import se.liu.ida.tdp024.account.data.impl.db.entity.TransactionDB;
import se.liu.ida.tdp024.account.data.impl.db.facade.TransactionEntityFacadeDB;
import se.liu.ida.tdp024.account.data.api.util.StorageFacade;
import se.liu.ida.tdp024.account.data.impl.db.util.StorageFacadeDB;
import java.util.List;
import se.liu.ida.tdp024.account.util.json.AccountJsonSerializer;
import se.liu.ida.tdp024.account.util.json.AccountJsonSerializerImpl;


public class TransactionEntityFacadeTest {

  //---- Unit under test ----//
  private TransactionEntityFacade transactionEntityFacade = new TransactionEntityFacadeDB();
  private AccountEntityFacade accountEntityFacade = new AccountEntityFacadeDB();
  private StorageFacade storageFacade = new StorageFacadeDB();
  public String accounttype = "CHECK";
  public String personkey = "3";
  public String bankkey = "1";
  public String type = "CREDIT";
  public Integer amount = 200;
  public boolean status = true;
  public long id = makeAcc();

  public long makeAcc(){
    boolean acc = accountEntityFacade.create(accounttype, personkey, bankkey);
    List<Account> existingAccounts = accountEntityFacade.findAccounts("3");
    return existingAccounts.get(0).getId();
  }

  @After
  public void tearDown() {
     storageFacade.emptyStorage();
  }

  @Test
  public void testAdd(){
    boolean transaction = transactionEntityFacade.addTransaction(type, id, amount, status);
    Assert.assertEquals(transaction, true);
    transaction = transactionEntityFacade.addTransaction("DEBIT", id, amount, status);
    Assert.assertEquals(transaction, true);
    transaction = transactionEntityFacade.addTransaction("FREE REAL ESTATE", id, amount, status);
    Assert.assertEquals(transaction, false);
    transaction = transactionEntityFacade.addTransaction(type, 50, amount, status);
    Assert.assertEquals(transaction, false);
    transaction = transactionEntityFacade.addTransaction(type, id, -200, status);
    Assert.assertEquals(transaction, false);
  }

  @Test
  public void testFind(){
    boolean transaction = transactionEntityFacade.addTransaction(type, id, amount, status);
    Assert.assertEquals(transaction, true);

    transaction = transactionEntityFacade.addTransaction("DEBIT", id, amount, status);
    Assert.assertEquals(transaction, true);

    transaction = transactionEntityFacade.addTransaction(type, id, 1337, status);
    Assert.assertEquals(transaction, true);

    transaction = transactionEntityFacade.addTransaction(type, id, 420, false);
    Assert.assertEquals(transaction, true);

    List<Transaction> trans = transactionEntityFacade.findTransactions(id);
    Assert.assertEquals(trans.size(), 4);

    Assert.assertEquals(trans.get(0).getAmount(), amount);
    Assert.assertEquals(trans.get(0).getStatus(), "OK");
    Assert.assertEquals(trans.get(0).getType(), "CREDIT");
    Assert.assertEquals(trans.get(0).getAccount().getId(), 1);

    Assert.assertEquals(trans.get(1).getAmount(), amount);
    Assert.assertEquals(trans.get(1).getStatus(), "OK");
    Assert.assertEquals(trans.get(1).getType(), "DEBIT");
    Assert.assertEquals(trans.get(1).getAccount().getId(), 1);

    Assert.assertEquals((long)trans.get(2).getAmount(), 1337);
    Assert.assertEquals(trans.get(2).getStatus(), "OK");
    Assert.assertEquals(trans.get(2).getType(), "CREDIT");
    Assert.assertEquals(trans.get(2).getAccount().getId(), 1);

    Assert.assertEquals((long)trans.get(3).getAmount(), 420);
    Assert.assertEquals(trans.get(3).getStatus(), "FAILED");
    Assert.assertEquals(trans.get(3).getType(), "CREDIT");
    Assert.assertEquals(trans.get(3).getAccount().getId(), 1);
  }

}
