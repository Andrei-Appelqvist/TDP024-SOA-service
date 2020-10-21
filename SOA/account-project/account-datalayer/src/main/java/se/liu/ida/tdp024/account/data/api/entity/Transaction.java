package se.liu.ida.tdp024.account.data.api.entity;

import java.io.Serializable;

public interface Transaction extends Serializable {

  public String getType();
  public void setType(String type);
  public Integer getAmount();
  public void setAmount(Integer amount);
  public void setCreated(String created);
  public Account getAccount();
  public void setAccount(Account account);
  public String getStatus();
  public void setStatus(boolean status);
}
