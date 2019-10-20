package es.udc.fi.dc.fd.test.integration.service;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

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
import es.udc.fi.dc.fd.controller.exception.InvalidDateException;
import es.udc.fi.dc.fd.dtos.LoginParamsDto;
import es.udc.fi.dc.fd.model.persistence.UserImpl;
import es.udc.fi.dc.fd.service.UserService;

@RunWith(JUnitPlatform.class)
@ExtendWith(SpringExtension.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class })
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:context/service.xml",
		"classpath:context/persistence.xml",
"classpath:context/application-context.xml" })
@TestPropertySource({ "classpath:config/persistence-access.properties",
"classpath:config/service.properties" })
public class ITUserService {

	@Autowired
	private UserService userService;

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

	//----- signUp -----

	@Test
	public void testSignUpAndLoginFromUserName() throws DuplicateInstanceException, InstanceNotFoundException, InvalidDateException {
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

		assertThrows(DuplicateInstanceException.class,() -> {
			userService.signUp(user);
		});
	}

	@Test
	public void testSignUpInvalidDateException() throws DuplicateInstanceException, InvalidDateException {
		final UserImpl user = createUser("usuarioSignUpIDE", "contraseñaSignUpIDE", LocalDateTime.now(), "nombre",
				"coruna", "descripcion");
		assertThrows(InvalidDateException.class,() -> {
			userService.signUp(user);
		});
	}

	//----- login -----

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

		assertThrows(IncorrectLoginException.class,() -> {
			userService.login(loginDto);
		});
	}

	//----- loginFromUserId -----

	@Test
	public void testLoginFromUserId() throws DuplicateInstanceException, InstanceNotFoundException, InvalidDateException {
		final UserImpl user = createUser("userLoginFromUserId", "passwordLoginFromUserId", getDateTime(1, 1, 2000),
				"hombre", "coruna", "descripcion");

		userService.signUp(user);

		userService.loginFromUserId(user.getId());
	}

	@Test
	public void testLoginFromUserIdWithInstanceNotFoundException() throws DuplicateInstanceException, InvalidDateException {
		final UserImpl user = createUser("userLoginFromUserIdINFE", "passwordLoginFromUserIdINFE",
				getDateTime(1, 1, 2000), "hombre", "coruna", "descripcion");

		userService.signUp(user);

		user.setId(-1L);

		assertThrows(InstanceNotFoundException.class,() -> {
			userService.loginFromUserId(user.getId());
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

	@Test
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
