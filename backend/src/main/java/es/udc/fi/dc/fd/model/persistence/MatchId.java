package es.udc.fi.dc.fd.model.persistence;

import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

@Embeddable
public class MatchId implements Serializable {

  @Transient
  private static final long serialVersionUID = 662827762820441L;

  private Long user1;

  private Long user2;

  public MatchId() {
    super();
  }

  /**
   * Default constructor for the entity containing the matching IDs of an users match.
   *
   * @param user1 The id of the first user
   * @param user2 The id of the second user
   */
  public MatchId(Long user1, Long user2) {
    super();
    this.user1 = user1;
    this.user2 = user2;
  }

  public Long getUser1() {
    return user1;
  }

  public void setUser1(Long user1) {
    this.user1 = user1;
  }

  public Long getUser2() {
    return user2;
  }

  public void setUser2(Long user2) {
    this.user2 = user2;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (user1 == null ? 0 : user1.hashCode());
    result = prime * result + (user2 == null ? 0 : user2.hashCode());
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
    final MatchId other = (MatchId) obj;
    if (user1 == null) {
      if (other.user1 != null) {
        return false;
      }
    } else if (!user1.equals(other.user1)) {
      return false;
    }
    if (user2 == null) {
      if (other.user2 != null) {
        return false;
      }
    } else if (!user2.equals(other.user2)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "MatchId [user1=" + user1 + ", user2=" + user2 + "]";
  }

}
