package es.udc.fi.dc.fd.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import es.udc.fi.dc.fd.model.persistence.MatchId;

public interface Match extends Serializable {

	public MatchId getMatchId();

	public LocalDateTime getDate();

	public void setMatchId(final MatchId matchId);

	public void setDate(final LocalDateTime date);

}
