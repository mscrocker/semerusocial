package es.udc.fi.dc.fd.controller.exception;

@SuppressWarnings("serial")
public class NotEnoughAgeException extends Exception {
	
	public NotEnoughAgeException(String message)  {
    	super(message); 	
    }
}
