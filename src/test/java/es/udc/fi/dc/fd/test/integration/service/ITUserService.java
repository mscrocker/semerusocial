package es.udc.fi.dc.fd.test.integration.service;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
import es.udc.fi.dc.fd.model.User;
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
	
	private UserImpl createUser(String userName, String password) {
		return new UserImpl(userName, password);
	}
	
	
	//----- signUp -----

	@Test
	public void testSignUpAndLoginFromUserName() throws DuplicateInstanceException, InstanceNotFoundException {
		UserImpl user = createUser("usuarioSignUpAndLoginFromId","contraseñaSignUpAndLoginFromId");

		userService.signUp(user);
		
		User loggedInUser = userService.loginFromUserName(user.getUserName());
		
		assertEquals(user.getUserName(), loggedInUser.getUserName());
	}
	
	@Test
	public void testSignUpDuplicatedUserName() throws DuplicateInstanceException {
		UserImpl user = createUser("usuarioSignUpDuplicated","contraseñaSignUpDuplicated");
		userService.signUp(user);
		
		assertThrows(DuplicateInstanceException.class,() -> {
			userService.signUp(user);
		});
	}
	
	
	//----- login -----
	
	@Test
	public void testLogin() throws DuplicateInstanceException, IncorrectLoginException {
		UserImpl user = createUser("usuarioLogin","contraseñaLogin");
		String clearPassword = user.getPassword();

		userService.signUp(user);

		UserImpl loggedInUser = userService.login(user.getUserName(), clearPassword);

		assertEquals(user.getUserName(), loggedInUser.getUserName());
	}
	
	@Test
	public void testLoginWithIncorrectPassword() throws DuplicateInstanceException, IncorrectLoginException {
		UserImpl user = createUser("usuarioLoginIncorrectPass","contraseñaLoginIncorrectPass");
		String clearPassword = user.getPassword();
		
		userService.signUp(user);
		assertThrows(IncorrectLoginException.class,() -> {
			userService.login(user.getUserName(), "xd" + clearPassword);
		});
	}
	
	@Test
	public void testLoginWithNonExistentUserName() {
		assertThrows(InstanceNotFoundException.class,() -> {
			userService.loginFromUserName("usuario que no existe");
		});
	}
	
}
