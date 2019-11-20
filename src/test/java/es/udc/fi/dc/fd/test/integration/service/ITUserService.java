package es.udc.fi.dc.fd.test.integration.service;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

import es.udc.fi.dc.fd.controller.exception.DuplicateInstanceException;
import es.udc.fi.dc.fd.controller.exception.IncorrectLoginException;
import es.udc.fi.dc.fd.controller.exception.InstanceNotFoundException;
import es.udc.fi.dc.fd.controller.exception.InvalidAgeException;
import es.udc.fi.dc.fd.controller.exception.InvalidDateException;
import es.udc.fi.dc.fd.controller.exception.InvalidRateException;
import es.udc.fi.dc.fd.controller.exception.ItsNotYourFriendException;
import es.udc.fi.dc.fd.controller.exception.NotRatedException;
import es.udc.fi.dc.fd.dtos.LoginParamsDto;
import es.udc.fi.dc.fd.model.SexCriteriaEnum;
import es.udc.fi.dc.fd.model.persistence.MatchId;
import es.udc.fi.dc.fd.model.persistence.MatchImpl;
import es.udc.fi.dc.fd.model.persistence.SearchCriteria;
import es.udc.fi.dc.fd.model.persistence.UserImpl;
import es.udc.fi.dc.fd.repository.CityCriteriaRepository;
import es.udc.fi.dc.fd.repository.MatchRepository;
import es.udc.fi.dc.fd.repository.UserRepository;
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
public class ITUserService {

	@Autowired
	private UserService userService;

	@Autowired
	private CityCriteriaRepository cityCriteriaRepository;

	@Autowired
	private MatchRepository matchRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	public ITUserService() {
		super();
	}

	private UserImpl createUser(String userName, String password, LocalDateTime date, String sex, String city,
			String description) {
		return new UserImpl(userName, password, date, sex, city, description, 0, 0);
	}

	private LocalDateTime getDateTime(int day, int month, int year) {
		return LocalDateTime.of(year, month, day, 00, 01);
	}

	private SearchCriteria createCriteria(String sex, int minAge, int maxAge, List<String> CityList) {
		return new SearchCriteria(SexCriteriaEnum.fromCode(sex), minAge, maxAge, CityList, 1);
	}

	private SearchCriteria createCriteria(String sex, int minAge, int maxAge, List<String> CityList, int minRate) {
		return new SearchCriteria(SexCriteriaEnum.fromCode(sex), minAge, maxAge, CityList, minRate);
	}

	// ----- signUp -----

	@Test
	public void testSignUpAndLoginFromUserName()
			throws DuplicateInstanceException, InstanceNotFoundException, InvalidDateException {

		final UserImpl user = createUser("usuarioSignUpAndLoginFromId", "contraseñaSignUpAndLoginFromId",
				getDateTime(1, 1, 2000), "hombre", "coruna", "descripcion");

		userService.signUp(user);

		final UserImpl loggedInUser = userService.loginFromUserId(user.getId());
		assertEquals(user, loggedInUser);
	}

	@Test
	public void testSignUpDuplicatedUserName() throws DuplicateInstanceException, InvalidDateException {

		final UserImpl user = createUser("usuarioSignUpDuplicated", "contraseñaSignUpDuplicated",
				getDateTime(1, 1, 2000), "hombre", "coruna", "descripcion");

		userService.signUp(user);

		assertThrows(DuplicateInstanceException.class, () -> {
			userService.signUp(user);
		});
	}

	@Test
	public void testSignUpInvalidDateException() throws DuplicateInstanceException, InvalidDateException {

		final UserImpl user = createUser("usuarioSignUpIDE", "contraseñaSignUpIDE", LocalDateTime.now(), "nombre",
				"coruna", "descripcion");

		assertThrows(InvalidDateException.class, () -> {
			userService.signUp(user);
		});
	}

	// ----- login -----

	@Test
	public void testLogin() throws DuplicateInstanceException, IncorrectLoginException, InvalidDateException {

		final UserImpl user = createUser("usuarioLogin", "contraseñaLogin", getDateTime(1, 1, 2000), "hombre", "coruna",
				"descripcion");

		final String clearPassword = user.getPassword();

		userService.signUp(user);

		final LoginParamsDto loginDto = new LoginParamsDto();
		loginDto.setUserName(user.getUserName());
		loginDto.setPassword(clearPassword);

		final UserImpl loggedInUser = userService.login(loginDto);

		assertEquals(user, loggedInUser);
	}

