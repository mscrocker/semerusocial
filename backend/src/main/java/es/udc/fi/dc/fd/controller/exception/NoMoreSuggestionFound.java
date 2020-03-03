package es.udc.fi.dc.fd.controller.exception;

@SuppressWarnings("serial")
public class NoMoreSuggestionFound extends InstanceException {

  public NoMoreSuggestionFound(String name, Object key) {
    super(name, key);
  }
}
