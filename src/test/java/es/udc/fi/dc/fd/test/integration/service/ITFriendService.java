package es.udc.fi.dc.fd.test.integration.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

import es.udc.fi.dc.fd.controller.exception.AlreadyAceptedException;
import es.udc.fi.dc.fd.controller.exception.AlreadyRejectedException;
import es.udc.fi.dc.fd.controller.exception.DuplicateInstanceException;
import es.udc.fi.dc.fd.controller.exception.InstanceNotFoundException;
import es.udc.fi.dc.fd.controller.exception.InvalidAgeException;
import es.udc.fi.dc.fd.controller.exception.InvalidDateException;
import es.udc.fi.dc.fd.controller.exception.InvalidRecommendationException;
import es.udc.fi.dc.fd.controller.exception.NotYourFriendException;
import es.udc.fi.dc.fd.controller.exception.RequestParamException;
import es.udc.fi.dc.fd.controller.exception.ValidationException;
import es.udc.fi.dc.fd.model.SexCriteriaEnum;
import es.udc.fi.dc.fd.model.persistence.MatchId;
import es.udc.fi.dc.fd.model.persistence.MatchImpl;
import es.udc.fi.dc.fd.model.persistence.MessageImpl;
import es.udc.fi.dc.fd.model.persistence.RejectedId;
import es.udc.fi.dc.fd.model.persistence.RejectedImpl;
import es.udc.fi.dc.fd.model.persistence.RequestId;
import es.udc.fi.dc.fd.model.persistence.RequestImpl;
import es.udc.fi.dc.fd.model.persistence.SearchCriteria;
import es.udc.fi.dc.fd.model.persistence.UserImpl;
import es.udc.fi.dc.fd.repository.MatchRepository;
import es.udc.fi.dc.fd.repository.MessageRepository;
import es.udc.fi.dc.fd.repository.RejectedRepository;
import es.udc.fi.dc.fd.repository.RequestRepository;
import es.udc.fi.dc.fd.repository.UserRepository;
import es.udc.fi.dc.fd.service.BlockFriendList;
import es.udc.fi.dc.fd.service.FriendService;
import es.udc.fi.dc.fd.service.UserService;

@RunWith(JUnitPlatform.class)
@ExtendWith(SpringExtension.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:context/service.xml", "classpath:context/persistence.xml",
"classpath:context/application-context.xml" })
@TestPropertySource({ "classpath:config/persistence-access.properties", "classpath:config/service.properties" })
@Rollback
@Transactional
public class ITFriendService {

	@Autowired
	private UserService userService;

	@Autowired
	private FriendService friendService;
	@Autowired
	private RequestRepository requestRepository;

	@Autowired
	private RejectedRepository rejectedRepository;
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MatchRepository matchRepository;

	@Autowired
	private MessageRepository messageRepository;

	@Autowired
	public ITFriendService() {
		super();
	}

	private UserImpl createUser(String userName, String password, LocalDateTime date, String sex, String city,
			String description) {
		return new UserImpl(userName, password, date, sex, city, description);
	}

	private LocalDateTime getDateTime(int day, int month, int year) {
		return LocalDateTime.of(year, month, day, 00, 01);
	}

	private LocalDateTime getDateTimeFromAge(int age) {
		assert age > 0;

		return LocalDate.now().minusYears(age).atStartOfDay();

	}

	private UserImpl signUp(String userName, String password, int age, String sex, String city) {

		final UserImpl user = createUser(userName, password, getDateTimeFromAge(age), sex, city, "descripcion");

		try {
			userService.signUp(user);
		} catch (DuplicateInstanceException | InvalidDateException e) {
			throw new RuntimeException(e);
		}

		return user;

	}

	private void setSearchCriteria(Long id, String genre, int minAge, int maxAge, String... cities) {
		try {
			userService.setSearchCriteria(id,
					new SearchCriteria(SexCriteriaEnum.fromCode(genre), minAge, maxAge, Arrays.asList(cities)));
		} catch (InstanceNotFoundException | InvalidAgeException e) {
			e.printStackTrace();
			fail();

		}
	}

	// -----addImage-----

	@Test
	public void testDefaultCriteriaRequest()
			throws InstanceNotFoundException, InvalidRecommendationException, AlreadyRejectedException,
			AlreadyAceptedException {
		final UserImpl user1 = signUp("manolo3", "pass", 22, "Male", "Catalunya");
		final UserImpl user2 = signUp("manolo4", "pass2", 23, "Female", "Catalunya");
		friendService.acceptRecommendation(user1.getId(), user2.getId());
		// assertTrue(requestRepository.count() == 1);
		final Optional<RequestImpl> opt = requestRepository.findById(new RequestId(user1.getId(), user2.getId()));

		assertTrue(opt.isPresent());
		assertEquals(opt.get().getRequestId().getObject(), user2.getId());
		assertEquals(opt.get().getRequestId().getSubject(), user1.getId());

	}

