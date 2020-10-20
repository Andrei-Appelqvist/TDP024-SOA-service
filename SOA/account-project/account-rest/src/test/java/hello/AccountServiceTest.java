package test.hello;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import se.liu.ida.tdp024.account.util.http.HTTPHelper;
import se.liu.ida.tdp024.account.util.http.HTTPHelperImpl;
import se.liu.ida.tdp024.account.data.api.util.StorageFacade;
import se.liu.ida.tdp024.account.data.impl.db.util.StorageFacadeDB;
import hello.AccountService;
import org.springframework.http.ResponseEntity;

public class AccountServiceTest {
    //--- Unit under test ---//
    public StorageFacade storageFacade = new StorageFacadeDB();
    private static final HTTPHelper httpHelper = new HTTPHelperImpl();
    public AccountService accS = new AccountService();
    String ENDPOINT = "http://localhost:8080/account-rest/";
    public long id = 1;
    public String first = "[{\"id\":1,\"personKey\":\"3\",\"accountType\":\"CHECK\",\"bankKey\":\"4\",\"holdings\":0}]";
    public String afterCredit = "[{\"id\":1,\"personKey\":\"3\",\"accountType\":\"CHECK\",\"bankKey\":\"4\",\"holdings\":200}]";
    public String afterDebit = "[{\"id\":1,\"personKey\":\"3\",\"accountType\":\"CHECK\",\"bankKey\":\"4\",\"holdings\":69}]";

    @Before
    public void tearDown() {
      if (storageFacade != null)
        storageFacade.emptyStorage();
    }

    @Test
    public void test1(){
      ////////////TEST_CREATE///////////
      String res = accS.create("3", "SWEDBANK", "CHECK");
      Assert.assertEquals("OK", res);
      res = accS.create( null, "SWEDBANK", "CHECK");
      Assert.assertEquals("FAILED", res);
      res = accS.create("3", null, "CHECK");
      Assert.assertEquals("FAILED", res);
      res = accS.create("3", "SWEDBANK", null);
      Assert.assertEquals("FAILED", res);

      res = accS.create("300", "NORDEA", "CHECK");
      Assert.assertEquals("FAILED", res);
      res = accS.create("3", "SCAM-AB", "CHECK");
      Assert.assertEquals("FAILED", res);
      res = accS.create("3", "NORDEA", "MONEYLAUNDERING");
      Assert.assertEquals("FAILED", res);
    }
    //
    @Test
    public void test2(){
      ////////////TEST_FIND///////////
      System.out.println("TWO");
      String res = accS.create("3", "NORDEA", "CHECK");
      Assert.assertEquals(res, "OK");
      res = accS.findPerson("3");
      Assert.assertEquals(res, first);
      res = accS.findPerson("27");
      Assert.assertEquals("[]", res);
    }


    @Test
    public void test3(){
      ////////////TEST_CREDIT///////////
      String res = accS.create("3", "NORDEA", "CHECK");
      Assert.assertEquals(res, "OK");
      res = accS.findPerson("3");
      Assert.assertEquals(res, first);
      res = accS.credit(1, 200);
      Assert.assertEquals(res, "OK");
      res = accS.findPerson("3");
      Assert.assertEquals(res, afterCredit);

      res = accS.credit(1, -22);
      Assert.assertEquals(res, "FAILED");

      res = accS.credit(1000, 200);
      Assert.assertEquals(res, "FAILED");
    }


    @Test
    public void test4(){
      ////////////TEST_DEBIT///////////
      String res = accS.create("3", "NORDEA", "CHECK");
      Assert.assertEquals(res, "OK");
      res = accS.findPerson("3");
      Assert.assertEquals(res, first);
      res = accS.credit(1, 200);
      Assert.assertEquals(res, "OK");
      res = accS.findPerson("3");
      Assert.assertEquals(res, afterCredit);

      res = accS.debit(1, 131);
      Assert.assertEquals(res, "OK");
      res = accS.findPerson("3");
      Assert.assertEquals(res, afterDebit);

      res = accS.debit(1, -22);
      Assert.assertEquals(res, "FAILED");

      res = accS.debit(1000, 200);
      Assert.assertEquals(res, "FAILED");

      res = accS.debit(1, 70);
      Assert.assertEquals(res, "FAILED");
    }


    @Test
    public void test5(){
      String res = accS.create("3", "NORDEA", "CHECK");
      Assert.assertEquals(res, "OK");
      res = accS.credit(1, 200);
      Assert.assertEquals(res, "OK");
      res = accS.debit(1, 131);
      Assert.assertEquals(res, "OK");
      res = accS.debit(1, 70);
      Assert.assertEquals(res, "FAILED");

      String correct = "[{\"id\":2,\"type\":\"CREDIT\",\"amount\":200,\"created\":\"2020-10-20 18:17:57\",\"status\":\"OK\",\"account\":{\"id\":1,\"personKey\":\"3\",\"accountType\":\"CHECK\",\"bankKey\":\"4\",\"holdings\":69}},{\"id\":3,\"type\":\"DEBIT\",\"amount\":131,\"created\":\"2020-10-20 18:17:57\",\"status\":\"OK\",\"account\":{\"id\":1,\"personKey\":\"3\",\"accountType\":\"CHECK\",\"bankKey\":\"4\",\"holdings\":69}},{\"id\":4,\"type\":\"DEBIT\",\"amount\":70,\"created\":\"2020-10-20 18:17:57\",\"status\":\"FAILED\",\"account\":{\"id\":1,\"personKey\":\"3\",\"accountType\":\"CHECK\",\"bankKey\":\"4\",\"holdings\":69}}]";

      String pattern = "[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1]) (0[0-9]|1[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])";
      res = accS.transactions(1);
      Assert.assertEquals(correct.replaceAll(pattern, "time"), res.replaceAll(pattern, "time"));

      res = accS.transactions(42);
      Assert.assertEquals(res, "[]");

    }

}
