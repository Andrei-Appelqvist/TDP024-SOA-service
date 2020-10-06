package hello;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class Transaction {

    private final long id;
    private final String type;
    private final long amount;
    private final String created;
    private String status;
    //private final Account account;

    //private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public Transaction(long id, String type, long amount) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        //this.account = new Account(id, type);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        this.created = now.format(dtf);

        System.out.println(this.created);
        //Date date = new Date();
        //this.created = formatter.format(date);
    }

    public long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public long getAmount() {
      return amount;
    }

    public String getCreated() {
      return created;
    }

    public String getStatus(){
      return status;
    }

    //public Account getAccount() {
      //return account;
    //}

    public String setStatus(String status) {
      this.status = status;
      return status;
    }
}
