package es.udc.fi.dc.fd.model.persistence;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import es.udc.fi.dc.fd.model.Match;

@Entity(name = "Match")
@Table(name = "MatchTable")
public class MatchImpl implements Match {

	@Transient
	private static final long serialVersionUID = 7236756751L;

	@EmbeddedId
	private MatchId matchId;

	@Column(name = "date", nullable = false)
	private LocalDateTime date;

	public MatchImpl() {
		super();
	}

	public MatchImpl(MatchId matchId, LocalDateTime date) {
		super();
		this.matchId = matchId;
		this.date = date;
	}

	@Override
	public MatchId getMatchId() {
		return matchId;
	}

	@Override
	public void setMatchId(MatchId matchId) {
		this.matchId = matchId;
	}

	@Override
	public LocalDateTime getDate() {
		return date;
	}

	@Override
	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (date == null ? 0 : date.hashCode());
		result = prime * result + (matchId == null ? 0 : matchId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final MatchImpl other = (MatchImpl) obj;
		if (date == null) {
			if (other.date != null) {
				return false;
			}
		} else if (!date.equals(other.date)) {
			return false;
		}
		if (matchId == null) {
			if (other.matchId != null) {
				return false;
			}
		} else if (!matchId.equals(other.matchId)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "MatchImpl [matchId=" + matchId + ", date=" + date + "]";
	}

}
