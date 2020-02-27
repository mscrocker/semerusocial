package es.udc.fi.dc.fd.controller.exception;

@SuppressWarnings("serial")
public class DuplicateInstanceException extends InstanceException {

	public DuplicateInstanceException(String name, Object key) {
		super(name, key);
	}

}
