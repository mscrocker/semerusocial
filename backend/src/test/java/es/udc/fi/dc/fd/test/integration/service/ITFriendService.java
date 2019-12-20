package es.udc.fi.dc.fd.test.integration.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
import es.udc.fi.dc.fd.controller.exception.AlreadyBlockedException;
import es.udc.fi.dc.fd.controller.exception.AlreadyRejectedException;
import es.udc.fi.dc.fd.controller.exception.CantFindMoreFriendsException;
import es.udc.fi.dc.fd.controller.exception.DuplicateInstanceException;
import es.udc.fi.dc.fd.controller.exception.InstanceNotFoundException;
import es.udc.fi.dc.fd.controller.exception.InvalidAgeException;
import es.udc.fi.dc.fd.controller.exception.InvalidDateException;
import es.udc.fi.dc.fd.controller.exception.InvalidRateException;
import es.udc.fi.dc.fd.controller.exception.InvalidRecommendationException;
import es.udc.fi.dc.fd.controller.exception.ItsNotYourFriendException;
import es.udc.fi.dc.fd.controller.exception.NotRatedException;
import es.udc.fi.dc.fd.controller.exception.RequestParamException;
import es.udc.fi.dc.fd.dtos.SearchCriteriaDto;
import es.udc.fi.dc.fd.dtos.SearchUsersDto;
import es.udc.fi.dc.fd.model.SexCriteriaEnum;
import es.udc.fi.dc.fd.model.persistence.BlockedId;
import es.udc.fi.dc.fd.model.persistence.FriendListOut;
import es.udc.fi.dc.fd.model.persistence.MatchId;
import es.udc.fi.dc.fd.model.persistence.MatchImpl;
import es.udc.fi.dc.fd.model.persistence.RejectedId;
import es.udc.fi.dc.fd.model.persistence.RejectedImpl;
import es.udc.fi.dc.fd.model.persistence.RequestId;
import es.udc.fi.dc.fd.model.persistence.RequestImpl;
import es.udc.fi.dc.fd.model.persistence.SearchCriteria;
import es.udc.fi.dc.fd.model.persistence.SuggestedSearchCriteria;
import es.udc.fi.dc.fd.model.persistence.UserImpl;
import es.udc.fi.dc.fd.repository.BlockedRepository;
import es.udc.fi.dc.fd.repository.MatchRepository;
import es.udc.fi.dc.fd.repository.MessageRepository;
import es.udc.fi.dc.fd.repository.RejectedRepository;
import es.udc.fi.dc.fd.repository.RequestRepository;
import es.udc.fi.dc.fd.repository.UserRepository;
import es.udc.fi.dc.fd.service.Block;
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
	private BlockedRepository blockedRepository;

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

	private SearchCriteria createCriteria(String sex, int minAge, int maxAge, List<String> CityList, int minRate) {
		return new SearchCriteria(SexCriteriaEnum.fromCode(sex), minAge, maxAge, CityList, minRate);
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

	private void setSearchCriteria(Long id, String genre, int minAge, int maxAge, String... cities)
			throws InvalidRateException, NotRatedException {
		try {
			userService.setSearchCriteria(id,
					new SearchCriteria(SexCriteriaEnum.fromCode(genre), minAge, maxAge, Arrays.asList(cities), 1));
		} catch (InstanceNotFoundException | InvalidAgeException e) {
			e.printStackTrace();
			fail();

		}
	}

	// -----addImage-----

	@Test
	public void testDefaultCriteriaRequest() throws InstanceNotFoundException, InvalidRecommendationException,
	AlreadyRejectedException, AlreadyAceptedException {
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
	public void testInvalidCriteriaRequest() throws InstanceNotFoundException, InvalidRecommendationException,
	AlreadyRejectedException, AlreadyAceptedException, InvalidRateException, NotRatedException {
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
	public void testReject() throws InstanceNotFoundException, InvalidRecommendationException, AlreadyRejectedException,
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
	public void testRejectAlreadyRejected() throws InstanceNotFoundException, InvalidRecommendationException,
	AlreadyRejectedException, AlreadyAceptedException {
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
	public void testInvalidCriteriaReject() throws InstanceNotFoundException, InvalidRecommendationException,
	AlreadyRejectedException, AlreadyAceptedException, InvalidRateException, NotRatedException {

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

	/*******
	 * SUGGEST FRIEND TESTS
	 *
	 * @throws NotRatedException
	 * @throws InvalidRateException
	 *************************************/
	@Test

	// @Sql(scripts = "/initialData.sql")
	public void TestSuggestFriend() throws InstanceNotFoundException, InvalidRateException, NotRatedException {
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
	public void TestSuggestFriendMinRate() throws InstanceNotFoundException, InvalidRateException, NotRatedException,
	ItsNotYourFriendException, RequestParamException {

		final UserImpl user1 = signUp("testSuggestFriendMinRate", "pass", 22, "Male", "osaka");
		final UserImpl user2 = signUp("testSuggestFriendMinRate2", "pass", 22, "Male", "osaka");
		final UserImpl user3 = signUp("testSuggestFriendMinRate3", "pass", 22, "Male", "osaka");
		final UserImpl user4 = signUp("testSuggestFriendMinRate4", "pass", 22, "Male", "osaka");
		setSearchCriteria(user1.getId(), "Male", 18, 45, "osaka");

		// Hacemos que user 2 sea amigo de user1 ,user3y user4 y les hace una votacion
		matchRepository.save(new MatchImpl(new MatchId(user2.getId(), user1.getId()), LocalDateTime.now()));
		matchRepository.save(new MatchImpl(new MatchId(user2.getId(), user3.getId()), LocalDateTime.now()));
		matchRepository.save(new MatchImpl(new MatchId(user2.getId(), user4.getId()), LocalDateTime.now()));
		userService.rateUser(3, user2.getId(), user1.getId());// El user2 vota al user1
		userService.rateUser(3, user2.getId(), user3.getId());// El user 2 vota al user3
		userService.rateUser(1, user2.getId(), user4.getId());// El user 2 vota al user4

		// Ponemos el minRate del user1 a 3
		user1.setMinRateCriteria(3);
		userRepository.save(user1);

		// With default matches
		final Optional<UserImpl> userSuggested = friendService.suggestFriend(user1.getId());

		assertTrue(userSuggested.isPresent());
		assertEquals(userSuggested.get().getUserName(), user3.getUserName());
		matchRepository.save(new MatchImpl(new MatchId(user1.getId(), user3.getId()), LocalDateTime.now()));
		// No lo encuentra ya que el user 4 tiene una media 1 y el minRate del user1 es
		// 3
		final Optional<UserImpl> userSuggested2 = friendService.suggestFriend(user1.getId());
		assertTrue(userSuggested2.isEmpty());

		user4.setPremium(true);
		userRepository.save(user4);

		// Ahora al ser el user4 premium lo tiene que encontrar
		final Optional<UserImpl> userSuggested3 = friendService.suggestFriend(user1.getId());

		assertTrue(userSuggested3.isPresent());
		assertEquals(userSuggested3.get().getUserName(), user4.getUserName());

	}

	@Test
	public void TestSuggestFriendPremium() throws InstanceNotFoundException, InvalidRateException, NotRatedException {

		final UserImpl user1 = signUp("testSuggestAAAA", "pass", 80, "Female", "pepe");
		final UserImpl user2 = signUp("testSuggestBBBB", "pass", 22, "Male", "osaka");
		setSearchCriteria(user2.getId(), "Male", 18, 45, "osaka");

		user1.setPremium(true);
		userRepository.save(user1);

		final Optional<UserImpl> userSuggested = friendService.suggestFriend(user2.getId());
		assertTrue(userSuggested.isPresent());
		assertEquals(userSuggested.get().getUserName(), user1.getUserName());
	}

	@Test
	public void TestSuggestFriendInstanceNotFoundException() throws InstanceNotFoundException {
		assertThrows(InstanceNotFoundException.class, () -> {
			friendService.suggestFriend(-1L);
		});
	}

	//
	private List<UserImpl> initialFriendList() throws DuplicateInstanceException, InvalidDateException {
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

		final List<UserImpl> list = new ArrayList<>();
		list.add(user1);
		list.add(user2);
		list.add(user3);
		list.add(user4);
		list.add(user5);

		return list;
	}

	@Test
	public void testGetFriendList()
			throws DuplicateInstanceException, InvalidDateException, InstanceNotFoundException, RequestParamException {
		final UserImpl user1 = initialFriendList().get(0);

		Block<FriendListOut> user1Result;
		for (int i = 0; i < 3; i++) {
			user1Result = friendService.getFriendList(user1.getId(), i, 2);
			assertEquals(user1Result.getElements().size(), i == 2 ? 0 : 2);
			assertEquals(user1Result.isExistMoreElements(), i == 0);
		}

	}

	@Test
	public void testGetFriendListUser5()
			throws DuplicateInstanceException, InvalidDateException, InstanceNotFoundException, RequestParamException {
		final UserImpl user5 = initialFriendList().get(4);

		Block<FriendListOut> user5Result;
		for (int i = 0; i < 2; i++) {
			user5Result = friendService.getFriendList(user5.getId(), i, 2);
			assertEquals(user5Result.getElements().size(), i == 0 ? 1 : 0);
			assertEquals(user5Result.isExistMoreElements(), false);
		}
	}

	@Test
	public void testGetFriendListFriendWithRating()
			throws DuplicateInstanceException, InvalidDateException, InstanceNotFoundException, RequestParamException {
		final List<UserImpl> list = initialFriendList();
		final UserImpl user1 = list.get(0);
		final UserImpl user5 = list.get(4);

		user5.setRatingVotes(1);
		user5.setRating(4);
		userRepository.save(user5);

		final Block<FriendListOut> user1Result = friendService.getFriendList(user1.getId(), 0, 10);
		assertEquals(4, user1Result.getElements().size());
		assertEquals(false, user1Result.isExistMoreElements());

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

	@Test
	public void testBlockUser() throws InstanceNotFoundException, ItsNotYourFriendException, AlreadyBlockedException,
	DuplicateInstanceException, InvalidDateException, InvalidRecommendationException, AlreadyRejectedException,
	AlreadyAceptedException {
		final UserImpl user = createUser("usuarioBlockUser", "contraseñaBlockUser", getDateTime(1, 1, 2000), "hombre",
				"coruna", "descripcion");
		final UserImpl user2 = createUser("usuarioBlockUser2", "contraseñaBlockUser2", getDateTime(1, 1, 2000),
				"hombre", "coruna", "descripcion");

		userService.signUp(user);
		userService.signUp(user2);

		// they are friends now
		friendService.acceptRecommendation(user.getId(), user2.getId());
		friendService.acceptRecommendation(user2.getId(), user.getId());

		// user blocks user2
		friendService.blockUser(user.getId(), user2.getId());

		final Long firstId = Math.min(user.getId(), user2.getId());
		final Long secondId = user2.getId().equals(firstId) ? user.getId() : user2.getId();

		// check if they are not friends now and if user blocked user2
		assertTrue(!matchRepository.findById(new MatchId(firstId, secondId)).isPresent()
				&& blockedRepository.findById(new BlockedId(user.getId(), user2.getId())).isPresent());
	}

	@Test
	public void testBlockUserINFE() throws DuplicateInstanceException, InvalidDateException {
		final UserImpl user = createUser("usuarioBlockUserINFE", "contraseñaBlockUserINFE", getDateTime(1, 1, 2000),
				"hombre", "coruna", "descripcion");
		userService.signUp(user);

		// user2 does not exists
		assertThrows(InstanceNotFoundException.class, () -> {
			friendService.blockUser(user.getId(), -1L);
		});
		// user does not exists
		assertThrows(InstanceNotFoundException.class, () -> {
			friendService.blockUser(-1L, user.getId());
		});
	}

	@Test
	public void testBlockUserINYF() throws InstanceNotFoundException, ItsNotYourFriendException,
	AlreadyBlockedException, DuplicateInstanceException, InvalidDateException, InvalidRecommendationException,
	AlreadyRejectedException, AlreadyAceptedException {
		final UserImpl user = createUser("usuarioBlockINYF", "contraseñaBlockINYF", getDateTime(1, 1, 2000), "hombre",
				"coruna", "descripcion");
		final UserImpl user2 = createUser("usuarioBlock2INYF", "contraseñaBlock2INYF", getDateTime(1, 1, 2000),
				"hombre", "coruna", "descripcion");

		userService.signUp(user);
		userService.signUp(user2);

		friendService.acceptRecommendation(user.getId(), user2.getId());

		// user can not block user2 because they are not friends
		assertThrows(ItsNotYourFriendException.class, () -> {
			friendService.blockUser(user.getId(), user2.getId());
		});
	}

	// AlreadyBlockedException
	@Test
	public void testBlockUserABE() throws InstanceNotFoundException, ItsNotYourFriendException, AlreadyBlockedException,
	DuplicateInstanceException, InvalidDateException, InvalidRecommendationException, AlreadyRejectedException,
	AlreadyAceptedException {
		final UserImpl user = createUser("usuarioBlockABE", "contraseñaBlockABE", getDateTime(1, 1, 2000), "hombre",
				"coruna", "descripcion");
		final UserImpl user2 = createUser("usuarioBlock2ABE", "contraseñaBlock2ABE", getDateTime(1, 1, 2000), "hombre",
				"coruna", "descripcion");

		userService.signUp(user);
		userService.signUp(user2);

		// they are friends now
		friendService.acceptRecommendation(user.getId(), user2.getId());
		friendService.acceptRecommendation(user2.getId(), user.getId());

		// user blocks user2
		friendService.blockUser(user.getId(), user2.getId());
		// user try to clock user2 again
		assertThrows(AlreadyBlockedException.class, () -> {
			friendService.blockUser(user.getId(), user2.getId());
		});
	}

	@Test
	public void SuggestedSearchCriteria()
			throws InstanceNotFoundException, CantFindMoreFriendsException, DuplicateInstanceException,
			InvalidDateException, InvalidRecommendationException, AlreadyRejectedException, AlreadyAceptedException,
			InvalidRateException, ItsNotYourFriendException, InvalidAgeException, NotRatedException {
		final UserImpl user = createUser("UserSSC", "UserSSC", getDateTime(1, 1, 2000), "Male", "coruna",
				"descripcion");
		final UserImpl user2 = createUser("UserSSC2", "UserSSC", getDateTime(1, 1, 2000), "Male", "coruna",
				"descripcion");
		final UserImpl user3 = createUser("UserSSC3", "UserSSC", getDateTime(1, 1, 2000), "Male", "coruna",
				"descripcion");

		userService.signUp(user);
		userService.signUp(user2);
		userService.signUp(user3);

		// they are friends now
		friendService.acceptRecommendation(user.getId(), user2.getId());
		friendService.acceptRecommendation(user2.getId(), user.getId());

		// User2 rates with 3 points to user
		userService.rateUser(3, user2.getId(), user.getId());

		final List<String> cityList = new ArrayList<>();
		cityList.add("coruna");
		final SearchCriteria criteria = createCriteria("Male", 30, 60, cityList, 1);
		userService.setSearchCriteria(user.getId(), criteria);

		final SuggestedSearchCriteria suggestionCriteria = friendService.suggestNewCriteria(user.getId());

		// user1 encontraria a user3
		assertEquals(suggestionCriteria, new SuggestedSearchCriteria(-12, 0, 0, 1));

		// they are friends now
		matchRepository.save(new MatchImpl(new MatchId(user.getId(), user3.getId()), LocalDateTime.now()));

		/////////////////////////////////////////////////////////////

		final UserImpl user4 = createUser("UserSSC4", "UserSSC", getDateTime(1, 1, 1930), "Male", "coruna",
				"descripcion");
		userService.signUp(user4);

		// user1 encontraria a user4
		final SuggestedSearchCriteria suggestionCriteria2 = friendService.suggestNewCriteria(user.getId());

		// check if they are not friends now and if user blocked user2
		assertEquals(suggestionCriteria2, new SuggestedSearchCriteria(0, 30, 0, 1));

		// they are friends now
		matchRepository.save(new MatchImpl(new MatchId(user.getId(), user4.getId()), LocalDateTime.now()));

		////////////////////////////////////////////////////////////////

		final UserImpl user5 = createUser("UserSSC5", "UserSSC", getDateTime(1, 1, 2000), "Male", "coruna",
				"descripcion");
		userService.signUp(user5);

		final SearchCriteria criteria2 = createCriteria("Male", 18, 60, cityList, 4);
		userService.setSearchCriteria(user.getId(), criteria2);

		// they are friends now
		matchRepository.save(new MatchImpl(new MatchId(user2.getId(), user5.getId()), LocalDateTime.now()));
		userService.rateUser(1, user2.getId(), user5.getId());

		// user1 encontraria a user5
		final SuggestedSearchCriteria suggestionCriteria3 = friendService.suggestNewCriteria(user.getId());

		assertEquals(suggestionCriteria3, new SuggestedSearchCriteria(0, 0, -3, 1));

		// they are friends now
		matchRepository.save(new MatchImpl(new MatchId(user.getId(), user5.getId()), LocalDateTime.now()));
		////////////////////////////////////////////////////////////////

		final UserImpl user6 = createUser("UserSSC6", "UserSSC", getDateTime(1, 1, 1930), "Male", "coruna",
				"descripcion");
		userService.signUp(user6);

		// they are friends now
		matchRepository.save(new MatchImpl(new MatchId(user2.getId(), user6.getId()), LocalDateTime.now()));
		userService.rateUser(1, user2.getId(), user6.getId());

		// user1 encontraria a user6
		final SuggestedSearchCriteria suggestionCriteria4 = friendService.suggestNewCriteria(user.getId());

		assertEquals(suggestionCriteria4, new SuggestedSearchCriteria(0, 30, -3, 1));

		// they are friends now
		matchRepository.save(new MatchImpl(new MatchId(user.getId(), user6.getId()), LocalDateTime.now()));

		////////////////////////////////////////////////////////////////

		final UserImpl user7 = createUser("UserSSC7", "UserSSC", getDateTime(1, 1, 1820), "Male", "coruna",
				"descripcion");
		userService.signUp(user7);

		// they are friends now
		matchRepository.save(new MatchImpl(new MatchId(user2.getId(), user7.getId()), LocalDateTime.now()));
		userService.rateUser(5, user2.getId(), user7.getId());

		final SearchCriteria criteria3 = createCriteria("Male", 18, 181, cityList, 4);
		userService.setSearchCriteria(user.getId(), criteria3);

		// user1 encontraria a user7
		final SuggestedSearchCriteria suggestionCriteria5 = friendService.suggestNewCriteria(user.getId());

		assertEquals(suggestionCriteria5, new SuggestedSearchCriteria(0, 19, 0, 1));

		// they are friends now
		matchRepository.save(new MatchImpl(new MatchId(user.getId(), user7.getId()), LocalDateTime.now()));

		////////////////////////////////////////////////////////////////

		final UserImpl user8 = createUser("UserSSC8", "UserSSC", getDateTime(1, 1, 2001), "Male", "coruna",
				"descripcion");
		userService.signUp(user8);
		final UserImpl user9 = createUser("UserSSC9", "UserSSC", getDateTime(1, 1, 2001), "Male", "coruna",
				"descripcion");
		userService.signUp(user9);
		final UserImpl user10 = createUser("UserSSC10", "UserSSC", getDateTime(1, 1, 2001), "Male", "coruna",
				"descripcion");
		userService.signUp(user10);

		// they are friends now
		matchRepository.save(new MatchImpl(new MatchId(user2.getId(), user8.getId()), LocalDateTime.now()));
		userService.rateUser(5, user2.getId(), user8.getId());
		matchRepository.save(new MatchImpl(new MatchId(user2.getId(), user9.getId()), LocalDateTime.now()));
		userService.rateUser(5, user2.getId(), user9.getId());
		matchRepository.save(new MatchImpl(new MatchId(user2.getId(), user10.getId()), LocalDateTime.now()));
		userService.rateUser(5, user2.getId(), user10.getId());

		final SearchCriteria criteria4 = createCriteria("Male", 28, 60, cityList, 4);
		userService.setSearchCriteria(user.getId(), criteria4);

		// user1 encontraria a user8,user9,user10
		final SuggestedSearchCriteria suggestionCriteria6 = friendService.suggestNewCriteria(user.getId());

		assertEquals(suggestionCriteria6, new SuggestedSearchCriteria(-10, 0, 0, 3));

		// they are friends now
		matchRepository.save(new MatchImpl(new MatchId(user.getId(), user8.getId()), LocalDateTime.now()));
	}

	// searchUsersByMetadataAndKeywords
	@Test
	public void testSearchUsersByMetadataAndKeywords() throws DuplicateInstanceException, InvalidDateException {
		final UserImpl user = createUser("usuarioSearch", "contraseñaBlockABE", getDateTime(1, 1, 2000), "Male",
				"coruna", "palabra1");
		final UserImpl user2 = createUser("usuarioSearch2", "contraseñaBlockABE", getDateTime(1, 1, 2000), "Male",
				"coruna", "palabra2");
		final UserImpl user3 = createUser("usuarioSearch3", "contraseñaBlockABE", getDateTime(1, 1, 2000), "Male",
				"coruna", "palabra3");
		final UserImpl user4 = createUser("usuarioSearch4", "contraseñaBlockABE", getDateTime(1, 1, 2000), "Male",
				"lugo", "palabra1");

		userService.signUp(user);
		userService.signUp(user2);
		userService.signUp(user3);
		userService.signUp(user4);

		final List<String> cities = Arrays.asList("coruna", "gotham");

		final SearchCriteriaDto searchCriteriaDto = new SearchCriteriaDto("Male", 18, 22, cities, 1);

		final SearchUsersDto searchUsersDto = new SearchUsersDto("palabra1 palabra2", searchCriteriaDto);

		final Block<UserImpl> result1 = friendService.searchUsersByMetadataAndKeywords(searchUsersDto, 0, 10);

		final List<UserImpl> expected1 = new ArrayList<>();
		expected1.add(user);
		expected1.add(user2);
		expected1.add(user4);

		assertEquals(expected1, result1.getElements());
		assertFalse(result1.isExistMoreElements());

		final Block<UserImpl> result1v2 = friendService.searchUsersByMetadataAndKeywords(searchUsersDto, 0, 2);

		final List<UserImpl> expected1v2 = new ArrayList<>();
		expected1v2.add(user);
		expected1v2.add(user2);

		assertEquals(expected1v2, result1v2.getElements());
		assertTrue(result1v2.isExistMoreElements());

		final Block<UserImpl> result1v3 = friendService.searchUsersByMetadataAndKeywords(searchUsersDto, 1, 2);

		final List<UserImpl> expected1v3 = new ArrayList<>();
		expected1v3.add(user4);

		assertEquals(expected1v3, result1v3.getElements());
		assertFalse(result1v3.isExistMoreElements());

		final SearchUsersDto searchUsersDto2 = new SearchUsersDto("palabra4", null);

		final Block<UserImpl> result2 = friendService.searchUsersByMetadataAndKeywords(searchUsersDto2, 0, 10);

		final List<UserImpl> expected2 = new ArrayList<>();

		assertEquals(expected2, result2.getElements());
		assertFalse(result2.isExistMoreElements());

		final List<String> cities3 = Arrays.asList("lugo");

		final SearchCriteriaDto searchCriteriaDto3 = new SearchCriteriaDto("Female", 23, 28, cities3, 1);

		final SearchUsersDto searchUsersDto3 = new SearchUsersDto(null, searchCriteriaDto3);

		final Block<UserImpl> result3 = friendService.searchUsersByMetadataAndKeywords(searchUsersDto3, 0, 10);

		final List<UserImpl> expected3 = new ArrayList<>();
		expected3.add(user4);

		assertEquals(expected3, result3.getElements());
		assertFalse(result3.isExistMoreElements());

	}

	@Test
	public void SuggestedSearchCriteriaINF()
			throws InstanceNotFoundException, CantFindMoreFriendsException, DuplicateInstanceException,
			InvalidDateException, InvalidRecommendationException, AlreadyRejectedException, AlreadyAceptedException,
			InvalidRateException, ItsNotYourFriendException, InvalidAgeException, NotRatedException {

		assertThrows(InstanceNotFoundException.class, () -> {
			friendService.suggestNewCriteria(-1L);
		});

		assertThrows(InstanceNotFoundException.class, () -> {
			friendService.suggestNewCriteria(null);
		});

	}

	@Test
	public void SuggestedSearchCriteriaCFMF()
			throws InstanceNotFoundException, CantFindMoreFriendsException, DuplicateInstanceException,
			InvalidDateException, InvalidRecommendationException, AlreadyRejectedException, AlreadyAceptedException,
			InvalidRateException, ItsNotYourFriendException, InvalidAgeException, NotRatedException {

		final UserImpl user = createUser("UserSSCCFMF", "UserSSC", getDateTime(1, 1, 2001), "Male", "coruna",
				"descripcion");
		userService.signUp(user);

		assertThrows(CantFindMoreFriendsException.class, () -> {
			friendService.suggestNewCriteria(user.getId());
		});
	}
}
