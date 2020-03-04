package es.udc.fi.dc.fd.controller.exception;

@SuppressWarnings("serial")
public class InstanceNotFoundException extends InstanceException {

  /**
   * Default constructor of the instance not found exception.
   *
   * @param name The name of the class
   * @param key  The key of the instance
   */
  public InstanceNotFoundException(String name, Object key) {
    super(name, key);
  }

}
