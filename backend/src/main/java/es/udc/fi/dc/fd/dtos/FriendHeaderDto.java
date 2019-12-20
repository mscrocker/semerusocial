package es.udc.fi.dc.fd.dtos;

import java.time.LocalDateTime;

import es.udc.fi.dc.fd.model.FriendChatTitle;

public class FriendHeaderDto extends FriendChatTitle {

	public FriendHeaderDto() {
		super();
	}

	public FriendHeaderDto(Long friendId, String friendName, String content, Boolean sentByYou, LocalDateTime date) {
		super(friendId, friendName, content, sentByYou, date);
		
	}
	
}