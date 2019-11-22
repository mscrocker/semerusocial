package es.udc.fi.dc.fd.test.integration.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fi.dc.fd.controller.exception.InstanceNotFoundException;
import es.udc.fi.dc.fd.controller.exception.ItsNotYourFriendException;
import es.udc.fi.dc.fd.controller.exception.RequestParamException;
import es.udc.fi.dc.fd.controller.exception.ValidationException;
import es.udc.fi.dc.fd.dtos.MessageDetailsDto;
import es.udc.fi.dc.fd.model.FriendChatTitle;
import es.udc.fi.dc.fd.model.persistence.MatchId;
import es.udc.fi.dc.fd.model.persistence.MatchImpl;
import es.udc.fi.dc.fd.model.persistence.MessageImpl;
import es.udc.fi.dc.fd.model.persistence.UserImpl;
import es.udc.fi.dc.fd.repository.MatchRepository;
import es.udc.fi.dc.fd.repository.MessageRepository;
import es.udc.fi.dc.fd.repository.UserRepository;
import es.udc.fi.dc.fd.service.Block;
import es.udc.fi.dc.fd.service.ChatService;

@RunWith(JUnitPlatform.class)
@ExtendWith(SpringExtension.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:context/service.xml", "classpath:context/persistence.xml",
		"classpath:context/application-context.xml" })
@TestPropertySource({ "classpath:config/persistence-access.properties", "classpath:config/service.properties" })
@Rollback
@Transactional
public class ITChatService {
	@Autowired
	ChatService chatService;

	@Autowired
	private MatchRepository matchRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MessageRepository messageRepository;

	private LocalDateTime getDateTime(int day, int month, int year) {
		return LocalDateTime.of(year, month, day, 00, 01);
	}

	private LocalDateTime createMessage(UserImpl owner, UserImpl receiver, String content) {
		final LocalDateTime date = LocalDateTime.now();
		final MessageImpl msg = new MessageImpl();
		msg.setDate(date);
		msg.setMessageContent(content);
		msg.setTransmitter(owner);
		if (owner.getId() > receiver.getId()) {
			msg.setUser1(receiver);
			msg.setUser2(owner);
		} else {
			msg.setUser2(receiver);
			msg.setUser1(owner);
		}
		messageRepository.save(msg);

		return date;
	}

	private UserImpl createUser(String userName, String password, LocalDateTime date, String sex, String city,
			String description) {
		return new UserImpl(userName, password, date, sex, city, description);
	}

	private Optional<UserImpl> createUser(String userName) {
		final UserImpl user1 = createUser(userName, "pass", getDateTime(1, 1, 2000), "hombre", "coruna", "descripcion");
		userRepository.save(user1);
		return userRepository.findByUserName(userName);
	}

	@Test
	public void testSendMessage() throws InstanceNotFoundException, ItsNotYourFriendException, ValidationException {
		final String content = "mensaje";

		// Creamos los usuarios
		final Optional<UserImpl> user = createUser("user1");
		final Optional<UserImpl> friend = createUser("friend");

		// Los hacemos amigos
		final MatchId matchId = new MatchId(user.get().getId(), friend.get().getId());
		final MatchImpl match = new MatchImpl(matchId, LocalDateTime.now());
		matchRepository.save(match);

		// Usamos el servicio
		chatService.sendMessage(user.get().getId(), friend.get().getId(), content);

		// Asserts
		final List<MessageImpl> msg = messageRepository.findAll();
		assertNotNull(msg);
		final MessageImpl item = msg.get(0);
		assertEquals(item.getMessageContent(), content);
		assertEquals(item.getUser1(), item.getTransmitter());
		assertEquals(item.getUser1(), user.get());
		assertEquals(item.getUser2(), friend.get());
	}

