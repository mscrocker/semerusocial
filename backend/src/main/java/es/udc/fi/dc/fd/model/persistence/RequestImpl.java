package es.udc.fi.dc.fd.model.persistence;

import es.udc.fi.dc.fd.model.Request;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity(name = "Request")
@Table(name = "RequestTable")
public class RequestImpl implements Request {

  @Transient
  private static final long serialVersionUID = 871678321L;

  @EmbeddedId
  private RequestId requestId;

  @Column(name = "date", nullable = false)
  private LocalDateTime date;

  public RequestImpl() {
    super();
  }

  /**
   * Default constructor for the request entity.
   * @param requestId The IDs of the users involved in the request
   * @param date The date of the request
   */
  public RequestImpl(RequestId requestId, LocalDateTime date) {
    super();
    this.requestId = requestId;
    this.date = date;
  }

  @Override
  public RequestId getRequestId() {
    return requestId;
  }

  @Override
  public void setRequestId(RequestId requestId) {
    this.requestId = requestId;
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
    result = prime * result + (requestId == null ? 0 : requestId.hashCode());
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
    final RequestImpl other = (RequestImpl) obj;
    if (date == null) {
      if (other.date != null) {
        return false;
      }
    } else if (!date.equals(other.date)) {
      return false;
    }
    if (requestId == null) {
      if (other.requestId != null) {
        return false;
      }
    } else if (!requestId.equals(other.requestId)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "RequestImpl [requestId=" + requestId + ", date=" + date + "]";
  }

}
