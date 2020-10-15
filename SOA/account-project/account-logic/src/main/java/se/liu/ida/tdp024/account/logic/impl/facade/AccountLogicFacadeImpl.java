package se.liu.ida.tdp024.account.logic.impl.facade;

import se.liu.ida.tdp024.account.data.api.facade.AccountEntityFacade;
import se.liu.ida.tdp024.account.logic.api.facade.AccountLogicFacade;
import java.util.ArrayList;
import se.liu.ida.tdp024.account.util.http.HTTPHelper;
import se.liu.ida.tdp024.account.util.http.HTTPHelperImpl;
import se.liu.ida.tdp024.account.util.json.AccountJsonSerializer;
import se.liu.ida.tdp024.account.util.json.AccountJsonSerializerImpl;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.*;

public class AccountLogicFacadeImpl implements AccountLogicFacade {

    private AccountEntityFacade accountEntityFacade;
    private static final HTTPHelper httpHelper = new HTTPHelperImpl();
    private static final AccountJsonSerializerImpl jsonSerializer = new AccountJsonSerializerImpl();
    //public static final String ENDPOINT = "http://localhost:8080/account-rest/"

    public AccountLogicFacadeImpl(AccountEntityFacade accountEntityFacade) {
        this.accountEntityFacade = accountEntityFacade;
    }



    @Override
    public String test() {
        return accountEntityFacade.test();
    }

    @Override
    public boolean register(String person, String bank, String accounttype){
      //Kolla om person finns mot Python API (PersonApi)
      String response = httpHelper.get("http://localhost:8060/person" + "/find.key?key="+person);
      if (response.equals("null")){
        return false;
      }
      //Hämta bankkey med hjälp av namn från PythonApi (BankApi)
      response = httpHelper.get("http://localhost:8070/bank" + "/find.name?name="+bank);
      if (response.equals("null")){
        return false;
      }

      Map<String, Object> bankObject = jsonSerializer.fromJson(response, Map.class);
      Integer bankint = (Integer) bankObject.get("key");
      String bankkey = Integer.toString(bankint);
      return accountEntityFacade.create(accounttype, person, bankkey);
    }

    @Override
    public String findPerson(String person){
      String accounts = accountEntityFacade.findAccounts(person);
      return accounts;
    }

    @Override
    public boolean debitAccount(long id, Integer amount){
      return accountEntityFacade.debitAccount(id, amount);
    }

    @Override
    public boolean creditAccount(long id, Integer amount){
      return accountEntityFacade.creditAccount(id, amount);
    }
}