	@Test
	public void testInvalidCriteriaRequest()
			throws InstanceNotFoundException, InvalidRecommendationException, AlreadyRejectedException,
			AlreadyAceptedException {
		final UserImpl user1 = signUp("manolo5", "pass", 22, "Male", "Catalunya");
		// user1.setCriteriaMaxAge(50);
		setSearchCriteria(user1.getId(), "Female", 18, 50, "Catalunya");
		// userRepository.save(user1);
		final UserImpl user2 = signUp("manolo6", "pass2", 102, "Female", "Catalunya");

		assertThrows(InvalidRecommendationException.class, () -> {
			friendService.acceptRecommendation(user1.getId(), user2.getId());
		});

		user2.setDate(getDateTimeFromAge(45));
		user2.setCity("Espanya");
		userRepository.save(user2);

		assertThrows(InvalidRecommendationException.class, () -> {
			friendService.acceptRecommendation(user1.getId(), user2.getId());
		});
		user2.setCity("Catalunya");
		userRepository.save(user2);

		friendService.acceptRecommendation(user1.getId(), user2.getId());
		// assertTrue(requestRepository.count() == 1);

	}

	@Test
	public void testReject()
			throws InstanceNotFoundException, InvalidRecommendationException, AlreadyRejectedException,
			AlreadyAceptedException {
		final UserImpl user1 = signUp("manolo7", "pass", 22, "Male", "Catalunya");
		final UserImpl user2 = signUp("manolo8", "pass2", 23, "Female", "Catalunya");
		friendService.rejectRecommendation(user1.getId(), user2.getId());
		// assertTrue(rejectedRepository.count() == 1);
		final Optional<RejectedImpl> opt = rejectedRepository.findById(new RejectedId(user1.getId(), user2.getId()));

		assertTrue(opt.isPresent());
		assertEquals(opt.get().getRejectedId().getObject(), user2.getId());
		assertEquals(opt.get().getRejectedId().getSubject(), user1.getId());
	}

	@Test
	public void testRejectAlreadyRejected()
			throws InstanceNotFoundException, InvalidRecommendationException, AlreadyRejectedException,
			AlreadyAceptedException {
		final UserImpl user1 = signUp("manolo9", "pass", 22, "Male", "Catalunya");
		userRepository.save(user1);

		final UserImpl user2 = signUp("manolo10", "pass2", 23, "Female", "Catalunya");
		friendService.rejectRecommendation(user1.getId(), user2.getId());
		assertThrows(AlreadyRejectedException.class, () -> {
			friendService.rejectRecommendation(user1.getId(), user2.getId());
		});
		assertThrows(AlreadyRejectedException.class, () -> {
			friendService.acceptRecommendation(user1.getId(), user2.getId());
		});
	}

	@Test
	public void testRejectAlreadyAcepted() throws InstanceNotFoundException, InvalidRecommendationException,
	AlreadyRejectedException, AlreadyAceptedException {
		final UserImpl user1 = signUp("manolo17", "pass", 22, "Male", "Catalunya");
		userRepository.save(user1);

		final UserImpl user2 = signUp("manolo18", "pass2", 23, "Female", "Catalunya");
		friendService.acceptRecommendation(user1.getId(), user2.getId());
		assertThrows(AlreadyAceptedException.class, () -> {
			friendService.rejectRecommendation(user1.getId(), user2.getId());
		});
		assertThrows(AlreadyAceptedException.class, () -> {
			friendService.acceptRecommendation(user1.getId(), user2.getId());
		});
	}

	@Test
	public void testInvalidCriteriaReject()
			throws InstanceNotFoundException, InvalidRecommendationException, AlreadyRejectedException,
			AlreadyAceptedException {
		final UserImpl user1 = signUp("manolo11", "pass", 22, "Male", "Catalunya");
		// user1.setCriteriaMaxAge(99);
		setSearchCriteria(user1.getId(), "Female", 18, 99, "Catalunya");
		// userRepository.save(user1);

		final UserImpl user2 = signUp("manolo12", "pass2", 102, "Female", "Catalunya");

		assertThrows(InvalidRecommendationException.class, () -> {
			friendService.rejectRecommendation(user1.getId(), user2.getId());
		});
		user2.setDate(getDateTimeFromAge(35));
		user2.setCity("Espanya");
		userRepository.save(user2);

		assertThrows(InvalidRecommendationException.class, () -> {
			friendService.rejectRecommendation(user1.getId(), user2.getId());
		});
		user2.setCity("Catalunya");
		userRepository.save(user2);

		friendService.rejectRecommendation(user1.getId(), user2.getId());
		// assertTrue(rejectedRepository.count() == 1);

	}

