
package hello;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        for(int i = 0; i<10; ++i) {
          //acc = new Account("asdf", "fdsa", "assa");

          new Transaction(30, "DEBIT", 20);
        }
        return new Greeting(counter.incrementAndGet(),
                            String.format(template, name));
    }
/*
    @RequestMapping("/account-rest/account/create")
    public Greeting create(@RequestParam String accounttype, @RequestParam String person,@RequestParam String bank) {
        return new Greeting(counter.incrementAndGet(),String.format("Called create with %s %s %s!", accounttype, person, bank));
        //http://localhost:8080/account-rest/account/create?accounttype=CHECKING&person=32134123&bank=NORDEA
    }

    @RequestMapping("/account-rest/account/find/person")
    public Greeting findPerson(@RequestParam String person) {
        return new Greeting(counter.incrementAndGet(),String.format("Called find person with value %s!", person));
        //http://localhost:8080/account-rest/account/find/person?person=omfgwhatisthis
    }

    @RequestMapping("/account-rest/account/debit")
    public Greeting debit(@RequestParam String id, @RequestParam Integer amount) {
        return new Greeting(counter.incrementAndGet(),String.format("Called debit with value %s %d!", id, amount));
        //http://localhost:8080/account-rest/account/debit?id=uniktkonto&amount=23
    }

    @RequestMapping("/account-rest/account/credit")
    public Greeting credit(@RequestParam String id, @RequestParam Integer amount) {
        return new Greeting(counter.incrementAndGet(),String.format("Called credit with value %s %d!", id, amount));
        //http://localhost:8080/account-rest/account/credit?id=uniktkonto2&amount=24
    }

    @RequestMapping("/account-rest/account/transactions")
    public Greeting transactions(@RequestParam String id) {
        return new Greeting(counter.incrementAndGet(),String.format("Called transactions with value %s!", id));
        //http://localhost:8080/account-rest/account/transactions?id=uniktkonto3
    }
    */
}
