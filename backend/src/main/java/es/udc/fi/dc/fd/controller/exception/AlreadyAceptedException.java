package es.udc.fi.dc.fd.controller.exception;

@SuppressWarnings("serial")
public class AlreadyAceptedException extends InstanceException {

  public AlreadyAceptedException(String name, Object key) {
    super(name, key);
  }

}