	@Test
	public void testLoginWithIncorrectPassword() throws DuplicateInstanceException, InvalidDateException {

		final UserImpl user = createUser("usuarioLoginIncorrectPass", "contraseñaLoginIncorrectPass",
				getDateTime(1, 1, 2000), "hombre", "coruna", "descripcion");

		final String clearPassword = user.getPassword();

		userService.signUp(user);

		final LoginParamsDto loginDto = new LoginParamsDto();
		loginDto.setUserName(user.getUserName());
		loginDto.setPassword("xd" + clearPassword);

		assertThrows(IncorrectLoginException.class, () -> {
			userService.login(loginDto);
		});
	}

	// ----- loginFromUserId -----

	@Test
	public void testLoginFromUserId()
			throws DuplicateInstanceException, InstanceNotFoundException, InvalidDateException {

		final UserImpl user = createUser("userLoginFromUserId", "passwordLoginFromUserId", getDateTime(1, 1, 2000),
				"hombre", "coruna", "descripcion");

		userService.signUp(user);

		userService.loginFromUserId(user.getId());
	}

	@Test
	public void testLoginFromUserIdWithInstanceNotFoundException()
			throws DuplicateInstanceException, InvalidDateException {

		final UserImpl user = createUser("userLoginFromUserIdINFE", "passwordLoginFromUserIdINFE",
				getDateTime(1, 1, 2000), "hombre", "coruna", "descripcion");

		userService.signUp(user);

		//		user.setId(-1L);

		assertThrows(InstanceNotFoundException.class, () -> {
			userService.loginFromUserId(-1L);
		});
	}

	// ----- setSearchCriteria -----

	@Test
	public void testSetSearchCriteria()
			throws DuplicateInstanceException, InvalidDateException, InstanceNotFoundException, InvalidAgeException,
			InvalidRateException, NotRatedException, ItsNotYourFriendException {

		final UserImpl user = createUser("userSetSearchCriteria", "passwordSetSearchCriteria", getDateTime(1, 1, 2000),
				"hombre", "coruna", "description");
		final UserImpl user2 = createUser("userSetSearchCriteria2S", "passwordSetSearchCriteria2",
				getDateTime(1, 1, 2000),
				"hombre", "coruna", "description");
		final Long userId = userService.signUp(user);
		final Long userId2 = userService.signUp(user2);

		user.setCriteriaSex(SexCriteriaEnum.MALE);
		user.setCriteriaMaxAge(60);
		user.setCriteriaMinAge(30);

		final List<String> cityList = new ArrayList<>();
		cityList.add("a coruna");
		cityList.add("madrid");
		cityList.add("vigo");

		final SearchCriteria criteria = createCriteria("Male", 30, 60, cityList, 2);

		matchRepository.save(new MatchImpl(new MatchId(userId, userId2), LocalDateTime.now()));
		userService.rateUser(2, userId2, userId);
		userService.setSearchCriteria(userId, criteria);

		final List<String> registeredCities = cityCriteriaRepository.findCitiesByUserId(userId);

		final UserImpl registeredUser = userService.loginFromUserId(userId);

		Collections.sort(cityList);
		Collections.sort(registeredCities);
		assertEquals(cityList, registeredCities);
		assertEquals(user, registeredUser);
		assertEquals(2, user.getMinRateCriteria());

		/////////////////// Hacemos el registro de datos nuevos////////////
		final List<String> cityList2 = new ArrayList<>();
		cityList2.add("lugo");
		cityList2.add("orense");
		cityList2.add("malaga");
		cityList2.add("coruña");
		final SearchCriteria criteria2 = createCriteria("Male", 30, 60, cityList2);
		userService.setSearchCriteria(userId, criteria2);


		final List<String> registeredCities2 = cityCriteriaRepository.findCitiesByUserId(userId);

		Collections.sort(cityList2);
		Collections.sort(registeredCities2);
		assertEquals(cityList2, registeredCities2);
		assertEquals(user, registeredUser);

	}

	@Test
	public void testSetSearchCriteriaInstanceNotFoundException() throws InstanceNotFoundException, InvalidAgeException {

		final List<String> cityList = new ArrayList<>();
		cityList.add("A Coruna");
		cityList.add("Madrid");
		cityList.add("Vigo");
		final SearchCriteria criteria = createCriteria("Male", 30, 60, cityList);

		assertThrows(InstanceNotFoundException.class, () -> {
			userService.setSearchCriteria(-1L, criteria);
		});
	}

