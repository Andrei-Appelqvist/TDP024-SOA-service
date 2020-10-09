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


public class AccountEntityFacadeDB implements AccountEntityFacade {

  @Override
  public String test(){
    return "Omaaahghaaaad it works!";
  }

  @Override
  public boolean create(String accounttype, String personkey, String bankkey){
    EntityManager em = EMF.getEntityManager();
    em.getTransaction().begin();
    Account acc = new AccountDB();
    acc.setPersonKey(personkey);
    acc.setAccountType(accounttype);
    //System.out.printf("=%s=", acc.getId());
    em.persist(acc);
    em.flush();
    System.out.printf("¤¤%s¤¤", acc.getId());
    em.getTransaction().commit();
    em.close();
    //System.out.printf("%s, %s, %s",accounttype, personkey, bankkey);
    return true;
  }

  @Override
  public List<Account> findAccounts(String personkey){
    EntityManager em = EMF.getEntityManager();
    ArrayList accounts = new ArrayList();
    accounts.add(personkey);
    Query query = em.createQuery("SELECT c FROM AccountDB c WHERE c.personKey = :personkey");
    query.setParameter("personkey", personkey);
    List asdf = query.getResultList();
    Account a = (Account) asdf.get(0);
    System.out.printf("%s", a.getPersonKey());
    //Account asdf = em.find(AccountDB.class, personkey);
    return accounts;
  }

  @Override
  public boolean debitAccount(long id, Integer amount){
    System.out.printf("%s, %d", id, amount);
    return true;
  }

  @Override
  public boolean creditAccount(long id, Integer amount){
    EntityManager em = EMF.getEntityManager();
    Account foundaccount = em.find(AccountDB.class, id);
    System.out.printf("????????????%s?????????????", foundaccount);
    return true;
  }

  @Override
  public Account getAccount(long id) {
    EntityManager em = EMF.getEntityManager();
    Account foundaccount = em.find(AccountDB.class, id);
    System.out.printf("%s", foundaccount.getAccountType());
    return foundaccount;
  }

}
