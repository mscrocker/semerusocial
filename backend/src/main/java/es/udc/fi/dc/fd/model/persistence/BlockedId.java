package es.udc.fi.dc.fd.model.persistence;

import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

@Embeddable
public class BlockedId implements Serializable {

  @Transient
  private static final long serialVersionUID = 668475227762820441L;

  private Long subject; // el que bloquea

  private Long object; // el bloqueado

  /**
   * Default constructor for the BlockedId entity.
   * @param subject The subject who blocks
   * @param object The object who gets blocked
   */
  public BlockedId(Long subject, Long object) {
    super();
    this.subject = subject;
    this.object = object;
  }

  public BlockedId() {
    super();
  }

  public Long getSubject() {
    return subject;
  }

  public void setSubject(Long subject) {
    this.subject = subject;
  }

  public Long getObject() {
    return object;
  }

  public void setObject(Long object) {
    this.object = object;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((object == null) ? 0 : object.hashCode());
    result = prime * result + ((subject == null) ? 0 : subject.hashCode());
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
    final BlockedId other = (BlockedId) obj;
    if (object == null) {
      if (other.object != null) {
        return false;
      }
    } else if (!object.equals(other.object)) {
      return false;
    }
    if (subject == null) {
      if (other.subject != null) {
        return false;
      }
    } else if (!subject.equals(other.subject)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "BlockedId [subject=" + subject + ", object=" + object + "]";
  }

}
