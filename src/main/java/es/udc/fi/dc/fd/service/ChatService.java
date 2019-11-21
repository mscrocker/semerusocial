package es.udc.fi.dc.fd.service;

import java.time.LocalDateTime;

import es.udc.fi.dc.fd.controller.exception.InstanceNotFoundException;
import es.udc.fi.dc.fd.controller.exception.NotYourFriendException;
import es.udc.fi.dc.fd.controller.exception.RequestParamException;
import es.udc.fi.dc.fd.controller.exception.ValidationException;
import es.udc.fi.dc.fd.dtos.MessageDetailsDto;
import es.udc.fi.dc.fd.model.FriendChatTitle;
import es.udc.fi.dc.fd.model.persistence.MessageImpl;

public interface ChatService {
	
	public void sendMessage(Long userId, Long friendId, String content)
			throws InstanceNotFoundException, NotYourFriendException, ValidationException;

	public Block<MessageDetailsDto> getConversation(Long userId, Long friendId, int page, int size)
			throws InstanceNotFoundException, NotYourFriendException, ValidationException;

	public Block<FriendChatTitle> getUserConversations(Long userId, int page) 
			throws RequestParamException, InstanceNotFoundException;
	
}