	@Test
	public void MatchTest() throws InstanceNotFoundException, InvalidRecommendationException, AlreadyRejectedException,
	AlreadyAceptedException {
		final UserImpl user1 = signUp("manolo13", "pass", 22, "Male", "Catalunya");

		userRepository.save(user1);

		final UserImpl user2 = signUp("manolo14", "pass2", 44, "Female", "Catalunya");
		userRepository.save(user2);

		friendService.acceptRecommendation(user1.getId(), user2.getId());
		friendService.acceptRecommendation(user2.getId(), user1.getId());
		// assertTrue(requestRepository.count() == 0);
		// assertTrue(matchRepository.count() == 1);
		final Long firstId = Math.min(user1.getId(), user2.getId());
		final Long secondId = user1.getId().equals(firstId) ? user2.getId() : user1.getId();

		final Optional<MatchImpl> opt = matchRepository.findById(new MatchId(firstId, secondId));
		assertTrue(opt.isPresent());
		final MatchImpl matchImpl = opt.get();
		final MatchId matchId = matchImpl.getMatchId();
		assertEquals(matchId.getUser1(), firstId);
		assertEquals(matchId.getUser2(), secondId);
	}

	@Test
	public void TestINFE() throws InstanceNotFoundException, InvalidRecommendationException, AlreadyRejectedException {
		final UserImpl user1 = signUp("manolo15", "pass", 22, "Male", "Catalunya");
		user1.setCriteriaMaxAge(99);
		user1.setCriteriaMinAge(18);
		user1.setCriteriaSex(SexCriteriaEnum.ANY);

		userRepository.save(user1);

		signUp("manolo16", "pass2", 44, "Female", "Catalunya");
		assertThrows(InstanceNotFoundException.class, () -> {
			friendService.acceptRecommendation(user1.getId(), -1L);
		});
		assertThrows(InstanceNotFoundException.class, () -> {
			friendService.acceptRecommendation(-1L, user1.getId());
		});
		assertThrows(InstanceNotFoundException.class, () -> {
			friendService.acceptRecommendation(-1L, -1L);
		});
	}

	/******* SUGGEST FRIEND TESTS *************************************/
	@Test

	//	@Sql(scripts = "/initialData.sql")
	public void TestSuggestFriend() throws InstanceNotFoundException {
		/*
		 * User id=1: CriteriaSex = Female CriteriaMinAge = "18" CriteriaMaxAge = "99"
		 * CitiesCriteria //TODO
		 */

		final UserImpl user1 = signUp("testSuggestFriend", "pass", 22, "Male", "osaka");
		final UserImpl user2 = signUp("testSuggestFriend2", "pass", 22, "Male", "osaka");
		setSearchCriteria(user1.getId(), "Male", 18, 45, "osaka");

		// With default matches
		Optional<UserImpl> userSuggested = friendService.suggestFriend(user1.getId());

		assertTrue(userSuggested.isPresent());
		assertEquals(userSuggested.get().getUserName(), user2.getUserName());

		// MaxAge under the target -> fails
		user1.setCriteriaMaxAge(21);
		userRepository.save(user1);
		userSuggested = friendService.suggestFriend(user1.getId());
		assertTrue(userSuggested.isEmpty());

		// Genre doesnt match -> fails
		user1.setCriteriaSex(SexCriteriaEnum.FEMALE);
		user1.setCriteriaMaxAge(25);
		userRepository.save(user1);

		userSuggested = friendService.suggestFriend(user1.getId());
		assertTrue(userSuggested.isEmpty());

		// Genre does match -> success
		user1.setCriteriaSex(SexCriteriaEnum.MALE);
		userRepository.save(user1);

		userSuggested = friendService.suggestFriend(user1.getId());
		assertTrue(userSuggested.isPresent());
		assertEquals(userSuggested.get().getUserName(), user2.getUserName());

		// Genre does match -> success
		user1.setCriteriaSex(SexCriteriaEnum.ANY);
		userRepository.save(user1);

		userSuggested = friendService.suggestFriend(user1.getId());
		assertTrue(userSuggested.isPresent());
		assertEquals(userSuggested.get().getUserName(), user2.getUserName());

		// City doesnt match -> fail
		setSearchCriteria(user1.getId(), "Female", 19, 28, "Los Angeles", "Rabat");

		userSuggested = friendService.suggestFriend(user1.getId());
		assertTrue(userSuggested.isEmpty());

		// City
		setSearchCriteria(user1.getId(), "Female", 19, 28, "Osaka");
		userSuggested = friendService.suggestFriend(user1.getId());
		assertTrue(userSuggested.isEmpty());

		// Ignores casing on the city
		setSearchCriteria(user1.getId(), "Male", 18, 45, "OSAKA");
		userSuggested = friendService.suggestFriend(user1.getId());

		assertTrue(userSuggested.isPresent());
		assertEquals(userSuggested.get().getUserName(), user2.getUserName());

		// TODO -> Ajustar fechas usuarios a hoy

		// TODO: PROBARLO A FONDO CAMBIANDO LA CRITERIA:
		// -Si CriteriaSex = ANY -> 3L
		// -Si CriteriaSex = OTHER -> 6L
		// -Si CriteriaSex = MALE -> null

		// - Si CritSex= ANY y edad entre -- y -- (30 años) -> 7L
		// - Si edad > 100 -> null

		// TODO -> Cities
	}

