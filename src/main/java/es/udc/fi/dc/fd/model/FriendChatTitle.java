package es.udc.fi.dc.fd.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;

import es.udc.fi.dc.fd.model.persistence.ImageImpl;
import es.udc.fi.dc.fd.model.persistence.MessageImpl;
import es.udc.fi.dc.fd.model.persistence.UserImpl;

@Entity
public class FriendChatTitle {

	private Long friendId;
	private String friendName;
	private String content;
	private Boolean sentByYou;
	private LocalDateTime date;
	

	public Long getFriendId() {
		return friendId;
	}


	public void setFriendId(Long friendId) {
		this.friendId = friendId;
	}


	public String getFriendName() {
		return friendName;
	}


	public void setFriendName(String friendName) {
		this.friendName = friendName;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public Boolean getSentByYou() {
		return sentByYou;
	}


	public void setSentByYou(Boolean sentByYou) {
		this.sentByYou = sentByYou;
	}


	public LocalDateTime getDate() {
		return date;
	}


	public void setDate(LocalDateTime date) {
		this.date = date;
	}
	
	


	public FriendChatTitle(Long friendId, String friendName, String content, Boolean sentByYou, LocalDateTime date) {
		super();
		this.friendId = friendId;
		this.friendName = friendName;
		this.content = content;
		this.sentByYou = sentByYou;
		this.date = date;
	}


	@Override
	public String toString() {
		return "FriendChatTitle [friendId=" + friendId + ", friendName=" + friendName + ", content=" + content
				+ ", sentByYou=" + sentByYou + ", date=" + date + "]";
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((friendId == null) ? 0 : friendId.hashCode());
		result = prime * result + ((friendName == null) ? 0 : friendName.hashCode());
		result = prime * result + ((sentByYou == null) ? 0 : sentByYou.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FriendChatTitle other = (FriendChatTitle) obj;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (friendId == null) {
			if (other.friendId != null)
				return false;
		} else if (!friendId.equals(other.friendId))
			return false;
		if (friendName == null) {
			if (other.friendName != null)
				return false;
		} else if (!friendName.equals(other.friendName))
			return false;
		if (sentByYou == null) {
			if (other.sentByYou != null)
				return false;
		} else if (!sentByYou.equals(other.sentByYou))
			return false;
		return true;
	}


	public FriendChatTitle() {
		super();
	}
}
