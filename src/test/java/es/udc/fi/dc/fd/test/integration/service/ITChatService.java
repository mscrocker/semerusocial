package es.udc.fi.dc.fd.test.integration.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
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
import es.udc.fi.dc.fd.controller.exception.NotYourFriendException;
import es.udc.fi.dc.fd.controller.exception.RequestParamException;
import es.udc.fi.dc.fd.controller.exception.ValidationException;
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
	public void testSendMessage() throws InstanceNotFoundException, NotYourFriendException, ValidationException {
		final String content = "mensaje";

		//Creamos los usuarios
		final Optional<UserImpl> user = createUser("user1");
		final Optional<UserImpl> friend = createUser("friend");

		//Los hacemos amigos
		final MatchId matchId = new MatchId(user.get().getId(), friend.get().getId());
		final MatchImpl match = new MatchImpl(matchId, LocalDateTime.now());
		matchRepository.save(match);

		//Usamos el servicio
		chatService.sendMessage(user.get().getId(), friend.get().getId(), content);

		//Asserts
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
			throws InstanceNotFoundException, NotYourFriendException, ValidationException {
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
			throws InstanceNotFoundException, NotYourFriendException, ValidationException {
		final String content = "mensaje";

		// Creamos los usuarios
		final Optional<UserImpl> user = createUser("user1");

		// Usamos el servicio
		assertThrows(InstanceNotFoundException.class, () -> {
			chatService.sendMessage(user.get().getId(), 900L, content);
		});
	}

	@Test
	public void testSendMessageNotYourFriendException()
			throws InstanceNotFoundException, NotYourFriendException, ValidationException {
		final String content = "mensaje";

		// Creamos los usuarios
		final Optional<UserImpl> user = createUser("user1");
		final Optional<UserImpl> friend = createUser("friend");

		// Usamos el servicio
		assertThrows(NotYourFriendException.class, () -> {
			chatService.sendMessage(user.get().getId(), friend.get().getId(), content);
		});
	}

	@Test
	public void testSendMessageValidationExceptionUserNull()
			throws InstanceNotFoundException, NotYourFriendException, ValidationException {
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
			throws InstanceNotFoundException, NotYourFriendException, ValidationException {
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
			throws InstanceNotFoundException, NotYourFriendException, ValidationException {
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
			throws InstanceNotFoundException, NotYourFriendException, ValidationException {
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
			throws InstanceNotFoundException, NotYourFriendException, ValidationException {
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
	public void testFindMessageHistory()
			throws InstanceNotFoundException, NotYourFriendException, ValidationException, RequestParamException {
		
		final UserImpl user = createUser("user1").get();
		final UserImpl friend = createUser("friend").get();
		final MatchId matchId = new MatchId(user.getId(), friend.getId());
		final MatchImpl match = new MatchImpl(matchId, LocalDateTime.now());
		matchRepository.save(match);
		
		int i = 0;
		for (; i < 10; i++) {
			MessageImpl message = new MessageImpl();
			message.setUser1(user);
			message.setUser2(friend);
			message.setTransmitter(friend);
			message.setDate(LocalDateTime.now().plusHours((long)i));
			message.setMessageContent("message" + i);
			messageRepository.save(message);
			
		}
		
		String expectedResult = "message" + (i-1);

		
		Block<FriendChatTitle> result = chatService.getUserConversations(user.getId(), 0);
		String resultProcessed = result.getElements().get(0).getContent();
		
		assertEquals(expectedResult, resultProcessed);
	}
	
}