	@Test
	public void testSetSearchCriteriaWithInvalidAgeException()
			throws InstanceNotFoundException, DuplicateInstanceException, InvalidDateException {

		final UserImpl user = createUser("CriteriaUserIAE", "CriteriaPassIAE", getDateTime(1, 1, 2000), "hombre",
				"coruna", "description");
		final Long userId = userService.signUp(user);

		user.setCriteriaSex(SexCriteriaEnum.MALE);
		user.setCriteriaMaxAge(60);
		user.setCriteriaMinAge(30);

		final List<String> cityList = new ArrayList<>();
		cityList.add("A Coruna");
		cityList.add("Madrid");
		cityList.add("Vigo");
		final SearchCriteria criteria = createCriteria("Male", 150, 30, cityList);

		assertThrows(InvalidAgeException.class, () -> {
			userService.setSearchCriteria(userId, criteria);
		});

		final SearchCriteria criteria2 = createCriteria("Male", 17, 60, cityList);

		assertThrows(InvalidAgeException.class, () -> {
			userService.setSearchCriteria(userId, criteria2);
		});
	}

	@Test
	public void testSetSearchCriteriaInvalidRateException()
			throws InstanceNotFoundException, InvalidAgeException, InvalidRateException, ItsNotYourFriendException,
			DuplicateInstanceException, InvalidDateException {

		final UserImpl user = createUser("userSetSearchCriteria", "passwordSetSearchCriteria", getDateTime(1, 1, 2000),
				"hombre", "coruna", "description");
		final UserImpl user2 = createUser("userSetSearchCriteria2S", "passwordSetSearchCriteria2",
				getDateTime(1, 1, 2000), "hombre", "coruna", "description");
		final Long userId = userService.signUp(user);
		final Long userId2 = userService.signUp(user2);

		final List<String> cityList = new ArrayList<>();
		cityList.add("a coruna");
		cityList.add("madrid");
		cityList.add("vigo");

		final SearchCriteria criteria = createCriteria("Male", 30, 60, cityList, 5);

		matchRepository.save(new MatchImpl(new MatchId(userId, userId2), LocalDateTime.now()));
		userService.rateUser(2, userId2, userId);

		assertThrows(InvalidRateException.class, () -> {
			userService.setSearchCriteria(userId, criteria);
		});

		criteria.setMinRate(0);
		assertThrows(InvalidRateException.class, () -> {
			userService.setSearchCriteria(userId, criteria);
		});

		criteria.setMinRate(10);
		assertThrows(InvalidRateException.class, () -> {
			userService.setSearchCriteria(userId, criteria);
		});
	}

	// ----- getSearchCriteria -----

	@Test
	public void testGetSearchCriteria()
			throws InstanceNotFoundException, InvalidAgeException, DuplicateInstanceException, InvalidDateException,
			InvalidRateException, NotRatedException {

		final UserImpl user = createUser("userGetSearchCriteria", "passwordGetSearchCriteria", getDateTime(1, 1, 2000),
				"hombre", "coruna", "description");

		final Long userId = userService.signUp(user);

		user.setCriteriaSex(SexCriteriaEnum.MALE);
		user.setCriteriaMaxAge(60);
		user.setCriteriaMinAge(30);

		final List<String> cityList = new ArrayList<>();
		cityList.add("a coruna");
		cityList.add("madrid");
		cityList.add("vigo");
		final SearchCriteria criteria = createCriteria("Male", 30, 60, cityList);

		userService.setSearchCriteria(userId, criteria);
		final SearchCriteria userCriteria = userService.getSearchCriteria(userId);

		final SearchCriteria searchCriteria = new SearchCriteria(user.getCriteriaSex(), user.getCriteriaMinAge(),
				user.getCriteriaMaxAge(), cityList, 1);

		assertEquals(searchCriteria, userCriteria);
	}

	@Test
	public void testGetSearchCriteriaInstaceNotFoundException()
			throws InstanceNotFoundException, InvalidAgeException, DuplicateInstanceException, InvalidDateException {

		assertThrows(InstanceNotFoundException.class, () -> {
			userService.getSearchCriteria(-1L);
		});

	}

	// ----- updateProfile -----

	@Test
	public void testUpdateProfile() throws DuplicateInstanceException, InstanceNotFoundException, InvalidDateException {
		final UserImpl user = createUser("userUpdateProfile", "passwordUpdateProfile", getDateTime(1, 1, 2000),
				"hombre", "coruna", "descripcion");
		final UserImpl newUser = new UserImpl(getDateTime(1, 1, 1999), "mujer", "lugo", "descripcion editada");

		userService.signUp(user);

		userService.updateProfile(user.getId(), newUser);

		final UserImpl userFound = userService.loginFromUserId(user.getId());

		assertEquals(userFound.getDate(), newUser.getDate());
		assertEquals(userFound.getSex(), newUser.getSex());
		assertEquals(userFound.getCity(), newUser.getCity());
		assertEquals(userFound.getDescription(), newUser.getDescription());
	}

