package es.udc.fi.dc.fd.model;

import es.udc.fi.dc.fd.model.persistence.RequestId;
import java.io.Serializable;
import java.time.LocalDateTime;

public interface Request extends Serializable {

  public RequestId getRequestId();

  public LocalDateTime getDate();

  public void setRequestId(final RequestId requestId);

  public void setDate(final LocalDateTime date);

}
