package hello;
import java.util.concurrent.atomic.AtomicInteger;

public class Account {

    private final Integer id;
    private final String personKey;
    private final String accountType;
    private final String bankKey;
    private long holdings;
    private static final AtomicInteger idGenerator = new AtomicInteger(1000);

    public Account(String personKey, String accountType, String bankKey) {
        this.personKey = personKey;
        this.accountType = accountType;
        this.bankKey = bankKey;
        this.id = idGenerator.getAndIncrement();
    }

    public Integer getId() {
        return id;
    }

    public String getPersonKey() {
        return personKey;
    }

    public String getAccountType() {
        return accountType;
    }

    public String getBankKey() {
        return bankKey;
    }

    public long getHoldings() {
        return holdings;
    }

    public long setHoldings(int amount) {
        holdings += amount;
        return holdings;
    }
}
