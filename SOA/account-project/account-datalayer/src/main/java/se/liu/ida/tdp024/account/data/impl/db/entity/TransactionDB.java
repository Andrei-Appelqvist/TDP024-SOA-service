package se.liu.ida.tdp024.account.data.impl.db.entity;

import se.liu.ida.tdp024.account.data.api.entity.Transaction;
import se.liu.ida.tdp024.account.data.api.entity.Account;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class TransactionDB implements Transaction {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  private String type;
  private Integer amount;
  private String created;
  private String status;

  @ManyToOne(targetEntity = AccountDB.class)
  private Account account;


  public long getId(){
    return id;
  }

  public void setId(long id){
    this.id = id;
  }

  public String getType(){
    return type;
  }

  public void setType(String type){
    this.type = type;
  }

  public Integer getAmount(){
    return amount;
  }

  public void setAmount(Integer amount){
    this.amount = amount;
  }

  public String getCreated(){
    return created;
  }

  public void setCreated(String created){
    this.created = created;
  }

  public Account getAccount(){
    return account;
  }

  public void setAccount(Account account){
    this.account = account;
  }

  public String getStatus(){
    return status;
  }

  public void setStatus(boolean status){
    if(status){
      this.status = "OK";
    } else {
      this.status = "FAILED";
    }
  }


}
