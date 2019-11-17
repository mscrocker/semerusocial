package es.udc.fi.dc.fd.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import es.udc.fi.dc.fd.model.persistence.UserImpl;

public interface Message extends Serializable {

	public Long getMessageId();

	public UserImpl getUser1();

	public UserImpl getUser2();

	public UserImpl getTransmitter();

	public String getMessageContent();

	public LocalDateTime getDate();

	public void setMessageId(Long messageId);

	public void setUser1(UserImpl user1);

	public void setUser2(UserImpl user2);

	public void setTransmitter(UserImpl transmitter);

	public void setMessageContent(String messageContent);

	public void setDate(LocalDateTime date);
}