	@Test
	public void testSendMessageFriendToUser()
			throws InstanceNotFoundException, ItsNotYourFriendException, ValidationException {
		final String content = "mensaje";

		// Creamos los usuarios
		final Optional<UserImpl> user = createUser("user1");
		final Optional<UserImpl> friend = createUser("friend");

		// Los hacemos amigos
		final MatchId matchId = new MatchId(user.get().getId(), friend.get().getId());
		final MatchImpl match = new MatchImpl(matchId, LocalDateTime.now());
		matchRepository.save(match);

		// Usamos el servicio
		chatService.sendMessage(friend.get().getId(), user.get().getId(), content);

		// Asserts
		final List<MessageImpl> msg = messageRepository.findAll();
		assertNotNull(msg);
		final MessageImpl item = msg.get(0);
		assertEquals(item.getMessageContent(), content);
		assertEquals(item.getUser2(), item.getTransmitter());
		assertEquals(item.getUser1(), user.get());
		assertEquals(item.getUser2(), friend.get());
	}

	@Test
	public void testSendMessageInstanceNotFoundException()
			throws InstanceNotFoundException, ItsNotYourFriendException, ValidationException {
		final String content = "mensaje";

		// Creamos los usuarios
		final Optional<UserImpl> user = createUser("user1");

		// Usamos el servicio
		assertThrows(InstanceNotFoundException.class, () -> {
			chatService.sendMessage(user.get().getId(), 900L, content);
		});
	}

	@Test
	public void testSendMessageInstanceNotFoundExceptionUserNotFound()
			throws InstanceNotFoundException, ItsNotYourFriendException, ValidationException {
		final String content = "mensaje";

		// Creamos los usuarios
		final Optional<UserImpl> user = createUser("user1");

		// Usamos el servicio
		assertThrows(InstanceNotFoundException.class, () -> {
			chatService.sendMessage(user.get().getId() + 1, 900L, content);
		});
	}

	@Test
	public void testSendMessageItsNotYourFriendException()
			throws InstanceNotFoundException, ItsNotYourFriendException, ValidationException {
		final String content = "mensaje";

		// Creamos los usuarios
		final Optional<UserImpl> user = createUser("user1");
		final Optional<UserImpl> friend = createUser("friend");

		// Usamos el servicio
		assertThrows(ItsNotYourFriendException.class, () -> {
			chatService.sendMessage(user.get().getId(), friend.get().getId(), content);
		});
	}

	@Test
	public void testSendMessageValidationExceptionUserNull()
			throws InstanceNotFoundException, ItsNotYourFriendException, ValidationException {
		final String content = "mensaje";

		// Creamos los usuarios
		final Optional<UserImpl> friend = createUser("friend");

		// Usamos el servicio
		assertThrows(ValidationException.class, () -> {
			chatService.sendMessage(null, friend.get().getId(), content);
		});
	}

	@Test
	public void testSendMessageValidationExceptionFriendNull()
			throws InstanceNotFoundException, ItsNotYourFriendException, ValidationException {
		final String content = "mensaje";

		// Creamos los usuarios
		final Optional<UserImpl> user = createUser("user");

		// Usamos el servicio
		assertThrows(ValidationException.class, () -> {
			chatService.sendMessage(user.get().getId(), null, content);
		});
	}

	@Test
	public void testSendMessageValidationExceptionMsgToYourself()
			throws InstanceNotFoundException, ItsNotYourFriendException, ValidationException {
		final String content = "mensaje";

		// Creamos los usuarios
		final Optional<UserImpl> user = createUser("user1");

		// Usamos el servicio
		assertThrows(ValidationException.class, () -> {
			chatService.sendMessage(user.get().getId(), user.get().getId(), content);
		});
	}

	@Test
	public void testSendMessageValidationExceptionContentNull()
			throws InstanceNotFoundException, ItsNotYourFriendException, ValidationException {
		final String content = null;

		// Creamos los usuarios
		final Optional<UserImpl> user = createUser("user1");
		final Optional<UserImpl> friend = createUser("friend");

		// Los hacemos amigos
		final MatchId matchId = new MatchId(user.get().getId(), friend.get().getId());
		final MatchImpl match = new MatchImpl(matchId, LocalDateTime.now());
		matchRepository.save(match);

		// Usamos el servicio
		assertThrows(ValidationException.class, () -> {
			chatService.sendMessage(user.get().getId(), friend.get().getId(), content);
		});
	}

