package es.udc.fi.dc.fd.controller.exception;

@SuppressWarnings("serial")
public class InvalidImageException extends Exception{
	 
	public InvalidImageException()  {
	    	super("You can't remove a image that doesn't belong to you."); 	
	    }
}