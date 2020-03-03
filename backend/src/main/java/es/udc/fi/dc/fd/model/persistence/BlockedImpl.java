package es.udc.fi.dc.fd.model.persistence;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity(name = "Blocked")
@Table(name = "BlockTable")
public class BlockedImpl implements Serializable {

  @Transient
  private static final long serialVersionUID = 7235476221L;

  @EmbeddedId
  private BlockedId blockedId;

  @Column(name = "date", nullable = false)
  private LocalDateTime date;

  public BlockedImpl() {
    super();
  }

  /**
   * Default constructor of the Blocked entity.
   *
   * @param blockedId Entity containing the IDs of both de blocker and blocked users
   * @param date      The date and time when the block happened
   */
  public BlockedImpl(BlockedId blockedId, LocalDateTime date) {
    super();
    this.blockedId = blockedId;
    this.date = date;
  }

  public BlockedId getBlockedId() {
    return blockedId;
  }

  public void setBlockedId(BlockedId blockedId) {
    this.blockedId = blockedId;
  }

  public LocalDateTime getDate() {
    return date;
  }

  public void setDate(LocalDateTime date) {
    this.date = date;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((blockedId == null) ? 0 : blockedId.hashCode());
    result = prime * result + ((date == null) ? 0 : date.hashCode());
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
    final BlockedImpl other = (BlockedImpl) obj;
    if (blockedId == null) {
      if (other.blockedId != null) {
        return false;
      }
    } else if (!blockedId.equals(other.blockedId)) {
      return false;
    }
    if (date == null) {
      return other.date == null;
    } else {
      return date.equals(other.date);
    }
  }

  @Override
  public String toString() {
    return "BlockedImpl [blockedId=" + blockedId + ", date=" + date + "]";
  }

}
