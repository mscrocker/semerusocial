package es.udc.fi.dc.fd.test.integration.service;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;

import es.udc.fi.dc.fd.controller.exception.DuplicateInstanceException;
import es.udc.fi.dc.fd.controller.exception.IncorrectLoginException;
import es.udc.fi.dc.fd.controller.exception.InstanceNotFoundException;
import es.udc.fi.dc.fd.controller.exception.InvalidAgeException;
import es.udc.fi.dc.fd.controller.exception.InvalidDateException;
import es.udc.fi.dc.fd.dtos.LoginParamsDto;
import es.udc.fi.dc.fd.dtos.SearchCriteriaDto;
import es.udc.fi.dc.fd.model.SexCriteriaEnum;
import es.udc.fi.dc.fd.model.persistence.UserImpl;
import es.udc.fi.dc.fd.repository.CityCriteriaRepository;
import es.udc.fi.dc.fd.service.UserService;

@RunWith(JUnitPlatform.class)
@ExtendWith(SpringExtension.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class })
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:context/service.xml", "classpath:context/persistence.xml",
"classpath:context/application-context.xml" })
@TestPropertySource({ "classpath:config/persistence-access.properties", "classpath:config/service.properties" })
public class ITUserService {

	@Autowired
	private UserService userService;

	@Autowired
	private CityCriteriaRepository cityCriteriaRepository;

	@Autowired
	public ITUserService() {
		super();
	}

	private UserImpl createUser(String userName, String password, LocalDateTime date, String sex, String city,
			String description) {
		return new UserImpl(userName, password, date, sex, city, description);
	}

	private LocalDateTime getDateTime(int day, int month, int year) {
		return LocalDateTime.of(year, month, day, 00, 01);
	}

	private SearchCriteriaDto createCriteria(String sex, int minAge, int maxAge, List<String> CityList) {
		return new SearchCriteriaDto(sex, minAge, maxAge, CityList);
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

		user.setId(-1L);

		assertThrows(InstanceNotFoundException.class, () -> {
			userService.loginFromUserId(user.getId());
		});
	}

	// ----- setSearchCriteria -----

	@Test
	public void testSetSearchCriteria() throws DuplicateInstanceException, InvalidDateException,
	InstanceNotFoundException, InvalidAgeException {

		final UserImpl user = createUser("userSetSearchCriteria", "passwordSetSearchCriteria", getDateTime(1, 1, 2000),
				"hombre", "coruna", "description");
		final Long userId = userService.signUp(user);

		user.setCriteriaSex(SexCriteriaEnum.MALE);
		user.setCriteriaMaxAge(60);
		user.setCriteriaMinAge(30);

		final List<String> cityList = new ArrayList<>();
		cityList.add("A Coruna");
		cityList.add("Madrid");
		cityList.add("Vigo");
		final SearchCriteriaDto criteria = createCriteria("Male", 30, 60, cityList);

		userService.setSearchCriteria(userId, criteria);

		final List<String> registeredCities = cityCriteriaRepository.findCitiesByUserId(userId);

		UserImpl registeredUser = userService.loginFromUserId(userId);

		assertEquals(cityList, registeredCities);
		assertEquals(user, registeredUser);

		/////////////////////////////// Hacemos el registro de los mismos datos
		/////////////////////////////// ////////////////////
		userService.setSearchCriteria(userId, criteria);

		final List<String> registeredCities2 = cityCriteriaRepository.findCitiesByUserId(userId);

		registeredUser = userService.loginFromUserId(userId);

		assertEquals(cityList, registeredCities2);
		assertEquals(user, registeredUser);

		/////////////////////////////// Hacemos el registro de datos nuevos
		/////////////////////////////// ////////////////////
		cityList.add("Marruecos");
		final List<String> cityList2 = new ArrayList<>();
		cityList2.add("A Coruna");
		cityList2.add("Madrid");
		cityList2.add("Marruecos");
		cityList2.add("Vigo");
		final SearchCriteriaDto criteria2 = createCriteria("Male", 30, 60, cityList);
		userService.setSearchCriteria(userId, criteria2);

		final List<String> registeredCities3 = cityCriteriaRepository.findCitiesByUserId(userId);

		registeredUser = userService.loginFromUserId(userId);

		assertEquals(cityList2, registeredCities3);
		assertEquals(user, registeredUser);
	}