	@Test
	public void testSendMessageValidationExceptionContentBlank()
			throws InstanceNotFoundException, ItsNotYourFriendException, ValidationException {
		final String content = "   ";

		// Creamos los usuarios
		final Optional<UserImpl> user = createUser("user1");
		final Optional<UserImpl> friend = createUser("friend");

		// Los hacemos amigos
		final MatchId matchId = new MatchId(user.get().getId(), friend.get().getId());
		final MatchImpl match = new MatchImpl(matchId, LocalDateTime.now());
		matchRepository.save(match);

		// Usamos el servicio
		assertThrows(ValidationException.class, () -> {
			chatService.sendMessage(user.get().getId(), friend.get().getId(), content);
		});
	}

	@Test
	public void testSendMessageValidationExceptionContentTooLarge()
			throws InstanceNotFoundException, ItsNotYourFriendException, ValidationException {

		final String content = StringUtils.repeat("a", 1000);

		// Creamos los usuarios
		final Optional<UserImpl> user = createUser("user1");
		final Optional<UserImpl> friend = createUser("friend");

		// Los hacemos amigos
		final MatchId matchId = new MatchId(user.get().getId(), friend.get().getId());
		final MatchImpl match = new MatchImpl(matchId, LocalDateTime.now());
		matchRepository.save(match);

		// Usamos el servicio
		assertThrows(ValidationException.class, () -> {
			chatService.sendMessage(user.get().getId(), friend.get().getId(), content);
		});
	}

	/***********
	 * TESTS GET CONVERSATION
	 *************************************************/

	@Test
	public void testGetConversation() throws InstanceNotFoundException, ItsNotYourFriendException, ValidationException {

		// Creamos los usuarios
		final Optional<UserImpl> user = createUser("user1");
		final Optional<UserImpl> friend = createUser("friend");

		// Los hacemos amigos
		final MatchId matchId = new MatchId(user.get().getId(), friend.get().getId());
		final MatchImpl match = new MatchImpl(matchId, LocalDateTime.now());
		matchRepository.save(match);

		// Enviamos "msgCant" mensajes
		final int msgCant = 10;
		for (int i = 0; i < msgCant; i++) {
			createMessage(user.get(), friend.get(), Integer.toString(i));
		}

		// Probamos el servicio
		final int size = msgCant - 2;
		final Block<MessageDetailsDto> conversation = chatService.getConversation(user.get().getId(),
				friend.get().getId(), 0, size);

		assertFalse(conversation.getElements().isEmpty());
		assertTrue(conversation.isExistMoreElements());
		assertEquals(conversation.getElements().size(), size);
		assertEquals(conversation.getElements().get(0).getMessageContent(), Integer.toString(9));
	}

	@Test
	public void testGetConversationInstanceNotFoundExceptionUser()
			throws InstanceNotFoundException, ItsNotYourFriendException, ValidationException {

		// Creamos un usuario
		final Optional<UserImpl> friend = createUser("friend");

		// Probamos el servicio
		assertThrows(InstanceNotFoundException.class, () -> {
			chatService.getConversation(null, friend.get().getId(), 0, 10);
		});
	}

	@Test
	public void testGetConversationInstanceNotFoundExceptionFriend()
			throws InstanceNotFoundException, ItsNotYourFriendException, ValidationException {

		// Creamos un usuario
		final Optional<UserImpl> user = createUser("user1");

		// Probamos el servicio
		assertThrows(InstanceNotFoundException.class, () -> {
			chatService.getConversation(null, user.get().getId(), 0, 10);
		});
	}

