package es.udc.fi.dc.fd.service;

import es.udc.fi.dc.fd.controller.exception.InstanceNotFoundException;
import es.udc.fi.dc.fd.controller.exception.ItsNotYourFriendException;
import es.udc.fi.dc.fd.controller.exception.RequestParamException;
import es.udc.fi.dc.fd.controller.exception.ValidationException;
import es.udc.fi.dc.fd.dtos.MessageDetailsDto;
import es.udc.fi.dc.fd.model.FriendChatTitle;

public interface ChatService {

	public void sendMessage(Long userId, Long friendId, String content)
			throws InstanceNotFoundException, ItsNotYourFriendException, ValidationException;

	public Block<MessageDetailsDto> getConversation(Long userId, Long friendId, int page, int size)
			throws InstanceNotFoundException, ItsNotYourFriendException, ValidationException;

	public Block<FriendChatTitle> getUserConversations(Long userId, int page)
			throws RequestParamException, InstanceNotFoundException;

}
