package es.udc.fi.dc.fd.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public interface User extends Serializable {

    public Long getId();

    public String getUserName();

    public String getPassword();

    public LocalDateTime getDate();

    public String getSex();

    public String getCity();

	public User getSuggestion();

	public SexCriteriaEnum getCriteriaSex();

	public int getCriteriaMinAge();

	public int getCriteriaMaxAge();

    public void setId(final Long userId);

    public void setUserName(final String userName);

    public void setPassword(final String password);

    public void setDate(final LocalDateTime date);

    public void setSex(final String sex);

    public void setCity(final String city);

	public void setSuggestion(final User suggestion);

	public void setCriteriaSex(final SexCriteriaEnum criteriaSex);

	public void setCriteriaMinAge(final int minAge);

	public void setCriteriaMaxAge(final int maxAge);

}
