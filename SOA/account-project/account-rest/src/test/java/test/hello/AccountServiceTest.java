package test.hello;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import se.liu.ida.tdp024.account.util.http.HTTPHelper;
import se.liu.ida.tdp024.account.util.http.HTTPHelperImpl;
import se.liu.ida.tdp024.account.data.api.util.StorageFacade;
import se.liu.ida.tdp024.account.data.impl.db.util.StorageFacadeDB;

public class AccountServiceTest {
    //--- Unit under test ---//
    public StorageFacade storageFacade = new StorageFacadeDB();
    private static final HTTPHelper httpHelper = new HTTPHelperImpl();
    String ENDPOINT = "http://localhost:8080/account-rest/";
    public long id = 1;

    @Before
    public void tearDown() {
      if (storageFacade != null)
        storageFacade.emptyStorage();
    }

    @Test
    public void test1(){
      ////////////TEST_CREATE///////////
      System.out.println("ONE");
      String res = httpHelper.get(ENDPOINT + "account/create/", "person", "3", "bank", "NORDEA", "accounttype", "CHECK");
      Assert.assertEquals("OK", res);
      res = httpHelper.get(ENDPOINT + "account/create/", "bank", "NORDEA", "accounttype", "CHECK");
      Assert.assertEquals("FAILED", res);
      res = httpHelper.get(ENDPOINT + "account/create/", "person", "3", "accounttype", "CHECK");
      Assert.assertEquals("FAILED", res);
      res = httpHelper.get(ENDPOINT + "account/create/", "person", "3", "bank", "NORDEA");
      Assert.assertEquals("FAILED", res);

      res = httpHelper.get(ENDPOINT + "account/create/", "person", "300", "bank", "NORDEA", "accounttype", "CHECK");
      Assert.assertEquals("FAILED", res);
      res = httpHelper.get(ENDPOINT + "account/create/", "person", "3", "bank", "SCAM-AB", "accounttype", "CHECK");
      Assert.assertEquals("FAILED", res);
      res = httpHelper.get(ENDPOINT + "account/create/", "person", "3", "bank", "NORDEA", "accounttype", "MONEYLAUNDERING");
      Assert.assertEquals("FAILED", res);
    }

    //
    @Test
    public void test2(){
      ////////////TEST_FIND///////////
      System.out.println("TWO");
      String correct  = "[{\"id\":1,\"personKey\":\"3\",\"accountType\":\"CHECK\",\"bankKey\":\"4\",\"holdings\":0}]";
      String res = httpHelper.get(ENDPOINT + "account/find/person", "person", "3");
      //Assert.assertEquals(correct, res);
      res = httpHelper.get(ENDPOINT + "account/find/person", "person", "27");
      Assert.assertEquals("[]", res);
    }


    @Test
    public void test3(){
      ////////////TEST_CREDIT///////////
      System.out.println("THREE");
      String correct  = "[{\"id\":1,\"personKey\":\"3\",\"accountType\":\"CHECK\",\"bankKey\":\"4\",\"holdings\":0}]";
      String after  = "[{\"id\":1,\"personKey\":\"3\",\"accountType\":\"CHECK\",\"bankKey\":\"4\",\"holdings\":200}]";

      String res = httpHelper.get(ENDPOINT + "account/find/person", "person", "3");
      //Assert.assertEquals(correct, res);
      res = httpHelper.get(ENDPOINT + "account/credit/", "id", "1", "amount", "200");
      res = httpHelper.get(ENDPOINT + "account/find/person", "person", "3");
      //Assert.assertEquals(after, res);

      res = httpHelper.get(ENDPOINT + "account/credit/", "id", "1", "amount", "-2");
      Assert.assertEquals(res, "FAILED");
      res = httpHelper.get(ENDPOINT + "account/credit/", "id", "2000", "amount", "2");
      Assert.assertEquals(res, "FAILED");
    }


    @Test
    public void test4(){
      ////////////TEST_DEBIT///////////
      System.out.println("FOUR");
      String correct  = "[{\"id\":1,\"personKey\":\"3\",\"accountType\":\"CHECK\",\"bankKey\":\"4\",\"holdings\":200}]";
      String after  = "[{\"id\":1,\"personKey\":\"3\",\"accountType\":\"CHECK\",\"bankKey\":\"4\",\"holdings\":69}]";

      String res = httpHelper.get(ENDPOINT + "account/find/person", "person", "3");
      //Assert.assertEquals(correct, res);
      res = httpHelper.get(ENDPOINT + "account/debit/", "id", "1", "amount", "131");
      res = httpHelper.get(ENDPOINT + "account/find/person", "person", "3");
      //Assert.assertEquals(after, res);

      res = httpHelper.get(ENDPOINT + "account/debit/", "id", "1", "amount", "-2");
      Assert.assertEquals(res, "FAILED");
      res = httpHelper.get(ENDPOINT + "account/debit/", "id", "2000", "amount", "2");
      Assert.assertEquals(res, "FAILED");
      res = httpHelper.get(ENDPOINT + "account/debit/", "id", "1", "amount", "70");
      Assert.assertEquals(res, "FAILED");
    }


    @Test
    public void test5(){
      System.out.println("FIVE");
      ////////////TEST_TRANSACTIONS///////////

      String correct = "[{\"id\":2,\"type\":\"CREDIT\",\"amount\":200,\"created\":\"2020-10-20 11:50:14\",\"status\":\"OK\",\"account\":{\"id\":1,\"personKey\":\"3\",\"accountType\":\"CHECK\",\"bankKey\":\"4\",\"holdings\":69}},{\"id\":3,\"type\":\"DEBIT\",\"amount\":131,\"created\":\"2020-10-20 11:50:14\",\"status\":\"OK\",\"account\":{\"id\":1,\"personKey\":\"3\",\"accountType\":\"CHECK\",\"bankKey\":\"4\",\"holdings\":69}},{\"id\":4,\"type\":\"DEBIT\",\"amount\":70,\"created\":\"2020-10-20 11:50:14\",\"status\":\"FAILED\",\"account\":{\"id\":1,\"personKey\":\"3\",\"accountType\":\"CHECK\",\"bankKey\":\"4\",\"holdings\":69}}]";

      String pattern = "[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1]) (0[0-9]|1[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])";

      String res = httpHelper.get(ENDPOINT + "account/transactions", "id", "1");
      //Assert.assertEquals(correct.replaceAll(pattern, "time"), res.replaceAll(pattern, "time"));

      res = httpHelper.get(ENDPOINT + "account/transactions", "id", "42");
      Assert.assertEquals(res, "[]");


    }


}
