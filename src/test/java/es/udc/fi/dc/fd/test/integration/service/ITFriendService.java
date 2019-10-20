package es.udc.fi.dc.fd.test.integration.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlScriptsTestExecutionListener;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;

import es.udc.fi.dc.fd.controller.exception.AlreadyRejectedException;
import es.udc.fi.dc.fd.controller.exception.DuplicateInstanceException;
import es.udc.fi.dc.fd.controller.exception.InstanceNotFoundException;
import es.udc.fi.dc.fd.controller.exception.InvalidDateException;
import es.udc.fi.dc.fd.controller.exception.InvalidRecommendationException;
import es.udc.fi.dc.fd.dtos.FriendDto;
import es.udc.fi.dc.fd.model.SexCriteriaEnum;
import es.udc.fi.dc.fd.model.persistence.MatchId;
import es.udc.fi.dc.fd.model.persistence.MatchImpl;
import es.udc.fi.dc.fd.model.persistence.RejectedId;
import es.udc.fi.dc.fd.model.persistence.RejectedImpl;
import es.udc.fi.dc.fd.model.persistence.RequestId;
import es.udc.fi.dc.fd.model.persistence.RequestImpl;
import es.udc.fi.dc.fd.model.persistence.UserImpl;
import es.udc.fi.dc.fd.repository.MatchRepository;
import es.udc.fi.dc.fd.repository.RejectedRepository;
import es.udc.fi.dc.fd.repository.RequestRepository;
import es.udc.fi.dc.fd.repository.UserRepository;
import es.udc.fi.dc.fd.service.FriendService;
import es.udc.fi.dc.fd.service.UserService;

@RunWith(JUnitPlatform.class)
@ExtendWith(SpringExtension.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
	SqlScriptsTestExecutionListener.class })
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:context/service.xml", "classpath:context/persistence.xml",
"classpath:context/application-context.xml" })
@TestPropertySource({ "classpath:config/persistence-access.properties", "classpath:config/service.properties" })
public class ITFriendService {

	@Autowired
	private UserService userService;

	@Autowired
	private FriendService friendService;
	@Autowired
	RequestRepository requestRepository;

	@Autowired
	RejectedRepository rejectedRepository;
	@Autowired
	UserRepository userRepository;

	@Autowired
	MatchRepository matchRepository;

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

	private UserImpl signUp(String userName, String password, int age, String sex, String city) {

		final UserImpl user = createUser(userName, password, getDateTime(1, 1, LocalDateTime.now().getYear() - age),
				sex, city, "descripcion");

		try {
			userService.signUp(user);
		} catch (DuplicateInstanceException | InvalidDateException e) {
			throw new RuntimeException(e);
		}

		return user;

	}

	// -----addImage-----

	@Test
	public void testDefaultCriteriaRequest()
			throws InstanceNotFoundException, InvalidRecommendationException, AlreadyRejectedException {
		UserImpl user1 = signUp("manolo3", "pass", 22, "Male", "Catalunya");
		user1.setCriteriaMaxAge(99);
		user1.setCriteriaMinAge(18);
		user1.setCriteriaSex(SexCriteriaEnum.ANY);
		user1 = userRepository.save(user1);
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
			throws InstanceNotFoundException, InvalidRecommendationException, AlreadyRejectedException {
		final UserImpl user1 = signUp("manolo5", "pass", 22, "Male", "Catalunya");
		user1.setCriteriaMaxAge(99);
		user1.setCriteriaMinAge(18);
		user1.setCriteriaSex(SexCriteriaEnum.ANY);

		userRepository.save(user1);

		final UserImpl user2 = signUp("manolo6", "pass2", 102, "Female", "Catalunya");

		assertThrows(InvalidRecommendationException.class, () -> {
			friendService.acceptRecommendation(user1.getId(), user2.getId());
		});
		user2.setDate(getDateTime(10, 10, 1998));
		// TODO with cities
		// user2.setCity("Espanya");
		userRepository.save(user2);
		//
		// assertThrows(InvalidRecommendationException.class, () -> {
		// friendService.acceptRecommendation(user1.getId(), user2.getId());
		// });
		// user2.setCity("Catalunya");
		// userRepository.save(user2);

		friendService.acceptRecommendation(user1.getId(), user2.getId());
		// assertTrue(requestRepository.count() == 1);

	}

	@Test
	public void testReject()
			throws InstanceNotFoundException, InvalidRecommendationException, AlreadyRejectedException {
		final UserImpl user1 = signUp("manolo7", "pass", 22, "Male", "Catalunya");
		user1.setCriteriaMaxAge(99);
		user1.setCriteriaMinAge(18);
		user1.setCriteriaSex(SexCriteriaEnum.ANY);

		userRepository.save(user1);

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
			throws InstanceNotFoundException, InvalidRecommendationException, AlreadyRejectedException {
		final UserImpl user1 = signUp("manolo9", "pass", 22, "Male", "Catalunya");
		user1.setCriteriaMaxAge(99);
		user1.setCriteriaMinAge(18);
		user1.setCriteriaSex(SexCriteriaEnum.ANY);

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
	public void testInvalidCriteriaReject()
			throws InstanceNotFoundException, InvalidRecommendationException, AlreadyRejectedException {
		final UserImpl user1 = signUp("manolo11", "pass", 22, "Male", "Catalunya");
		user1.setCriteriaMaxAge(99);
		user1.setCriteriaMinAge(18);
		user1.setCriteriaSex(SexCriteriaEnum.ANY);

		userRepository.save(user1);

		final UserImpl user2 = signUp("manolo12", "pass2", 102, "Female", "Catalunya");

		assertThrows(InvalidRecommendationException.class, () -> {
			friendService.rejectRecommendation(user1.getId(), user2.getId());
		});
		user2.setDate(getDateTime(10, 10, 1998));
		// TODO With cities
		// user2.setCity("Espanya");
		userRepository.save(user2);
		//
		// assertThrows(InvalidRecommendationException.class, () -> {
		// friendService.rejectRecommendation(user1.getId(), user2.getId());
		// });
		// user2.setCity("Catalunya");
		// userRepository.save(user2);

		friendService.rejectRecommendation(user1.getId(), user2.getId());
		// assertTrue(rejectedRepository.count() == 1);

	}

	@Test
	public void MatchTest() throws InstanceNotFoundException, InvalidRecommendationException, AlreadyRejectedException {
		final UserImpl user1 = signUp("manolo13", "pass", 22, "Male", "Catalunya");
		user1.setCriteriaMaxAge(99);
		user1.setCriteriaMinAge(18);
		user1.setCriteriaSex(SexCriteriaEnum.ANY);

		userRepository.save(user1);

		final UserImpl user2 = signUp("manolo14", "pass2", 44, "Female", "Catalunya");
		user2.setCriteriaMaxAge(99);
		user2.setCriteriaMinAge(18);
		user2.setCriteriaSex(SexCriteriaEnum.ANY);
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
	@Sql(scripts = "/initialData.sql")
	public void TestSuggestFriend() throws InstanceNotFoundException {
		/*
		 * User id=1: CriteriaSex = Female CriteriaMinAge = "18" CriteriaMaxAge = "99"
		 * CitiesCriteria //TODO
		 */
		//TODO -> Ajustar fechas usuarios a hoy
		final Optional<FriendDto> userSuggested = friendService.suggestFriend(1L);

		assertNotNull(userSuggested);
		assertEquals(userSuggested.get().getUserName(), "User3");

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

}