	@Test
	public void testUpdateProfileWithInstanceNotFoundException() {
		final UserImpl newUser = new UserImpl(getDateTime(1, 1, 1999), "mujer", "lugo", "descripcion editada");

		assertThrows(InstanceNotFoundException.class, () -> {
			userService.updateProfile(-1L, newUser);
		});
	}

	public void testUpdateProfileWithInvalidDateException() throws DuplicateInstanceException, InvalidDateException {
		final UserImpl user = createUser("userUpdateProfileIDE", "passwordUpdateProfileIDE", getDateTime(1, 1, 2000),
				"hombre", "coruna", "descripcion");
		final UserImpl newUser = new UserImpl(LocalDateTime.now(), "mujer", "lugo", "descripcion editada");

		userService.signUp(user);

		assertThrows(InvalidDateException.class, () -> {
			userService.updateProfile(user.getId(), newUser);
		});
	}

	@Test
	public void testRateUser() throws InstanceNotFoundException, InvalidRateException, ItsNotYourFriendException {

		// test cuando varios usuarios distintos votan al mismo usuario
		final UserImpl user1 = createUser("userRate1", "userRate1", LocalDateTime.now(), "masculino", "coruña",
				"descripcion");
		final UserImpl user2 = createUser("userRate2", "userRate2", LocalDateTime.now(), "masculino", "coruña",
				"descripcion");
		final UserImpl user3 = createUser("userRate3", "userRate3", LocalDateTime.now(), "masculino", "coruña",
				"descripcion");
		final UserImpl user4 = createUser("userRate4", "userRate4", LocalDateTime.now(), "masculino", "coruña",
				"descripcion");

		userRepository.save(user1);
		userRepository.save(user2);
		userRepository.save(user3);
		userRepository.save(user4);

		matchRepository.save(new MatchImpl(new MatchId(user1.getId(), user2.getId()), LocalDateTime.now()));
		matchRepository.save(new MatchImpl(new MatchId(user1.getId(), user3.getId()), LocalDateTime.now()));
		matchRepository.save(new MatchImpl(new MatchId(user1.getId(), user4.getId()), LocalDateTime.now()));

		userService.rateUser(2, user2.getId(), user1.getId());
		userService.rateUser(3, user3.getId(), user1.getId());
		final double rating = userService.rateUser(5, user4.getId(), user1.getId());

		assertEquals(rating, 3.333, 0.01);
		assertEquals(rating, user1.getRating(),0.01);

		// Hacemos que el usuario 4 cambie su puntuacion hacia el usuario 1
		final double rating2 = userService.rateUser(1, user4.getId(), user1.getId());

		assertEquals(rating2, 2.0, 0.01);
		assertEquals(rating2, user1.getRating(), 0.01);

	}

	@Test
	public void testRateUserInstanceNotFound()
			throws InstanceNotFoundException, InvalidRateException, ItsNotYourFriendException {

		final UserImpl user1 = createUser("userRate1", "userRate1", LocalDateTime.now(), "masculino", "coruña",
				"descripcion");

		userRepository.save(user1);

		assertThrows(InstanceNotFoundException.class, () -> {
			userService.rateUser(2, -1L, user1.getId());
		});

	}

	@Test
	public void testRateUserInvalidRateException()
			throws InstanceNotFoundException, InvalidRateException, ItsNotYourFriendException {

		final UserImpl user1 = createUser("userInvalidRate1", "userInvalidRate1", LocalDateTime.now(), "masculino",
				"coruña", "descripcion");

		final UserImpl user2 = createUser("userInvalidRate2", "userInvalidRate2", LocalDateTime.now(), "masculino",
				"coruña", "descripcion");

		userRepository.save(user1);
		userRepository.save(user2);

		assertThrows(InvalidRateException.class, () -> {
			userService.rateUser(-100, user1.getId(), user2.getId());
		});

		assertThrows(InvalidRateException.class, () -> {
			userService.rateUser(222, user1.getId(), user2.getId());
		});

	}

	@Test
	public void testRateUserNotYourFriendException()
			throws InstanceNotFoundException, InvalidRateException, ItsNotYourFriendException {

		final UserImpl user1 = createUser("userInvalidRate1", "userInvalidRate1", LocalDateTime.now(), "masculino",
				"coruña", "descripcion");

		final UserImpl user2 = createUser("userInvalidRate2", "userInvalidRate2", LocalDateTime.now(), "masculino",
				"coruña", "descripcion");

		userRepository.save(user1);
		userRepository.save(user2);

		assertThrows(ItsNotYourFriendException.class, () -> {
			userService.rateUser(1, user1.getId(), user2.getId());
		});



	}

}
