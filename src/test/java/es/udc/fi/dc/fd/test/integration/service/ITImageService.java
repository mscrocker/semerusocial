package es.udc.fi.dc.fd.test.integration.service;

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
import es.udc.fi.dc.fd.model.persistence.ImageImpl;
import es.udc.fi.dc.fd.model.persistence.UserImpl;
import es.udc.fi.dc.fd.service.ImageServiceImpl;
import es.udc.fi.dc.fd.service.UserServiceImpl;

@RunWith(JUnitPlatform.class)
@ExtendWith(SpringExtension.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class })
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:context/service.xml",
        "classpath:context/persistence.xml",
        "classpath:context/application-context.xml" })
@TestPropertySource({ "classpath:config/persistence-access.properties",
        "classpath:config/service.properties" })
public class ITImageService {
	/*
	@Autowired
	private UserServiceImpl userService;
	
	@Autowired
	private ImageServiceImpl imageService;
	
	public ITImageService() {
        super();
    }
	
	private UserImpl createUser(String userName, String password) {
		return new UserImpl(userName, password);
	}
	
	private ImageImpl createImage(UserImpl user, byte[] image, int age, String sex, String city, String description) {
		return new ImageImpl(user, image, age, sex, city, description);
	}
	
	private UserImpl signUp(String userName, String password) {
		
		UserImpl user = createUser(userName, password);
		
		try {
			userService.signUp(user);
		} catch (DuplicateInstanceException e) {
			throw new RuntimeException(e);
		}
		
		return user;
		
	}
	
	//----- addImage -----
	/*
	@Test
	public void testAddImage() throws InstanceNotFoundException, DuplicateInstanceException {
		
		userService.signUp(createUser("usuario", "contrasena"));
		ImageImpl i = createImage(signUp("usuario", "contrasena"),new byte[] {1,2,3}, 1, "hombre", "coruna", "esto es una descripcion");
		ImageImpl imageCreated = imageService.addImage(i,i.getUser().getUserId());
		assertEquals(i, imageCreated);
		
	}
	
	@Test
	public void testAddImageWithInstanceNotFoundException(ImageImpl image, Long userId) throws InstanceNotFoundException, DuplicateInstanceException {
		UserImpl user = createUser("usuario", "contrasena");
		user.setUserId(-1L);
		
		ImageImpl i = createImage(user,new byte[] {1,2,3}, 1, "hombre", "coruna", "esto es una descripcion");
		imageService.addImage(i,i.getUser().getUserId());
	}
	*/
}
