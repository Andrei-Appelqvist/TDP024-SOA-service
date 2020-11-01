package se.liu.ida.tdp024.account.data.test.facade;

import org.junit.After;
import org.junit.Test;
import org.junit.Assert;
import se.liu.ida.tdp024.account.data.api.facade.AccountEntityFacade;
import se.liu.ida.tdp024.account.data.api.entity.Account;
import se.liu.ida.tdp024.account.data.impl.db.entity.AccountDB;
import se.liu.ida.tdp024.account.data.impl.db.facade.AccountEntityFacadeDB;
import se.liu.ida.tdp024.account.data.impl.db.entity.TransactionDB;
import se.liu.ida.tdp024.account.data.impl.db.facade.TransactionEntityFacadeDB;
import se.liu.ida.tdp024.account.data.api.util.StorageFacade;
import se.liu.ida.tdp024.account.data.impl.db.util.StorageFacadeDB;
import java.util.List;
import se.liu.ida.tdp024.account.util.json.AccountJsonSerializer;
import se.liu.ida.tdp024.account.util.json.AccountJsonSerializerImpl;

public class AccountEntityFacadeTest {

    //---- Unit under test ----//
    //private TransactionEntityFacadeDB transFacadeDb = new TransactionEntityFacadeDB();
    private AccountEntityFacade accountEntityFacade = new AccountEntityFacadeDB();
    //private AccountEntityFacade accountEntityFacade = new AccountEntityFacadeDB(transFacadeDb);
    private StorageFacade storageFacade = new StorageFacadeDB();
    public String accounttype = "CHECK";
    public String personkey = "3";
    public String bankkey = "1";
    public long accountId;


    @After
    public void tearDown() {
       storageFacade.emptyStorage();
    }


    @Test
    public void testCreate() {
      boolean acc = accountEntityFacade.create(accounttype, personkey, bankkey);
      Assert.assertEquals(acc, true);
      boolean acc2 = accountEntityFacade.create("MONEYLAUNDERING", personkey, bankkey);
      Assert.assertEquals(acc2, false);
    }

    @Test
    public void testFind(){
      boolean acc = accountEntityFacade.create(accounttype, personkey, "2");
      Assert.assertEquals(acc, true);
      boolean acc2 = accountEntityFacade.create(accounttype, personkey, "3");
      Assert.assertEquals(acc, true);
      List<Account> existingAccounts = accountEntityFacade.findAccounts("3");
      Assert.assertEquals(existingAccounts.size(), 2);
      Assert.assertEquals(existingAccounts.get(0).getBankKey(), "2");
      Assert.assertEquals(existingAccounts.get(1).getBankKey(), "3");
      existingAccounts = accountEntityFacade.findAccounts("4");
      Assert.assertEquals(existingAccounts.size(), 0);
    }

    @Test
    public void testGet(){
      boolean acc = accountEntityFacade.create(accounttype, personkey, "5");
      Assert.assertEquals(acc, true);
      List<Account> existingAccounts = accountEntityFacade.findAccounts("3");
      long id = existingAccounts.get(0).getId();
      Account foundAcc = accountEntityFacade.getAccount(id);
      List<Account> ff = accountEntityFacade.findAccounts("3");
      Assert.assertEquals("5", foundAcc.getBankKey());

      Account fa = accountEntityFacade.getAccount(69);
      Assert.assertEquals(fa, null);
    }

    @Test
    public void testCredit(){
      boolean acc = accountEntityFacade.create(accounttype, personkey, bankkey);
      Assert.assertEquals(acc, true);
      List<Account> existingAccounts = accountEntityFacade.findAccounts("3");
      Assert.assertEquals(existingAccounts.size(), 1);
      long id = existingAccounts.get(0).getId();
      boolean credit = accountEntityFacade.creditAccount(id, 1337);
      Assert.assertEquals(credit, true);
      Account account = accountEntityFacade.getAccount(id);
      Assert.assertEquals((long)account.getHoldings(), 1337);

      boolean cr = accountEntityFacade.creditAccount(id, -1337);
      Assert.assertEquals(cr, false);

      cr = accountEntityFacade.creditAccount(420, 1337);
      Assert.assertEquals(cr, false);
    }

    @Test
    public void testDebit(){
      boolean acc = accountEntityFacade.create(accounttype, personkey, bankkey);
      Assert.assertEquals(acc, true);
      List<Account> existingAccounts = accountEntityFacade.findAccounts("3");
      Assert.assertEquals(existingAccounts.size(), 1);
      long id = existingAccounts.get(0).getId();
      boolean credit = accountEntityFacade.creditAccount(id, 1337);
      Assert.assertEquals(credit, true);
      Account account = accountEntityFacade.getAccount(id);
      Assert.assertEquals((long)account.getHoldings(), 1337);
      credit = accountEntityFacade.debitAccount(id, 917);
      Assert.assertEquals(credit, true);
      account = accountEntityFacade.getAccount(id);
      Assert.assertEquals((long)account.getHoldings(), 1337-917);
      boolean cr = accountEntityFacade.debitAccount(id, -1337);
      Assert.assertEquals(cr, false);

      cr = accountEntityFacade.debitAccount(id, 500);
      Assert.assertEquals(cr, false);

      boolean asd = accountEntityFacade.debitAccount(420, 20);
      Assert.assertEquals(cr, false);
    }


}
