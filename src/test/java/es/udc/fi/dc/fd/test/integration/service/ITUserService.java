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
	
	private UserImpl createUser(String userName, String password, LocalDateTime date, String sex, String city) {
		return new UserImpl(userName, password, date, sex, city);
	}
	private LocalDateTime getDateTime(int day, int month, int year) {
		return LocalDateTime.of(year, month, day, 00, 01);
	}
	
	//----- signUp -----

	@Test
	public void testSignUpAndLoginFromUserName() throws DuplicateInstanceException, InstanceNotFoundException, InvalidDateException {
		UserImpl user = createUser("usuarioSignUpAndLoginFromId","contraseñaSignUpAndLoginFromId", getDateTime(1,1,2000), "hombre", "coruna");
		
		userService.signUp(user);
		
		UserImpl loggedInUser = userService.loginFromUserId(user.getId());
		assertEquals(user, loggedInUser);
	}
	
	@Test
	public void testSignUpDuplicatedUserName() throws DuplicateInstanceException, InvalidDateException {
		UserImpl user = createUser("usuarioSignUpDuplicated","contraseñaSignUpDuplicated", getDateTime(1,1,2000), "hombre", "coruna");
		userService.signUp(user);
		
		assertThrows(DuplicateInstanceException.class,() -> {
			userService.signUp(user);
		});
	}
	
	@Test
	public void testSignUpInvalidDateException() throws DuplicateInstanceException, InvalidDateException {
		UserImpl user = createUser("usuarioSignUpIDE","contraseñaSignUpIDE", LocalDateTime.now(),"nombre", "coruna");
		assertThrows(InvalidDateException.class,() -> {
			userService.signUp(user);
		});
	}
	
	//----- login -----
	
	@Test
	public void testLogin() throws DuplicateInstanceException, IncorrectLoginException, InvalidDateException {
		UserImpl user = createUser("usuarioLogin","contraseñaLogin", getDateTime(1,1,2000), "hombre", "coruna");
		String clearPassword = user.getPassword();

		userService.signUp(user);

		LoginParamsDto loginDto = new LoginParamsDto();
		loginDto.setUserName(user.getUserName());
		loginDto.setPassword(clearPassword);
		
		UserImpl loggedInUser = userService.login(loginDto);

		assertEquals(user, loggedInUser);
	}
	
	@Test
	public void testLoginWithIncorrectPassword() throws DuplicateInstanceException, InvalidDateException {
		UserImpl user = createUser("usuarioLoginIncorrectPass","contraseñaLoginIncorrectPass", getDateTime(1,1,2000), "hombre", "coruna");
		String clearPassword = user.getPassword();
		
		userService.signUp(user);
		
		LoginParamsDto loginDto = new LoginParamsDto();
		loginDto.setUserName(user.getUserName());
		loginDto.setPassword("xd" + clearPassword);
		
		assertThrows(IncorrectLoginException.class,() -> {
			userService.login(loginDto);
		});
	}
	
	//----- loginFromUserId -----
	
	@Test
	public void testLoginFromUserId() throws DuplicateInstanceException, InstanceNotFoundException, InvalidDateException {
		UserImpl user = createUser("userLoginFromUserId","passwordLoginFromUserId", getDateTime(1,1,2000), "hombre", "coruna");
		
		userService.signUp(user);
		
		userService.loginFromUserId(user.getId());
	}
	
	@Test
	public void testLoginFromUserIdWithInstanceNotFoundException() throws DuplicateInstanceException, InvalidDateException {
		UserImpl user = createUser("userLoginFromUserIdINFE","passwordLoginFromUserIdINFE", getDateTime(1,1,2000), "hombre", "coruna");
		
		userService.signUp(user);
		
		user.setId(-1L);
		
		assertThrows(InstanceNotFoundException.class,() -> {
			userService.loginFromUserId(user.getId());
		});
	}
	
}
