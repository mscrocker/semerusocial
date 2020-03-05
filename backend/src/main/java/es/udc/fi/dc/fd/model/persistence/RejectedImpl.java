package es.udc.fi.dc.fd.model.persistence;

import es.udc.fi.dc.fd.model.Rejected;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity(name = "Rejected")
@Table(name = "RejectedTable")
public class RejectedImpl implements Rejected {

  @Transient
  private static final long serialVersionUID = 7235476221L;

  @EmbeddedId
  private RejectedId rejectedId;

  @Column(name = "date", nullable = false)
  private LocalDateTime date;

  public RejectedImpl() {
    super();
  }

  /**
   * Default constructor for the rejected entity.
   *
   * @param rejectedId The IDs of the users involved in the rejection
   * @param date       The date of the rejection
   */
  public RejectedImpl(RejectedId rejectedId, LocalDateTime date) {
    super();
    this.rejectedId = rejectedId;
    this.date = date;
  }

  @Override
  public RejectedId getRejectedId() {
    return rejectedId;
  }

  @Override
  public void setRejectedId(RejectedId rejectedId) {
    this.rejectedId = rejectedId;
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
    result = prime * result + (rejectedId == null ? 0 : rejectedId.hashCode());
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
    final RejectedImpl other = (RejectedImpl) obj;
    if (date == null) {
      if (other.date != null) {
        return false;
      }
    } else if (!date.equals(other.date)) {
      return false;
    }
    if (rejectedId == null) {
      if (other.rejectedId != null) {
        return false;
      }
    } else if (!rejectedId.equals(other.rejectedId)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "RejectedImpl [rejectedId=" + rejectedId + ", date=" + date + "]";
  }

}
