package es.udc.fi.dc.fd.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import es.udc.fi.dc.fd.model.persistence.RequestId;

public interface Request extends Serializable {

	public RequestId getRequestId();

	public LocalDateTime getDate();

	public void setRequestId(final RequestId requestId);

	public void setDate(final LocalDateTime date);

}
