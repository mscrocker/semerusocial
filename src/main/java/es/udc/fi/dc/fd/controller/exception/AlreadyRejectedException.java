package es.udc.fi.dc.fd.controller.exception;

@SuppressWarnings("serial")
public class AlreadyRejectedException extends InstanceException {

	public AlreadyRejectedException(String name, Object key) {
		super(name, key);
	}

}
