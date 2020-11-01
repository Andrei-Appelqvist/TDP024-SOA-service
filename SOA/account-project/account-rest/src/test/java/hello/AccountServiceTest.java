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
import se.liu.ida.tdp024.account.util.json.AccountJsonSerializer;
import se.liu.ida.tdp024.account.util.json.AccountJsonSerializerImpl;
import test.util.AccountDTO;
import test.util.TransactionDTO;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
//import se.liu.ida.tdp024.account.xfinal.test.util.AccountDTO;
// import se.liu.ida.tdp024.account.xfinal.test.util.FinalConstants;

public class AccountServiceTest {
    //--- Unit under test ---//
    public StorageFacade storageFacade = new StorageFacadeDB();
    private static final HTTPHelper httpHelper = new HTTPHelperImpl();
    private static final AccountJsonSerializer jsonSerializer = new AccountJsonSerializerImpl();
    public AccountService accS = new AccountService();
    //String ENDPOINT = "http://localhost:8080/account-rest/";
    public long id = 1;

    public List<AccountDTO> accs(String bod){
      AccountDTO[] accDTos = jsonSerializer.fromJson(bod, AccountDTO[].class);
      List<AccountDTO> accounts = new ArrayList<AccountDTO>();
      for (AccountDTO t : accDTos) {
        accounts.add(t);
      }
      Collections.sort(accounts, new Comparator<AccountDTO>() {
        @Override
        public int compare(AccountDTO t, AccountDTO t1) {
          if (t.getId() > t1.getId()) {
            return 1;
          } else if (t.getId() < t1.getId()) {
            return -1;
          } else {
            return 0;
          }
        }
      });
      return accounts;
    }

    public List<TransactionDTO> transacts(String bod){
      TransactionDTO[] transDTos = jsonSerializer.fromJson(bod, TransactionDTO[].class);
      List<TransactionDTO> transactions = new ArrayList<TransactionDTO>();
      for (TransactionDTO t : transDTos) {
        transactions.add(t);
      }
      Collections.sort(transactions, new Comparator<TransactionDTO>() {
        @Override
        public int compare(TransactionDTO t, TransactionDTO t1) {
          if (t.getId() > t1.getId()) {
            return 1;
          } else if (t.getId() < t1.getId()) {
            return -1;
          } else {
            return 0;
          }
        }
      });
      return transactions;
    }

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


    @Test
    public void test2(){
      ////////////TEST_FIND///////////
      System.out.println("TWO");
      String res = accS.create("3", "NORDEA", "CHECK");
      Assert.assertEquals(res, "OK");
      ResponseEntity<String> rss = accS.findPerson("3");
      String bod = rss.getBody();
      List<AccountDTO> accounts = accs(bod);
      Assert.assertEquals(accounts.get(0).getId(), 1);
      Assert.assertEquals(accounts.get(0).getAccountType(), "CHECK");
      Assert.assertEquals(accounts.get(0).getBankKey(), "4");
      Assert.assertEquals((long)accounts.get(0).getHoldings(), 0);


      String respBody = accS.findPerson("27").getBody();
      Assert.assertEquals("[]", respBody);
    }


    @Test
    public void test3(){
      ////////////TEST_CREDIT///////////
      String res = accS.create("3", "NORDEA", "CHECK");
      Assert.assertEquals(res, "OK");
      ResponseEntity<String> rss = accS.findPerson("3");
      String bod = rss.getBody();
      List<AccountDTO> accounts = accs(bod);

      Assert.assertEquals(accounts.get(0).getId(), 1);
      Assert.assertEquals(accounts.get(0).getAccountType(), "CHECK");
      Assert.assertEquals(accounts.get(0).getBankKey(), "4");
      Assert.assertEquals((long)accounts.get(0).getHoldings(), 0);

      res = accS.credit(1, 200);
      Assert.assertEquals(res, "OK");

      rss = accS.findPerson("3");
      bod = rss.getBody();
      accounts = accs(bod);

      Assert.assertEquals(accounts.get(0).getId(), 1);
      Assert.assertEquals(accounts.get(0).getAccountType(), "CHECK");
      Assert.assertEquals(accounts.get(0).getBankKey(), "4");
      Assert.assertEquals((long)accounts.get(0).getHoldings(), 200);

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

      ResponseEntity<String> rss = accS.findPerson("3");
      String bod = rss.getBody();
      List<AccountDTO> accounts = accs(bod);

      Assert.assertEquals(accounts.get(0).getId(), 1);
      Assert.assertEquals(accounts.get(0).getAccountType(), "CHECK");
      Assert.assertEquals(accounts.get(0).getBankKey(), "4");
      Assert.assertEquals((long)accounts.get(0).getHoldings(), 0);

      res = accS.credit(1, 200);
      Assert.assertEquals(res, "OK");

      rss = accS.findPerson("3");
      bod = rss.getBody();
      accounts = accs(bod);

      Assert.assertEquals(accounts.get(0).getId(), 1);
      Assert.assertEquals(accounts.get(0).getAccountType(), "CHECK");
      Assert.assertEquals(accounts.get(0).getBankKey(), "4");
      Assert.assertEquals((long)accounts.get(0).getHoldings(), 200);

      res = accS.debit(1, 131);
      Assert.assertEquals(res, "OK");

      rss = accS.findPerson("3");
      bod = rss.getBody();
      accounts = accs(bod);

      Assert.assertEquals(accounts.get(0).getId(), 1);
      Assert.assertEquals(accounts.get(0).getAccountType(), "CHECK");
      Assert.assertEquals(accounts.get(0).getBankKey(), "4");
      Assert.assertEquals((long)accounts.get(0).getHoldings(), 69);

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

      ResponseEntity<String> rss = accS.findPerson("3");
      String bod = rss.getBody();
      List<AccountDTO> accounts = accs(bod);
      AccountDTO accountDTO = accounts.get(0);

      res = accS.credit(1, 200);
      Assert.assertEquals(res, "OK");
      res = accS.debit(1, 131);
      Assert.assertEquals(res, "OK");
      res = accS.debit(1, 70);
      Assert.assertEquals(res, "FAILED");


      rss = accS.transactions(1);
      bod = rss.getBody();
      List<TransactionDTO> transactions = transacts(bod);

      Assert.assertEquals(transactions.get(0).getId(), 2);
      Assert.assertEquals(transactions.get(0).getType(), "CREDIT");
      Assert.assertEquals((long)transactions.get(0).getAmount(), 200);
      Assert.assertNotNull(transactions.get(0).getCreated());
      Assert.assertEquals(transactions.get(0).getStatus(), "OK");

      Assert.assertEquals(transactions.get(1).getId(), 3);
      Assert.assertEquals(transactions.get(1).getType(), "DEBIT");
      Assert.assertEquals((long)transactions.get(1).getAmount(), 131);
      Assert.assertNotNull(transactions.get(1).getCreated());
      Assert.assertEquals(transactions.get(1).getStatus(), "OK");

      Assert.assertEquals(transactions.get(2).getId(), 4);
      Assert.assertEquals(transactions.get(2).getType(), "DEBIT");
      Assert.assertEquals((long)transactions.get(2).getAmount(), 70);
      Assert.assertNotNull(transactions.get(2).getCreated());
      Assert.assertEquals(transactions.get(2).getStatus(), "FAILED");

      rss = accS.transactions(42);
      bod = rss.getBody();
      Assert.assertEquals(bod, "[]");

    }

}
