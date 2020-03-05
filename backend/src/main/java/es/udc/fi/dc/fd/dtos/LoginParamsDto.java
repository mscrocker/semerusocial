package es.udc.fi.dc.fd.dtos;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Serializable;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public final class LoginParamsDto implements Serializable {

  private static final long serialVersionUID = 1328776989450853492L;

  @NotEmpty
  @Length(min = 4, max = 30)
  private String userName;

  @NotEmpty
  @Length(min = 4, max = 20)
  private String password;

  public LoginParamsDto() {
    super();
  }

  /**
   * Default constructor for the dto containing the parameters required for logging in.
   * @param userName The userName of the user
   * @param password The password of the user
   */
  public LoginParamsDto(String userName, String password) {
    super();
    this.userName = userName;
    this.password = password;
  }


  public final String getUserName() {
    return userName;
  }

  public String getPassword() {
    return password;
  }

  public final void setUserName(final String userName) {
    this.userName = checkNotNull(userName, "Received a null pointer as userName"
        + " in LoginParamsDto");
  }

  public void setPassword(String password) {
    this.password = checkNotNull(password, "Received a null pointer as password"
        + " in LoginParamsDto");
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((password == null) ? 0 : password.hashCode());
    result = prime * result + ((userName == null) ? 0 : userName.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    LoginParamsDto other = (LoginParamsDto) obj;
    if (password == null) {
      if (other.password != null) {
        return false;
      }
    } else if (!password.equals(other.password)) {
      return false;
    }
    if (userName == null) {
      if (other.userName != null) {
        return false;
      }
    } else if (!userName.equals(other.userName)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "LoginParamsDto [userName=" + userName + ", password=" + password + "]";
  }

}
