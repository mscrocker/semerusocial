package es.udc.fi.dc.fd.model;

import es.udc.fi.dc.fd.model.persistence.MatchId;
import java.io.Serializable;
import java.time.LocalDateTime;

public interface Match extends Serializable {

  public MatchId getMatchId();

  public LocalDateTime getDate();

  public void setMatchId(final MatchId matchId);

  public void setDate(final LocalDateTime date);

}
