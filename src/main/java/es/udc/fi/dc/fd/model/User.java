package es.udc.fi.dc.fd.model;

import java.io.Serializable;

public interface User extends Serializable {

    public Long getId();

    public String getUserName();

    public String getPassword();

    public int getAge();

    public String getSex();

    public String getCity();

    public void setId(final Long userId);

    public void setUserName(final String userName);

    public void setPassword(final String password);

    public void setAge(final int age);

    public void setSex(final String sex);

    public void setCity(final String city);
    
}
