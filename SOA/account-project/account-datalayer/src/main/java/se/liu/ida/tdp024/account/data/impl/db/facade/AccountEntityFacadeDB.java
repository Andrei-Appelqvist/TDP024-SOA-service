package se.liu.ida.tdp024.account.data.impl.db.facade;
import java.util.ArrayList;
import se.liu.ida.tdp024.account.data.api.facade.AccountEntityFacade;
import se.liu.ida.tdp024.account.data.api.entity.Account;
import se.liu.ida.tdp024.account.data.impl.db.util.EMF;
import se.liu.ida.tdp024.account.data.impl.db.entity.AccountDB;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import com.google.gson.Gson;
import javax.persistence.LockModeType;
import se.liu.ida.tdp024.account.util.logger.KafkaObject;



public class AccountEntityFacadeDB implements AccountEntityFacade {
    private static final KafkaObject kafkaBitches = new KafkaObject();

  @Override
  public boolean create(String accounttype, String personkey, String bankkey){
    kafkaBitches.sendToKafka("transaction-topic", "{DATA (account): Create transaction started.}");
    ArrayList<String> allowed = new ArrayList();
    allowed.add("CHECK");
    allowed.add("SAVINGS");
    if(!allowed.contains(accounttype)){
      kafkaBitches.sendToKafka("transaction-topic", "{DATA (account) (account): Create failed. Account type was not CHECK or SAVINGS.}");
      return false;
    }
    EntityManager em = EMF.getEntityManager();
    try{
      em.getTransaction().begin();
      Account acc = new AccountDB();
      acc.setPersonKey(personkey);
      acc.setAccountType(accounttype);
      acc.setHoldings(0);
      acc.setBankKey(bankkey);
      em.persist(acc);
      em.getTransaction().commit();
      kafkaBitches.sendToKafka("transaction-topic", "{DATA (account): Create was succesfull.}");
      return true;
    } catch(Exception e){
      kafkaBitches.sendToKafka("transaction-topic", "{DATA (account): Something went wrong: "+e+"}");
      return false;
    } finally {
      em.close();
    }
  }

  @Override
  public List<Account> findAccounts(String personkey){
    kafkaBitches.sendToKafka("transaction-topic", "{DATA (account): Find Accounts transaction started.}");
    EntityManager em = EMF.getEntityManager();
    List<Account> accounts = new ArrayList();
    try {
      Query query = em.createQuery("SELECT c FROM AccountDB c WHERE c.personKey = :personkey");
      query.setParameter("personkey", personkey);
      accounts = query.getResultList();
      kafkaBitches.sendToKafka("transaction-topic", "{DATA (account): List of accounts succesfully returned}");
      return accounts;
    } catch (Exception e){
      kafkaBitches.sendToKafka("transaction-topic", "{DATA (account): Something went wrong: "+e+"}");
      return accounts;
    } finally{
      em.close();
    }
  }

  @Override
  public boolean debitAccount(long id, Integer amount){
    //kafkaBitches.sendToKafka("error-events", "{DATA (account): Debit transaction started}");
    if(amount < 0){
      //kafkaBitches.sendToKafka("error-events", "{DATA (account): Debit failed. Negative amount is not allowed.}");
      return false;
    }
    EntityManager em = EMF.getEntityManager();
    em.getTransaction().begin();
    Account foundaccount = em.find(AccountDB.class, id, LockModeType.PESSIMISTIC_WRITE);
    if(foundaccount == null){
      em.getTransaction().commit();
      em.close();
      //kafkaBitches.sendToKafka("error-events", "{DATA (account): No account with given id could be found}");
      return false;
    }
    boolean status = foundaccount.removeHoldings(amount);
    em.getTransaction().commit();
    em.close();
    //kafkaBitches.sendToKafka("error-events", "{DATA (account): Debit was succesfull.}");
    return status;
  }

  @Override
  public boolean creditAccount(long id, Integer amount){
    //kafkaBitches.sendToKafka("error-events", "{DATA (account): Credit transaction started}");
    if(amount < 0){
      //kafkaBitches.sendToKafka("error-events", "{DATA (account): Credit failed. Negative amount is not allowed.}");
      return false;
    }
    EntityManager em = EMF.getEntityManager();
    em.getTransaction().begin();
    Account foundaccount = em.find(AccountDB.class, id, LockModeType.PESSIMISTIC_WRITE);
    if(foundaccount == null){
      em.getTransaction().commit();
      em.close();
      //kafkaBitches.sendToKafka("error-events", "{DATA (account): Credit failed. Account could not be found}");
      return false;
    }
    boolean status = foundaccount.addHoldings(amount);
    em.getTransaction().commit();
    em.close();
    //kafkaBitches.sendToKafka("error-events", "{DATA (account): Credit was succesfull. Adding given amount to account}");
    return status;
  }

  @Override
  public Account getAccount(long id) {
    kafkaBitches.sendToKafka("transaction-topic", "{DATA (account): Get account transaction started.}");
    EntityManager em = EMF.getEntityManager();
    Account foundaccount = em.find(AccountDB.class, id);
    em.close();
    kafkaBitches.sendToKafka("transaction-topic", "{DATA (account): Account returned.}");
    return foundaccount;
  }

}
