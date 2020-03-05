package es.udc.fi.dc.fd.model.persistence;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity(name = "Message")
@Table(name = "MessageTable")
public class MessageImpl implements Serializable {

  @Transient
  private static final long serialVersionUID = 7235476221L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "messageId", nullable = false, unique = true)
  private Long messageId;

  @ManyToOne(optional = false, fetch = FetchType.EAGER)
  @JoinColumn(name = "user1", referencedColumnName = "id")
  private UserImpl user1;

  @ManyToOne(optional = false, fetch = FetchType.EAGER)
  @JoinColumn(name = "user2", referencedColumnName = "id")
  private UserImpl user2;

  @ManyToOne(optional = false, fetch = FetchType.EAGER)
  @JoinColumn(name = "transmitter", referencedColumnName = "id")
  private UserImpl transmitter;

  @Column(name = "messageContent", nullable = false)
  private String messageContent;

  @Column(name = "date", nullable = false)
  private LocalDateTime date;

  public MessageImpl() {
    super();
  }

  /**
   * Default constructor for the message entity.
   *
   * @param messageId      The id of the message
   * @param user1          The first user involved in the message
   * @param user2          The second user involved in the message
   * @param transmitter    ID of the user who sent the message (either user1 or user2)
   * @param messageContent The content of the message
   * @param date           The date of the message
   */
  public MessageImpl(Long messageId, UserImpl user1, UserImpl user2, UserImpl transmitter,
                     String messageContent, LocalDateTime date) {
    super();
    this.messageId = messageId;
    this.user1 = user1;
    this.user2 = user2;
    this.transmitter = transmitter;
    this.messageContent = messageContent;
    this.date = date;
  }

  public Long getMessageId() {
    return messageId;
  }

  public void setMessageId(Long messageId) {
    this.messageId = messageId;
  }

  public UserImpl getUser1() {
    return user1;
  }

  public void setUser1(UserImpl user1) {
    this.user1 = user1;
  }

  public UserImpl getUser2() {
    return user2;
  }

  public void setUser2(UserImpl user2) {
    this.user2 = user2;
  }

  public UserImpl getTransmitter() {
    return transmitter;
  }

  public void setTransmitter(UserImpl transmitter) {
    this.transmitter = transmitter;
  }

  public String getMessageContent() {
    return messageContent;
  }

  public void setMessageContent(String messageContent) {
    this.messageContent = messageContent;
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
    result = prime * result + ((date == null) ? 0 : date.hashCode());
    result = prime * result + ((messageContent == null) ? 0 : messageContent.hashCode());
    result = prime * result + ((messageId == null) ? 0 : messageId.hashCode());
    result = prime * result + ((transmitter == null) ? 0 : transmitter.hashCode());
    result = prime * result + ((user1 == null) ? 0 : user1.hashCode());
    result = prime * result + ((user2 == null) ? 0 : user2.hashCode());
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
    final MessageImpl other = (MessageImpl) obj;
    if (date == null) {
      if (other.date != null) {
        return false;
      }
    } else if (!date.equals(other.date)) {
      return false;
    }
    if (messageContent == null) {
      if (other.messageContent != null) {
        return false;
      }
    } else if (!messageContent.equals(other.messageContent)) {
      return false;
    }
    if (messageId == null) {
      if (other.messageId != null) {
        return false;
      }
    } else if (!messageId.equals(other.messageId)) {
      return false;
    }
    if (transmitter == null) {
      if (other.transmitter != null) {
        return false;
      }
    } else if (!transmitter.equals(other.transmitter)) {
      return false;
    }
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
    return "MessageImpl [messageId=" + messageId + ", user1=" + user1 + ", user2=" + user2
        + ", transmitter=" + transmitter + ", messageContent=" + messageContent + ", date=" + date
        + "]";
  }

}
