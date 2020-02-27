package es.udc.fi.dc.fd.controller.exception;

@SuppressWarnings("serial")
public class InstanceNotFoundException extends InstanceException {

	public InstanceNotFoundException(String name, Object key) {
		super(name, key);
	}

}
