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

public class TransactionEntityFacadeDB implements TransactionEntityFacade {

  @Override
  public void addTransaction(String type, long id, Integer amount, boolean status){

    Gson gson = new Gson();
    EntityManager em = EMF.getEntityManager();


    em.getTransaction().begin();

    Account acc = em.find(AccountDB.class, id);
    Transaction trans = new TransactionDB();

    if (acc == null)
    {
      System.out.printf("%s,", "//////account is null/////");
    }
    String accjson = gson.toJson(acc);

    System.out.printf("%s,", accjson);
    trans.setAccount(acc);
    trans.setType(type);
    trans.setAmount(amount);
    trans.setStatus(true);
    trans.setCreated("2020-10-09 10:41");

    em.persist(trans);
    em.getTransaction().commit();
    em.close();
    String json = gson.toJson(trans);
    System.out.printf("%s,", "=====================");
    System.out.printf("%s,", json);
  }


  @Override
  public ArrayList<Transaction> findTransactions(long id){
    return new ArrayList();
  }


}
