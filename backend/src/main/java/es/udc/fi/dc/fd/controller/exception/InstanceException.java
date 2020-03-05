package es.udc.fi.dc.fd.controller.exception;

@SuppressWarnings("serial")
public abstract class InstanceException extends Exception {

  private String name;

  private Object key;

  /**
   * Default constructor of the instance exception.
   *
   * @param name The name of the class
   * @param key  The key of the instance
   */
  public InstanceException(String name, Object key) {

    this.name = name;
    this.key = key;

  }

  public String getName() {
    return name;
  }

  public Object getKey() {
    return key;
  }

}