	@Test
	public void testSetSearchCriteriaInstanceNotFoundException() throws InvalidAgeException {

		final List<String> cityList = new ArrayList<>();
		cityList.add("A Coruna");
		cityList.add("Madrid");
		cityList.add("Vigo");
		final SearchCriteriaDto criteria = createCriteria("Male", 30, 60,cityList);

		assertThrows(InstanceNotFoundException.class,() -> {
			userService.setSearchCriteria(-1L, criteria);
		});
	}

	@Test
	public void testSetSearchCriteriaWithInvalidAgeException()
			throws InstanceNotFoundException, DuplicateInstanceException, InvalidDateException {

		final UserImpl user = createUser("CriteriaUserIAE", "CriteriaPassIAE",
				getDateTime(1, 1, 2000), "hombre", "coruna", "description");
		final Long userId = userService.signUp(user);

		user.setCriteriaSex(SexCriteriaEnum.MALE);
		user.setCriteriaMaxAge(60);
		user.setCriteriaMinAge(30);

		final List<String> cityList = new ArrayList<>();
		cityList.add("A Coruna");
		cityList.add("Madrid");
		cityList.add("Vigo");
		final SearchCriteriaDto criteria = createCriteria("Male", 30, 150, cityList);

		assertThrows(InvalidAgeException.class, () -> {
			userService.setSearchCriteria(userId, criteria);
		});

		final SearchCriteriaDto criteria2 = createCriteria("Male", 17, 60, cityList);

		assertThrows(InvalidAgeException.class, () -> {
			userService.setSearchCriteria(userId, criteria2);
		});
	}

	/*
	 * @Test public void testSetSearchCriteriaWithInvalidDateException() throws
	 * DuplicateInstanceException, InvalidDateException, InstanceNotFoundException,
	 * InvalidAgeException {
	 *
	 * final UserImpl user = createUser("userSetSearchCriteria",
	 * "passwordSetSearchCriteria", getDateTime(1, 1, 2000), "hombre", "coruna",
	 * "description"); final Long userId = userService.signUp(user);
	 *
	 * user.setCriteriaSex(SexCriteriaEnum.MALE); user.setCriteriaMaxAge(60);
	 * user.setCriteriaMinAge(30);
	 *
	 * final List<String> cityList = new ArrayList<>(); cityList.add("A Coruna");
	 * cityList.add("Madrid"); cityList.add("Vigo"); final SearchCriteriaDto
	 * criteria = createCriteria("Male", 30, 60, cityList);
	 *
	 * userService.setSearchCriteria(userId, criteria);
	 *
	 * final List<String> registeredCities =
	 * cityCriteriaRepository.findCitiesByUserId(userId);
	 *
	 * UserImpl registeredUser = userService.loginFromUserId(userId);
	 *
	 * assertEquals(cityList, registeredCities); assertEquals(user, registeredUser);
	 *
	 * /////////////////////////////// Hacemos el registro de los mismos datos
	 * /////////////////////////////// //////////////////// final List<String>
	 * emptyList = new ArrayList<>(); userService.setSearchCriteria(userId,
	 * criteria);
	 *
	 * final List<String> registeredCities2 =
	 * cityCriteriaRepository.findCitiesByUserId(userId);
	 *
	 * registeredUser = userService.loginFromUserId(userId);
	 *
	 * assertEquals(emptyList, registeredCities2); assertEquals(user,
	 * registeredUser);
	 *
	 * /////////////////////////////// Hacemos el registro de datos nuevos
	 * /////////////////////////////// ////////////////////
	 * cityList.add("Marruecos"); final List<String> marruecosList = new
	 * ArrayList<>(); marruecosList.add("Marruecos"); final SearchCriteriaDto
	 * criteria2 = createCriteria("Male", 30, 60, cityList);
	 * userService.setSearchCriteria(userId, criteria2);
	 *
	 * final List<String> registeredCities3 =
	 * cityCriteriaRepository.findCitiesByUserId(userId);
	 *
	 * registeredUser = userService.loginFromUserId(userId);
	 *
	 * assertEquals(marruecosList, registeredCities3); assertEquals(user,
	 * registeredUser); }
	 */
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

	public void testUpdateProfileWithInvalidDateException()
			throws DuplicateInstanceException, InvalidDateException {
		final UserImpl user = createUser("userUpdateProfileIDE", "passwordUpdateProfileIDE", getDateTime(1, 1, 2000),
				"hombre", "coruna", "descripcion");
		final UserImpl newUser = new UserImpl(LocalDateTime.now(), "mujer", "lugo", "descripcion editada");

		userService.signUp(user);

		assertThrows(InvalidDateException.class, () -> {
			userService.updateProfile(user.getId(), newUser);
		});
	}

}
