package es.udc.fi.dc.fd.controller.exception;

@SuppressWarnings("serial")
public class IncorrectLoginException extends Exception {

  private String userName;

  private String password;

  /**
   * Exception thrown when an login attempt was not succesfull.
   *
   * @param userName The userName of the login attempt
   * @param password The password of the login attempt
   */
  public IncorrectLoginException(String userName, String password) {

    this.userName = userName;
    this.password = password;

  }

  public String getUserName() {
    return userName;
  }

  public String getPassword() {
    return password;
  }

}
