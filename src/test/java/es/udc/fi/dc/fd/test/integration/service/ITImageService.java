package es.udc.fi.dc.fd.test.integration.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
import es.udc.fi.dc.fd.controller.exception.InstanceNotFoundException;
import es.udc.fi.dc.fd.controller.exception.InvalidImageException;
import es.udc.fi.dc.fd.model.persistence.ImageImpl;
import es.udc.fi.dc.fd.model.persistence.UserImpl;
import es.udc.fi.dc.fd.service.Block;
import es.udc.fi.dc.fd.service.ImageService;
import es.udc.fi.dc.fd.service.UserService;

@RunWith(JUnitPlatform.class)
@ExtendWith(SpringExtension.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class })
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:context/service.xml", "classpath:context/persistence.xml",
		"classpath:context/application-context.xml" })
@TestPropertySource({ "classpath:config/persistence-access.properties", "classpath:config/service.properties" })
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

	// ----- addImage -----

	@Test
	public void testAddImage() throws InstanceNotFoundException, DuplicateInstanceException {
		UserImpl user = signUp("userAddImage", "passAddImage");

		ImageImpl i = createImage(user, new byte[] { 1, 2, 3 }, 1, "hombre", "coruna", "esto es una descripcion");

		ImageImpl imageCreated = imageService.addImage(i, user.getId());

		assertEquals(i.getImageId(), imageCreated.getImageId());
	}

	@Test
	public void testAddImageWithInstanceNotFoundException()
			throws InstanceNotFoundException, DuplicateInstanceException {
		UserImpl user = createUser("userAddImgNotFoundException", "passAddImgNotFoundException");

		user.setId(-1L);

		ImageImpl i = createImage(user, new byte[] { 1, 2, 3 }, 1, "hombre", "coruna", "esto es una descripcion");

		assertThrows(InstanceNotFoundException.class, () -> {
			imageService.addImage(i, i.getUser().getId());
		});
	}

	// ----- editImage -----

	@Test
	public void testEditImage() throws InstanceNotFoundException, InvalidImageException {
		UserImpl user = signUp("userEditImage", "passEditImage");

		ImageImpl image = createImage(user, new byte[] { 1, 2, 3 }, 1, "hombre", "coruna", "esto es una descripcion");
		ImageImpl image2 = createImage(user, new byte[] { 4, 5, 6 }, 1, "mujer", "ferrol", "esto es otra descripcion");

		ImageImpl imageCreated = imageService.addImage(image, user.getId());

		ImageImpl imageUpdated = imageService.editImage(image2, imageCreated.getImageId(), user.getId());

		assertEquals(image2, imageUpdated);

	}

	@Test
	public void testEditImageWithInstanceNotFoundException() throws InstanceNotFoundException, InvalidImageException {
		UserImpl user = signUp("userEditImageNotfound", "passEditImageNotfound");

		ImageImpl image2 = createImage(user, new byte[] { 4, 5, 6 }, 1, "mujer", "ferrol", "esto es otra descripcion");

		assertThrows(InstanceNotFoundException.class, () -> {
			imageService.editImage(image2, -1L, user.getId());
		});
	}

	@Test
	public void testEditImageWithInvalidImageException() throws InstanceNotFoundException, InvalidImageException {
		UserImpl user = signUp("userEditImageNo", "passEditImage");
		UserImpl user2 = signUp("invalid", "invalid");

		ImageImpl image1 = createImage(user, new byte[] { 4, 5, 6 }, 1, "mujer", "ferrol", "esto es otra descripcion");
		ImageImpl image2 = createImage(user2, new byte[] { 4, 5, 6 }, 1, "mujer", "ferrol", "esto es otra descripcion");

		ImageImpl imageCreated = imageService.addImage(image2, user2.getId());

		assertThrows(InvalidImageException.class, () -> {
			imageService.editImage(image1, imageCreated.getImageId(), user.getId());
		});
	}

	// ----- removeImage -----

	@Test
	public void testRemoveImage() throws InstanceNotFoundException, InvalidImageException {
		UserImpl user = signUp("userRemoveImage", "passRemoveImage");

		ImageImpl i = createImage(user, new byte[] { 1, 2, 3 }, 1, "hombre", "coruna", "esto es una descripcion");

		ImageImpl imageCreated = imageService.addImage(i, user.getId());

		imageService.removeImage(imageCreated, user.getId());

	}

	@Test
	public void testRemoveImageWithInstanceNotFoundException() throws InstanceNotFoundException, InvalidImageException {
		UserImpl user = signUp("userRemoveImageINFE", "passRemoveImageINFE");

		ImageImpl i = createImage(user, new byte[] { 1, 2, 3 }, 1, "hombre", "coruna", "esto es una descripcion");

		ImageImpl imageCreated = imageService.addImage(i, user.getId());

		user.setId(-1L);

		assertThrows(InstanceNotFoundException.class, () -> {
			imageService.removeImage(imageCreated, user.getId());
		});
	}

	@Test
	public void testRemoveImageWithInvalidImageException() throws InstanceNotFoundException, InvalidImageException {
		UserImpl user = signUp("userRemoveImageIIE", "passRemoveImageIIE");
		UserImpl user2 = signUp("userRemoveImageIIE2", "passRemoveImageIIE2");
		
		ImageImpl i = createImage(user,new byte[] {1,2,3}, 1, "hombre", "coruna", "esto es una descripcion");
		
		ImageImpl imageCreated = imageService.addImage(i,user.getId());
		
		assertThrows(InvalidImageException.class,() -> {
			imageService.removeImage(imageCreated,user2.getId());
		});
	}
		
		//----- getImagesById -----
		
	@Test
	public void testGetImagesById() throws InstanceNotFoundException {

		UserImpl user = signUp("userTestGet", "userTestGet");

		ImageImpl i1 = createImage(user, new byte[] { 1, 2, 3 }, 1, "hombre", "coruna", "esto es una descripcion");
		ImageImpl i2 = createImage(user, new byte[] { 1, 2, 3 }, 1, "hombre", "coruna", "esto es una descripcion");
		ImageImpl i3 = createImage(user, new byte[] { 1, 2, 3 }, 1, "hombre", "coruna", "esto es una descripcion");
		ImageImpl i4 = createImage(user, new byte[] { 1, 2, 3 }, 1, "hombre", "coruna", "esto es una descripcion");
		ImageImpl i5 = createImage(user, new byte[] { 1, 2, 3 }, 1, "hombre", "coruna", "esto es una descripcion");

		ImageImpl imageCreated1 = imageService.addImage(i1, user.getId());
		ImageImpl imageCreated2 = imageService.addImage(i2, user.getId());
		ImageImpl imageCreated3 = imageService.addImage(i3, user.getId());
		ImageImpl imageCreated4 = imageService.addImage(i4, user.getId());
		ImageImpl imageCreated5 = imageService.addImage(i5, user.getId());

		
		List<ImageImpl> imageListResult = imageService.getImagesByUserId(user.getId());

		List <ImageImpl> imageList = new ArrayList<ImageImpl>();
		imageList.add(imageCreated1);imageList.add(imageCreated2);imageList.add(imageCreated3);
		imageList.add(imageCreated4);imageList.add(imageCreated5);
		
		assertEquals(imageListResult,imageList);
	}

}
