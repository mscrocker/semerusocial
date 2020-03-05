package es.udc.fi.dc.fd.model.persistence;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity(name = "Rate")
@Table(name = "RateTable")
public class RateImpl implements Serializable {

  @Transient
  private static final long serialVersionUID = 7235476221L;

  @EmbeddedId
  private RateId rateId;

  @Column(name = "points", nullable = false)
  private int points;

  public RateImpl() {
    super();
  }

  /**
   * Default constructor for the rate entity.
   *
   * @param rateId The IDs of the users involved in the rating
   * @param points The rating
   */
  public RateImpl(RateId rateId, int points) {
    super();
    this.rateId = rateId;
    this.points = points;
  }

  public RateId getRateId() {
    return rateId;
  }

  public void setRateId(RateId rateId) {
    this.rateId = rateId;
  }

  public int getPoints() {
    return points;
  }

  public void setPoints(int points) {
    this.points = points;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + points;
    result = prime * result + ((rateId == null) ? 0 : rateId.hashCode());
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
    final RateImpl other = (RateImpl) obj;
    if (points != other.points) {
      return false;
    }
    if (rateId == null) {
      if (other.rateId != null) {
        return false;
      }
    } else if (!rateId.equals(other.rateId)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "RateImpl [rateId=" + rateId + ", points=" + points + "]";
  }

}
