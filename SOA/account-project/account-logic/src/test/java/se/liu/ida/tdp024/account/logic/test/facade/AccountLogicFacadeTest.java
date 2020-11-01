package se.liu.ida.tdp024.account.logic.test.facade;

import org.junit.After;
import org.junit.Test;
import se.liu.ida.tdp024.account.data.api.util.StorageFacade;
import se.liu.ida.tdp024.account.logic.api.facade.AccountLogicFacade;
import se.liu.ida.tdp024.account.logic.impl.facade.AccountLogicFacadeImpl;
// import org.junit.After;
// import org.junit.Test;
import org.junit.Assert;
import se.liu.ida.tdp024.account.data.api.facade.AccountEntityFacade;
import se.liu.ida.tdp024.account.data.api.entity.Account;
import se.liu.ida.tdp024.account.data.impl.db.entity.AccountDB;
import se.liu.ida.tdp024.account.data.impl.db.facade.AccountEntityFacadeDB;
// import se.liu.ida.tdp024.account.data.api.facade.TransactionEntityFacade;
// import se.liu.ida.tdp024.account.data.api.entity.Transaction;
// import se.liu.ida.tdp024.account.data.impl.db.entity.TransactionDB;
// import se.liu.ida.tdp024.account.data.impl.db.facade.TransactionEntityFacadeDB;
import se.liu.ida.tdp024.account.data.api.util.StorageFacade;
import se.liu.ida.tdp024.account.data.impl.db.util.StorageFacadeDB;
import com.google.gson.Gson;
import java.util.List;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import se.liu.ida.tdp024.account.data.api.facade.TransactionEntityFacade;
import se.liu.ida.tdp024.account.data.impl.db.facade.TransactionEntityFacadeDB;
// import se.liu.ida.tdp024.account.util.json.AccountJsonSerializer;
// import se.liu.ida.tdp024.account.util.json.AccountJsonSerializerImpl;


public class AccountLogicFacadeTest {


    //--- Unit under test ---//
    private final TransactionEntityFacade transFacadeDb = new TransactionEntityFacadeDB();
    public AccountLogicFacade accountLogicFacade = new AccountLogicFacadeImpl(new AccountEntityFacadeDB(transFacadeDb));
    public StorageFacade storageFacade = new StorageFacadeDB();
    public String person = "3";
    public String bank = "NORDEA";
    public String accounttype = "CHECK";

    @After
    public void tearDown() {
      if (storageFacade != null)
        storageFacade.emptyStorage();
    }



    @Test
    public void testRegister() {
      boolean reg = accountLogicFacade.register(person, bank, accounttype);
      Assert.assertEquals(reg, true);
      reg = accountLogicFacade.register("20", bank, accounttype);
      Assert.assertEquals(reg, false);
      reg = accountLogicFacade.register(person, "THATGUYONTHECORNER", accounttype);
      Assert.assertEquals(reg, false);
      reg = accountLogicFacade.register(person, bank, "SECRET STASH");
      Assert.assertEquals(reg, false);
      reg = accountLogicFacade.register(person, bank, "SAVINGS");
      Assert.assertEquals(reg, true);
    }

    @Test
    public void testFind(){
      //String correct = "[{\"id\":1,\"personKey\":\"3\",\"accountType\":\"CHECK\",\"bankKey\":\"4\",\"holdings\":0},{\"id\":2,\"personKey\":\"3\",\"accountType\":\"CHECK\",\"bankKey\":\"1\",\"holdings\":0},{\"id\":3,\"personKey\":\"3\",\"accountType\":\"SAVINGS\",\"bankKey\":\"2\",\"holdings\":0}]";
      boolean reg = accountLogicFacade.register(person, bank, accounttype);
      Assert.assertEquals(reg, true);
      reg = accountLogicFacade.register(person, "SWEDBANK", accounttype);
      Assert.assertEquals(reg, true);
      reg = accountLogicFacade.register(person, "IKANOBANKEN", "SAVINGS");
      Assert.assertEquals(reg, true);

      List<Account> accounts = accountLogicFacade.findPerson("3");
      Assert.assertEquals(accounts.get(0).getAccountType(), "CHECK");
      Assert.assertEquals(accounts.get(0).getPersonKey(), "3");
      Assert.assertEquals(accounts.get(0).getBankKey(), "4");
      Assert.assertEquals((long)accounts.get(0).getHoldings(), 0);

      Assert.assertEquals(accounts.get(1).getAccountType(), "CHECK");
      Assert.assertEquals(accounts.get(1).getPersonKey(), "3");
      Assert.assertEquals(accounts.get(1).getBankKey(), "1");
      Assert.assertEquals((long)accounts.get(1).getHoldings(), 0);

      Assert.assertEquals(accounts.get(2).getAccountType(), "SAVINGS");
      Assert.assertEquals(accounts.get(2).getPersonKey(), "3");
      Assert.assertEquals(accounts.get(2).getBankKey(), "2");
      Assert.assertEquals((long)accounts.get(2).getHoldings(), 0);

    }

    @Test
    public void testCredit(){
      boolean reg = accountLogicFacade.register(person, bank, accounttype);
      Assert.assertEquals(reg, true);

      boolean deb = accountLogicFacade.creditAccount(1, 1337);
      Assert.assertEquals(deb, true);
      deb = accountLogicFacade.creditAccount(20, 1337);
      Assert.assertEquals(deb, false);
      deb = accountLogicFacade.creditAccount(20, -1337);
      Assert.assertEquals(deb, false);

      List<Account> accounts = accountLogicFacade.findPerson("3");
      Assert.assertEquals((long)accounts.get(0).getHoldings(), 1337);
    }

    @Test
    public void testDebit(){
      boolean reg = accountLogicFacade.register(person, bank, accounttype);
      Assert.assertEquals(reg, true);
      boolean deb = accountLogicFacade.creditAccount(1, 1337);
      Assert.assertEquals(deb, true);

      deb = accountLogicFacade.debitAccount(1, 917);
      Assert.assertEquals(deb, true);
      List<Account> accounts = accountLogicFacade.findPerson("3");
      Assert.assertEquals((long)accounts.get(0).getHoldings(), 420);
      // String holds = accounts.split("\"holdings\":")[1].split("}")[0];
      // Assert.assertEquals("420", holds);

      deb = accountLogicFacade.debitAccount(1, 500);
      Assert.assertEquals(deb, false);

      deb = accountLogicFacade.debitAccount(1, -500);
      Assert.assertEquals(deb, false);
    }


}
