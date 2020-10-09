package se.liu.ida.tdp024.account.data.api.entity;

import java.io.Serializable;

public interface Transaction extends Serializable {
  public long getId();
  public void setId(long id);
  public String getType();
  public void setType(String type);
  public Integer getAmount();
  public void setAmount(Integer amount);
  public String getCreated();
  public void setCreated(String created);
  public Account getAccount();
  public void setAccount(Account account);
  public boolean getStatus();
  public void setStatus(boolean status);
}
