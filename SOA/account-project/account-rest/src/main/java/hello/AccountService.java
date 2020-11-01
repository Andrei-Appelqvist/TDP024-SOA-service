package hello;
import se.liu.ida.tdp024.account.data.api.entity.Transaction;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import se.liu.ida.tdp024.account.logic.api.facade.AccountLogicFacade;
import se.liu.ida.tdp024.account.logic.api.facade.TransactionLogicFacade;
import se.liu.ida.tdp024.account.logic.impl.facade.AccountLogicFacadeImpl;
import se.liu.ida.tdp024.account.logic.impl.facade.TransactionLogicFacadeImpl;
import se.liu.ida.tdp024.account.data.impl.db.facade.AccountEntityFacadeDB;
import se.liu.ida.tdp024.account.data.impl.db.facade.TransactionEntityFacadeDB;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import se.liu.ida.tdp024.account.util.json.AccountJsonSerializer;
import se.liu.ida.tdp024.account.util.json.AccountJsonSerializerImpl;
import se.liu.ida.tdp024.account.util.logger.KafkaObject;
import com.google.gson.Gson;
import java.util.*;
import se.liu.ida.tdp024.account.data.api.entity.Account;


@RestController
@RequestMapping("account-rest")
public class AccountService {
  private final TransactionEntityFacadeDB transFacadeDb = new TransactionEntityFacadeDB();
  //private final TransactionLogicFacade transactionlogicfacade = new TransactionLogicFacadeImpl();
  private final TransactionLogicFacade transactionlogicfacade = new TransactionLogicFacadeImpl(transFacadeDb);
  private final AccountLogicFacade accountlogicfacade = new AccountLogicFacadeImpl(new AccountEntityFacadeDB());
  //private final AccountLogicFacade accountlogicfacade = new AccountLogicFacadeImpl(new AccountEntityFacadeDB(transFacadeDb));
  private static final AccountJsonSerializerImpl jsonSerializer = new AccountJsonSerializerImpl();
  private static final KafkaObject kafkaSender = new KafkaObject();

  @RequestMapping("/account/create")
  public String create(@RequestParam (required = false) String person,  @RequestParam (required = false) String bank, @RequestParam (required = false) String accounttype) {
    kafkaSender.sendToKafka("rest-topic", "{REST: Call to route create received}");
    if(person == null || bank==null ||accounttype==null){
      kafkaSender.sendToKafka("error-events", "{REST: Create failed, parameter missing or null}");
      return "FAILED";
    }
    boolean status = accountlogicfacade.register(person, bank, accounttype);
    if(status) {
      kafkaSender.sendToKafka("rest-topic", "{REST: Create was succesfull}");
      return "OK";
    }
    kafkaSender.sendToKafka("rest-topic", "{REST: Create failed, incorrect parameter(s)}");
    return "FAILED";
  }

  @RequestMapping("/account/find/person")
  public ResponseEntity<String> findPerson(@RequestParam String person){
    kafkaSender.sendToKafka("rest-topic", "{REST: Call to route find received}");
    List<Account> person_list = accountlogicfacade.findPerson(person);
    Gson gson = new Gson();
    String strlst = gson.toJson(person_list);
    kafkaSender.sendToKafka("rest-topic", "{REST: List of accounts served}");
    //return strlst;
    return new ResponseEntity(strlst, HttpStatus.OK);
  }

  @RequestMapping("/account/debit")
  public String debit(@RequestParam long id, @RequestParam Integer amount) {
    kafkaSender.sendToKafka("rest-topic", "{REST: Call to route debit received}");
    boolean status = accountlogicfacade.debitAccount(id, amount);
    transactionlogicfacade.addTransaction("DEBIT", id, amount, status);
    if(status){
      kafkaSender.sendToKafka("rest-topic", "{REST: Call to route debit. Debit was succesfull}");
      return "OK";

    }
    kafkaSender.sendToKafka("rest-topic", "{REST: Call to route debit. Debit failed, parameters might be invalid}");
    return "FAILED";

  }

  @RequestMapping("/account/credit")
  public String credit(@RequestParam long id, @RequestParam Integer amount){
    kafkaSender.sendToKafka("rest-topic", "{REST: Call to route credit received}");
    boolean status = accountlogicfacade.creditAccount(id, amount);
    transactionlogicfacade.addTransaction("CREDIT", id, amount, status);
    if(status)
    {
      kafkaSender.sendToKafka("rest-topic", "{REST: Call to route credit. Credit was succesfull}");
      return "OK";
    }
    kafkaSender.sendToKafka("rest-topic", "{REST: Call to route Credit. Credit failed, parameters might be invalid}");
    return "FAILED";
  }

  @RequestMapping("/account/transactions")
  public ResponseEntity transactions(@RequestParam long id) {
    kafkaSender.sendToKafka("rest-topic", "{REST: Call to route transactions received}");
    List<Transaction> trans_list = transactionlogicfacade.getTransactions(id);
    kafkaSender.sendToKafka("rest-topic", "{REST: List of transactions served}");
    Gson gson = new Gson();
    String strlst = gson.toJson(trans_list);
    // return trans_list;
    return new ResponseEntity(strlst, HttpStatus.OK);
  }
}
