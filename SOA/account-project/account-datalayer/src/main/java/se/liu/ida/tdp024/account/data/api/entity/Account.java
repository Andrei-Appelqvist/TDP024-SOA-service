package se.liu.ida.tdp024.account.data.api.entity;

import java.io.Serializable;

public interface Account extends Serializable {
    public long getId();
    public String getPersonKey();
    public void setPersonKey(String personKey);
    public void setAccountType(String accountType);
    public String getAccountType();
    public String getBankKey();
    public void setBankKey(String bankKey);
    public Integer getHoldings();
    public void setHoldings(Integer holdings);
    public boolean addHoldings(Integer holdings);
    public boolean removeHoldings(Integer holdings);


}
