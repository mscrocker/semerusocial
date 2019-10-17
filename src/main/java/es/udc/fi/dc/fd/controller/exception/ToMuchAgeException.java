package es.udc.fi.dc.fd.controller.exception;

@SuppressWarnings("serial")
public class ToMuchAgeException extends Exception {

	public ToMuchAgeException(String message) {
		super(message);
	}
}