	@Test
	public void TestSuggestFriendInstanceNotFoundException() throws InstanceNotFoundException {
		assertThrows(InstanceNotFoundException.class, () -> {
			friendService.suggestFriend(-1L);
		});
	}

	@Test
	public void testGetFriendList()
			throws DuplicateInstanceException, InvalidDateException, InstanceNotFoundException, RequestParamException {
		final UserImpl user1 = createUser("usuarioFriendList1", "contraseñaFriendList1", getDateTime(1, 1, 2000),
				"hombre", "coruna", "descripcion");
		final UserImpl user2 = createUser("usuarioFriendList2", "contraseñaFriendList2", getDateTime(1, 1, 2000),
				"hombre", "coruna", "descripcion");
		final UserImpl user3 = createUser("usuarioFriendList3", "contraseñaFriendList3", getDateTime(1, 1, 2000),
				"hombre", "coruna", "descripcion");
		final UserImpl user4 = createUser("usuarioFriendList4", "contraseñaFriendList4", getDateTime(1, 1, 2000),
				"hombre", "coruna", "descripcion");
		final UserImpl user5 = createUser("usuarioFriendList5", "contraseñaFriendList5", getDateTime(1, 1, 2000),
				"hombre", "coruna", "descripcion");

		userService.signUp(user1);
		userService.signUp(user2);
		userService.signUp(user3);
		userService.signUp(user4);
		userService.signUp(user5);

		matchRepository.save(new MatchImpl(new MatchId(user1.getId(), user2.getId()), getDateTime(1, 1, 2000)));
		matchRepository.save(new MatchImpl(new MatchId(user1.getId(), user3.getId()), getDateTime(1, 1, 2000)));
		matchRepository.save(new MatchImpl(new MatchId(user1.getId(), user4.getId()), getDateTime(1, 1, 2000)));
		matchRepository.save(new MatchImpl(new MatchId(user1.getId(), user5.getId()), getDateTime(1, 1, 2000)));

		BlockFriendList<UserImpl> user1Result = friendService.getFriendList(user1.getId(), 0, 2);
		assertEquals(user1Result.getFriends().size(), 2);
		assertEquals(user1Result.getExistMoreFriends(), true);

		user1Result = friendService.getFriendList(user1.getId(), 1, 2);
		assertEquals(user1Result.getFriends().size(), 2);
		assertEquals(user1Result.getExistMoreFriends(), false);

		user1Result = friendService.getFriendList(user1.getId(), 2, 2);
		assertEquals(user1Result.getFriends().size(), 0);
		assertEquals(user1Result.getExistMoreFriends(), false);
	}

	@Test
	public void testGetFriendListWithInstanceNotFoundException() {
		assertThrows(InstanceNotFoundException.class, () -> {
			friendService.getFriendList(-1L, 0, 10);
		});
	}

	@Test
	public void testGetFriendListRequestParamException() throws DuplicateInstanceException, InvalidDateException {
		final UserImpl user = createUser("usuarioFriendListRPE", "contraseñaFriendListRPE", getDateTime(1, 1, 2000),
				"hombre", "coruna", "descripcion");

		userService.signUp(user);

		assertThrows(RequestParamException.class, () -> {
			friendService.getFriendList(user.getId(), -1, 10);
		});

		assertThrows(RequestParamException.class, () -> {
			friendService.getFriendList(user.getId(), 0, 0);
		});
	}

	/******* SEND MESSAGE TESTS ****************************/

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
		friendService.sendMessage(user.get().getId(), friend.get().getId(), content);

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
		friendService.sendMessage(friend.get().getId(), user.get().getId(), content);

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
			friendService.sendMessage(user.get().getId(), 900L, content);
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
			friendService.sendMessage(user.get().getId(), friend.get().getId(), content);
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
			friendService.sendMessage(null, friend.get().getId(), content);
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
			friendService.sendMessage(user.get().getId(), null, content);
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
			friendService.sendMessage(user.get().getId(), user.get().getId(), content);
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
			friendService.sendMessage(user.get().getId(), friend.get().getId(), content);
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
			friendService.sendMessage(user.get().getId(), friend.get().getId(), content);
		});
	}
}