	@Test
	public void testGetConversationItsNotYourFriendException()
			throws InstanceNotFoundException, ItsNotYourFriendException, ValidationException {
		/*
		 * 4. Usamos el servicio
		 */
		// Creamos los usuarios
		final Optional<UserImpl> user = createUser("user1");
		final Optional<UserImpl> friend = createUser("friend");

		// Probamos el servicio
		assertThrows(ItsNotYourFriendException.class, () -> {
			chatService.getConversation(user.get().getId(), friend.get().getId(), 0, 10);
		});
	}

	@Test
	public void testGetConversationValidationException()
			throws InstanceNotFoundException, ItsNotYourFriendException, ValidationException {
		// Intentamos obtener la conversación con nosotros mismos

		// Creamos los usuarios
		final Optional<UserImpl> user = createUser("user1");

		// Probamos el servicio
		assertThrows(ValidationException.class, () -> {
			chatService.getConversation(user.get().getId(), user.get().getId(), 0, 10);
		});
	}

	@Test
	public void testGetConversationFriendToUser()
			throws InstanceNotFoundException, ItsNotYourFriendException, ValidationException {
		/*
		 * 4. Usamos el servicio
		 */
		// Creamos los usuarios
		final Optional<UserImpl> user = createUser("user1");
		final Optional<UserImpl> friend = createUser("friend");

		// Los hacemos amigos
		final MatchId matchId = new MatchId(user.get().getId(), friend.get().getId());
		final MatchImpl match = new MatchImpl(matchId, LocalDateTime.now());
		matchRepository.save(match);

		// Enviamos "msgCant" mensajes
		final int msgCant = 10;
		for (int i = 0; i < msgCant; i++) {
			createMessage(user.get(), friend.get(), Integer.toString(i));
		}

		// Probamos el servicio
		final int size = msgCant - 2;
		final Block<MessageDetailsDto> conversation = chatService.getConversation(friend.get().getId(),
				user.get().getId(), 0, size);

		assertFalse(conversation.getElements().isEmpty());
		assertTrue(conversation.isExistMoreElements());
		assertEquals(conversation.getElements().size(), size);
		assertEquals(conversation.getElements().get(0).getMessageContent(), Integer.toString(9));
	}

	/********
	 * TESTS GET USER CONVERSATIONS
	 *
	 * @throws RequestParamException
	 * @throws InstanceNotFoundException
	 ***********************************************************************/

	@Test
	public void testFindMessageHistory() throws InstanceNotFoundException, RequestParamException {

		final UserImpl user = createUser("user1").get();
		final UserImpl friend = createUser("friend").get();
		final MatchId matchId = new MatchId(user.getId(), friend.getId());
		final MatchImpl match = new MatchImpl(matchId, LocalDateTime.now());
		matchRepository.save(match);

		int i = 0;
		for (; i < 10; i++) {
			final MessageImpl message = new MessageImpl();
			message.setUser1(user);
			message.setUser2(friend);
			message.setTransmitter(friend);
			message.setDate(LocalDateTime.now().plusHours(i));
			message.setMessageContent("message" + i);
			messageRepository.save(message);

		}

		final String expectedResult = "message" + (i - 1);

		final Block<FriendChatTitle> result = chatService.getUserConversations(user.getId(), 0);
		final String resultProcessed = result.getElements().get(0).getContent();

		assertEquals(expectedResult, resultProcessed);
	}

	@Test
	public void testFindMessageHistoryRequestParamException() throws InstanceNotFoundException, RequestParamException {

		final UserImpl user = createUser("user1").get();
		final UserImpl friend = createUser("friend").get();
		final MatchId matchId = new MatchId(user.getId(), friend.getId());
		final MatchImpl match = new MatchImpl(matchId, LocalDateTime.now());
		matchRepository.save(match);

		int i = 0;
		for (; i < 10; i++) {
			final MessageImpl message = new MessageImpl();
			message.setUser1(user);
			message.setUser2(friend);
			message.setTransmitter(friend);
			message.setDate(LocalDateTime.now().plusHours(i));
			message.setMessageContent("message" + i);
			messageRepository.save(message);

		}

		assertThrows(RequestParamException.class, () -> {
			chatService.getUserConversations(user.getId(), -1);
		});
	}

}
