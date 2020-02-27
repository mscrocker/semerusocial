package es.udc.fi.dc.fd.controller.exception;

@SuppressWarnings("serial")
public class InvalidRecommendationException extends InstanceException {
	public InvalidRecommendationException(String name, Object key) {
		super(name, key);
	}

}
