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


public class AccountEntityFacadeDB implements AccountEntityFacade {

  @Override
  public String test(){
    return "Omaaahghaaaad it works!";
  }

  @Override
  public boolean create(String accounttype, String personkey, String bankkey){
    ArrayList<String> allowed = new ArrayList();
    allowed.add("CHECK");
    allowed.add("SAVINGS");
    if(!allowed.contains(accounttype)){
      return false;
    }
    EntityManager em = EMF.getEntityManager();
    try{
      em.getTransaction().begin();
      Account acc = new AccountDB();
      acc.setPersonKey(personkey);
      acc.setAccountType(accounttype);
      acc.setHoldings(0);
      em.persist(acc);
      //em.flush();
      //System.out.printf("¤¤%s¤¤", acc.getId());
      em.getTransaction().commit();
      return true;
    } catch(Exception e){
      return false;
    } finally {
      em.close();
    }
  }

  @Override
  public String findAccounts(String personkey){
    EntityManager em = EMF.getEntityManager();
    try {
      List<Account> accounts = new ArrayList();
      Query query = em.createQuery("SELECT c FROM AccountDB c WHERE c.personKey = :personkey");
      query.setParameter("personkey", personkey);
      accounts = query.getResultList();
      Gson gson = new Gson();
      String strlst = gson.toJson(accounts);
      return strlst;
    } catch (Exception e){
      return null;
    } finally{
      em.close();
    }
  }

  @Override
  public boolean debitAccount(long id, Integer amount){
    EntityManager em = EMF.getEntityManager();
    em.getTransaction().begin();
    Account foundaccount = em.find(AccountDB.class, id, LockModeType.PESSIMISTIC_WRITE);
    boolean status = foundaccount.removeHoldings(amount);
    em.getTransaction().commit();
    em.close();
    return status;
  }

  @Override
  public boolean creditAccount(long id, Integer amount){
    EntityManager em = EMF.getEntityManager();
    em.getTransaction().begin();
    Account foundaccount = em.find(AccountDB.class, id, LockModeType.PESSIMISTIC_WRITE);
    boolean status = foundaccount.addHoldings(amount);
    em.getTransaction().commit();
    em.close();
    return status;
  }

  @Override
  public Account getAccount(long id) {
    EntityManager em = EMF.getEntityManager();
    Account foundaccount = em.find(AccountDB.class, id);
    return foundaccount;
  }

}
