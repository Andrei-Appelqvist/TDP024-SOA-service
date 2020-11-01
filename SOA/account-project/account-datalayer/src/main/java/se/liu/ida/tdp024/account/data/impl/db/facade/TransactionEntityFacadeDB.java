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
import se.liu.ida.tdp024.account.util.logger.KafkaObject;

public class TransactionEntityFacadeDB implements TransactionEntityFacade {
  private static final KafkaObject kafkaSender = new KafkaObject();

  @Override
  public boolean addTransaction(String type, Integer amount, boolean status, Account acc){
    kafkaSender.sendToKafka("transaction-topic", "{DATA (transaction): Add transaction started.}");
    ArrayList<String> allowed = new ArrayList();
    allowed.add("CREDIT");
    allowed.add("DEBIT");
    if(!allowed.contains(type) || amount < 0){
      kafkaSender.sendToKafka("transaction-topic", "{DATA (transaction): Transaction type was not CREDIT or DEBIT}");
      return false;
    }
    Gson gson = new Gson();
    EntityManager em = EMF.getEntityManager();
    try {
      em.getTransaction().begin();

      //Account acc = em.find(AccountDB.class, id);
      Transaction trans = new TransactionDB();

      if (acc == null)
      {
        em.getTransaction().commit();
        kafkaSender.sendToKafka("transaction-topic", "{DATA (transaction): Account could not be found }");
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
      kafkaSender.sendToKafka("transaction-topic", "{DATA (transaction): Add transaction succesfull.}");
      return true;
    } catch(Exception e){
      kafkaSender.sendToKafka("transaction-topic", "{DATA (transaction): Something went wrong: "+e+"}");
      return false;
    } finally {
      em.close();
    }
  }


  @Override
  public List<Transaction> findTransactions(long id){
    kafkaSender.sendToKafka("transaction-topic", "{DATA (transaction): Find transactions started.}");
    List<Transaction> transactions = new ArrayList();
    EntityManager em = EMF.getEntityManager();
    Query query = em.createQuery("SELECT c FROM TransactionDB c WHERE c.account.id = :id");
    query.setParameter("id", id);
    transactions = query.getResultList();
    kafkaSender.sendToKafka("transaction-topic", "{DATA (transaction): Transactions succesfully returned.}");
    return transactions;

  }


}
