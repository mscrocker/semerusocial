package es.udc.fi.dc.fd.dtos;

import java.io.Serializable;
import java.time.LocalDateTime;

public class MessageDetailsDto implements Serializable {

  private static final long serialVersionUID = 1328776989350853492L;

  private String messageContent;

  private LocalDateTime date;

  private Long owner;

  private Long receiver;

  public MessageDetailsDto() {
    super();
  }

  /**
   * Default constructor for the message DTO.
   * @param messageContent The content of the message
   * @param date The date of the message
   * @param owner The user who sent the message
   * @param receiver The user who received the message
   */
  public MessageDetailsDto(String messageContent, LocalDateTime date, Long owner, Long receiver) {
    super();
    this.messageContent = messageContent;
    this.date = date;
    this.owner = owner;
    this.receiver = receiver;
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

  public Long getOwner() {
    return owner;
  }

  public void setOwner(Long owner) {
    this.owner = owner;
  }

  public Long getReceiver() {
    return receiver;
  }

  public void setReceiver(Long receiver) {
    this.receiver = receiver;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((date == null) ? 0 : date.hashCode());
    result = prime * result + ((messageContent == null) ? 0 : messageContent.hashCode());
    result = prime * result + ((owner == null) ? 0 : owner.hashCode());
    result = prime * result + ((receiver == null) ? 0 : receiver.hashCode());
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
    final MessageDetailsDto other = (MessageDetailsDto) obj;
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
    if (owner == null) {
      if (other.owner != null) {
        return false;
      }
    } else if (!owner.equals(other.owner)) {
      return false;
    }
    if (receiver == null) {
      if (other.receiver != null) {
        return false;
      }
    } else if (!receiver.equals(other.receiver)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "MessageDetailsDto [messageContent=" + messageContent + ", date=" + date
        + ", owner=" + owner + ", receiver=" + receiver + "]";
  }

}
