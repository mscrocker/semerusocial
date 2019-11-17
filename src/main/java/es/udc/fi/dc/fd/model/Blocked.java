package es.udc.fi.dc.fd.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import es.udc.fi.dc.fd.model.persistence.BlockedId;

public interface Blocked extends Serializable {

	public BlockedId getBlockedId();

	public LocalDateTime getDate();

	public void setBlockedId(final BlockedId blockedId);

	public void setDate(final LocalDateTime date);
}
