package es.udc.fi.dc.fd.controller.exception;

@SuppressWarnings("serial")
public class AlreadyBlockedException extends Exception {

  public AlreadyBlockedException(String message) {
    super(message);
  }
}
