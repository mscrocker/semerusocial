package es.udc.fi.dc.fd.model;

import es.udc.fi.dc.fd.model.persistence.RejectedId;
import java.io.Serializable;
import java.time.LocalDateTime;

public interface Rejected extends Serializable {

  public RejectedId getRejectedId();

  public LocalDateTime getDate();

  public void setRejectedId(final RejectedId rejectedId);

  public void setDate(final LocalDateTime date);

}
