package es.udc.fi.dc.fd.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fi.dc.fd.controller.exception.InstanceNotFoundException;
import es.udc.fi.dc.fd.controller.exception.ItsNotYourFriendException;
import es.udc.fi.dc.fd.controller.exception.RequestParamException;
import es.udc.fi.dc.fd.controller.exception.ValidationException;
import es.udc.fi.dc.fd.dtos.MessageConversor;
import es.udc.fi.dc.fd.dtos.MessageDetailsDto;
import es.udc.fi.dc.fd.model.FriendChatTitle;
import es.udc.fi.dc.fd.model.persistence.MessageImpl;
import es.udc.fi.dc.fd.model.persistence.UserImpl;
import es.udc.fi.dc.fd.repository.MatchRepository;
import es.udc.fi.dc.fd.repository.MessageRepository;
import es.udc.fi.dc.fd.repository.UserRepository;

@Service
@Transactional
public class ChatServiceImpl implements ChatService {

	@Autowired
	private MessageRepository messageRepository;

	@Autowired
	private MatchRepository matchRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PermissionChecker permissionChecker;

	private static final int MAX_LENGTH_MESSAGE = 999;

	@Override
	public void sendMessage(Long userId, Long friendId, String content)
			throws InstanceNotFoundException, ItsNotYourFriendException, ValidationException {

		// Comprobamos que los ids no son nulos
		if (userId == null || friendId == null) {
			throw new ValidationException("Ids can not be null");
		}
		//Comprobamos que el mensaje no es nulo
		if (content==null) {
			throw new ValidationException("Message content can not be null");
		}
		//Validamos que el mensaje no se pase del largo permitido
		if (content.length() > MAX_LENGTH_MESSAGE || content.trim().length() == 0) {
			throw new ValidationException(
					"Message length too large or blank. It must be less than " + MAX_LENGTH_MESSAGE);
		}

		// Comprobamos que no te estás intentando mandar un mensaje a ti mismo
		if (userId.equals(friendId)) {
			throw new ValidationException("You can not send a message to yourself");
		}
		// Comprobamos que los usuarios existen
		final Optional<UserImpl> user = userRepository.findById(userId);
		if (user.isEmpty()) {
			throw new InstanceNotFoundException(UserImpl.class.getName(), userId);
		}
		final Optional<UserImpl> friend = userRepository.findById(friendId);
		if (friend.isEmpty()) {
			throw new InstanceNotFoundException(UserImpl.class.getName(), friendId);
		}

		//Comprobamos que sean amigos
		if ((matchRepository.findMatch(userId, friendId)).isEmpty()
				&& (matchRepository.findMatch(friendId, userId)).isEmpty()) {
			throw new ItsNotYourFriendException("User with id " + friendId + " is not your friend.");
		}


		//Podemos almacenar el mensaje
		final MessageImpl msg = new MessageImpl();
		msg.setDate(LocalDateTime.now());
		msg.setMessageContent(content);
		if (userId < friendId) {
			msg.setUser1(user.get());
			msg.setUser2(friend.get());
		} else {
			msg.setUser2(user.get());
			msg.setUser1(friend.get());
		}
		msg.setTransmitter(user.get());
		messageRepository.save(msg);
	}

	@Override
	public Block<MessageDetailsDto> getConversation(Long userId, Long friendId, int page, int size)
			throws InstanceNotFoundException, ItsNotYourFriendException, ValidationException {
		if (userId == null) {
			throw new InstanceNotFoundException(UserImpl.class.getName(), userId);
		}

		if (userId.equals(friendId)) {
			throw new ValidationException("You can not get a conversation with yourself");
		}

		// Comprobamos que sean amigos
		if ((matchRepository.findMatch(userId, friendId)).isEmpty()
				&& (matchRepository.findMatch(friendId, userId)).isEmpty()) {
			throw new ItsNotYourFriendException("User with id " + friendId + " is not your friend.");
		}

		// En BD estamos almacenando 1º el id más pequeño
		Slice<MessageImpl> conversation;
		if (userId<friendId) {
			conversation = messageRepository.findMessagesByUsersId(userId, friendId, PageRequest.of(page, size));
		}else {
			conversation = messageRepository.findMessagesByUsersId(friendId, userId, PageRequest.of(page, size));
		}

		// Convertimos a dto
		final List<MessageDetailsDto> items = new ArrayList<>();
		for (final MessageImpl messageImpl : conversation) {
			items.add(MessageConversor.messageToMessageDetailsDto(messageImpl));
		}
		return new Block<>(items, conversation.hasNext());

	}


	@Override
	public Block<FriendChatTitle> getUserConversations(Long userId, int page) throws RequestParamException, InstanceNotFoundException {
		permissionChecker.checkUserExists(userId);
		if (page < 0) {
			throw new RequestParamException("Page must be at less 0, you have passed as page=" + page);
		}

		final Slice<FriendChatTitle> result = messageRepository.getLatestConversations(userId, page, 10);
		return new Block<>(result.getContent(), result.hasNext());
	}

}
