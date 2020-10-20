package se.liu.ida.tdp024.account.data.impl.db.facade;
import java.util.ArrayList;
import se.liu.ida.tdp024.account.data.api.facade.TransactionEntityFacade;
import se.liu.ida.tdp024.account.data.api.entity.Transaction;
import se.liu.ida.tdp024.account.data.impl.db.util.EMF;
import se.liu.ida.tdp024.account.data.impl.db.entity.TransactionDB;
import se.liu.ida.tdp024.account.data.impl.db.entity.AccountDB;
import se.liu.ida.tdp024.account.data.api.entity.Account;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import com.google.gson.Gson;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TransactionEntityFacadeDB implements TransactionEntityFacade {

  @Override
  public boolean addTransaction(String type, long id, Integer amount, boolean status){
    ArrayList<String> allowed = new ArrayList();
    allowed.add("CREDIT");
    allowed.add("DEBIT");
    if(!allowed.contains(type) || amount < 0){
      return false;
    }
    Gson gson = new Gson();
    EntityManager em = EMF.getEntityManager();
    try {
      em.getTransaction().begin();

      Account acc = em.find(AccountDB.class, id);
      Transaction trans = new TransactionDB();

      if (acc == null)
      {
        em.getTransaction().commit();
        return false;
      }
      String accjson = gson.toJson(acc);

      trans.setAccount(acc);
      trans.setType(type);
      trans.setAmount(amount);
      trans.setStatus(status);
      SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      Date date = new Date(System.currentTimeMillis());
      trans.setCreated(formatter.format(date));

      em.persist(trans);
      em.getTransaction().commit();
      return true;
    } catch(Exception e){
      return false;
      //Skriv grejer till KAfka
    } finally {
      em.close();
    }
  }


  @Override
  public List<Transaction> findTransactions(long id){
    List<Transaction> transactions = new ArrayList();
    EntityManager em = EMF.getEntityManager();
    Query query = em.createQuery("SELECT c FROM TransactionDB c WHERE c.account.id = :id");
    query.setParameter("id", id);
    transactions = query.getResultList();
    return transactions;

  }


}
