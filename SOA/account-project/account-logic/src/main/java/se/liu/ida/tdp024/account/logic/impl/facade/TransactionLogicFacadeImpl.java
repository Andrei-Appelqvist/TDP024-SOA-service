package se.liu.ida.tdp024.account.logic.impl.facade;
import se.liu.ida.tdp024.account.logic.api.facade.TransactionLogicFacade;
import se.liu.ida.tdp024.account.data.api.facade.TransactionEntityFacade;
import java.util.ArrayList;
import se.liu.ida.tdp024.account.util.http.HTTPHelper;
import se.liu.ida.tdp024.account.util.http.HTTPHelperImpl;
import se.liu.ida.tdp024.account.util.json.AccountJsonSerializer;
import se.liu.ida.tdp024.account.util.json.AccountJsonSerializerImpl;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.*;



public class TransactionLogicFacadeImpl implements TransactionLogicFacade {


  private TransactionEntityFacade transactionEntityFacade;
  private static final HTTPHelper httpHelper = new HTTPHelperImpl();
  private static final AccountJsonSerializerImpl jsonSerializer = new AccountJsonSerializerImpl();

  public TransactionLogicFacadeImpl(TransactionEntityFacade transactionEntityFacade) {
    this.transactionEntityFacade = transactionEntityFacade;
  }

  @Override
  public void addTransaction(String type, long id, Integer amount, boolean status){
    transactionEntityFacade.addTransaction(type, id, amount, status);
  }

  @Override
  public String getTransactions(long id){
    String asdf = transactionEntityFacade.findTransactions(id);
    System.out.printf("%s", asdf);
    return asdf;
  }

}
