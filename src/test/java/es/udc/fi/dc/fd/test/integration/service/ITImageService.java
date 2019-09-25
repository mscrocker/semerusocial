package es.udc.fi.dc.fd.test.integration.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import es.udc.fi.dc.fd.controller.exception.InstanceNotFoundException;
import es.udc.fi.dc.fd.controller.exception.InvalidImageException;
import es.udc.fi.dc.fd.model.persistence.ImageImpl;
import es.udc.fi.dc.fd.model.persistence.UserImpl;
import es.udc.fi.dc.fd.service.ImageService;
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
public class ITImageService {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ImageService imageService;
	
	@Autowired
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
	
	@Test
	public void testAddImage() throws InstanceNotFoundException, DuplicateInstanceException {
		UserImpl user = signUp("userAddImage", "passAddImage");
		
		ImageImpl i = createImage(user,new byte[] {1,2,3}, 1, "hombre", "coruna", "esto es una descripcion");
				
		ImageImpl imageCreated = imageService.addImage(i,user.getUserId());
		
		assertEquals(i.getImageId(), imageCreated.getImageId());
	}
	
	@Test
	public void testAddImageWithInstanceNotFoundException() throws InstanceNotFoundException, DuplicateInstanceException {
		UserImpl user = createUser("userAddImgNotFoundException", "passAddImgNotFoundException");

		user.setUserId(-1L);

		ImageImpl i = createImage(user,new byte[] {1,2,3}, 1, "hombre", "coruna", "esto es una descripcion");

		assertThrows(InstanceNotFoundException.class,() -> {
			imageService.addImage(i,i.getUser().getUserId());
		});
	}
	
	
	//----- editImage -----
	/*
	@Test
	public void testEditImage() throws InstanceNotFoundException, InvalidImageException {
		UserImpl user = signUp("userEditImage", "passEditImage");
		
		ImageImpl image = createImage(user,new byte[] {1,2,3}, 1, "hombre", "coruna", "esto es una descripcion");
		ImageImpl image2 = createImage(user,new byte[] {4,5,6}, 1, "mujer", "ferrol", "esto es otra descripcion");
		
		ImageImpl imageCreated = imageService.addImage(image,user.getUserId());
		
		ImageImpl imageUpdated = imageService.editImage(image2, imageCreated.getImageId(), user.getUserId());
		
		assertEquals(imageCreated.getSex(), imageUpdated.getSex());
		assertEquals(imageCreated.getImageId(), imageUpdated.getImageId());
	}
	*/
	
	//----- removeImage -----
	
	@Test
	public void testRemoveImage() throws InstanceNotFoundException, InvalidImageException {
		UserImpl user = signUp("userRemoveImage", "passRemoveImage");
		
		ImageImpl i = createImage(user,new byte[] {1,2,3}, 1, "hombre", "coruna", "esto es una descripcion");
		
		ImageImpl imageCreated = imageService.addImage(i,user.getUserId());
				
		imageService.removeImage(imageCreated,user.getUserId());
		
	}
	
	@Test
	public void testRemoveImageWithInstanceNotFoundException() throws InstanceNotFoundException, InvalidImageException {
		UserImpl user = signUp("userRemoveImageINFE", "passRemoveImageINFE");
		
		ImageImpl i = createImage(user,new byte[] {1,2,3}, 1, "hombre", "coruna", "esto es una descripcion");
		
		ImageImpl imageCreated = imageService.addImage(i,user.getUserId());
		
		user.setUserId(-1L);
		
		assertThrows(InstanceNotFoundException.class,() -> {
			imageService.removeImage(imageCreated,user.getUserId());
		});
	}
	
	@Test
	public void testRemoveImageWithInvalidImageException() throws InstanceNotFoundException, InvalidImageException {
		UserImpl user = signUp("userRemoveImageIIE", "passRemoveImageIIE");
		UserImpl user2 = signUp("userRemoveImageIIE2", "passRemoveImageIIE2");
		
		ImageImpl i = createImage(user,new byte[] {1,2,3}, 1, "hombre", "coruna", "esto es una descripcion");
		
		ImageImpl imageCreated = imageService.addImage(i,user.getUserId());
		
		assertThrows(InvalidImageException.class,() -> {
			imageService.removeImage(imageCreated,user2.getUserId());
		});
	}
	
}
