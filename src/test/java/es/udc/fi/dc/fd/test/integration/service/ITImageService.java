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
import es.udc.fi.dc.fd.controller.exception.ItsNotYourImageException;
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

	private UserImpl createUser(String userName, String password, int age, String sex, String city) {
		return new UserImpl(userName, password, age, sex, city);
	}

	private ImageImpl createImage(UserImpl user, byte[] image, String description) {
		return new ImageImpl(user, image, description);
	}

	private UserImpl signUp(String userName, String password, int age, String sex, String city) {

		UserImpl user = createUser(userName, password, age, sex, city);

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
		UserImpl user = signUp("userAddImage", "passAddImage", 1, "hombre", "coruna");

		ImageImpl i = createImage(user, new byte[] { 1, 2, 3 }, "esto es una descripcion");

		ImageImpl imageCreated = imageService.addImage(i, user.getId());

		assertEquals(i, imageCreated);
	}

	@Test
	public void testAddImageWithInstanceNotFoundException()
			throws InstanceNotFoundException, DuplicateInstanceException {
		UserImpl user = createUser("userAddImgNotFoundException", "passAddImgNotFoundException", 1, "hombre", "coruna");

		user.setId(-1L);

		ImageImpl i = createImage(user, new byte[] { 1, 2, 3 }, "esto es una descripcion");

		assertThrows(InstanceNotFoundException.class, () -> {
			imageService.addImage(i, i.getUser().getId());
		});
	}

	// ----- editImage -----

	@Test
	public void testEditImage() throws InstanceNotFoundException, ItsNotYourImageException {
		UserImpl user = signUp("userEditImage", "passEditImage", 1, "hombre", "coruna");

		ImageImpl image = createImage(user, new byte[] { 1, 2, 3 }, "esto es una descripcion");
		ImageImpl image2 = createImage(user, new byte[] { 4, 5, 6 }, "esto es otra descripcion");

		ImageImpl imageCreated = imageService.addImage(image, user.getId());

		ImageImpl imageUpdated = imageService.editImage(image2, imageCreated.getImageId(), user.getId());

		assertEquals(image2, imageUpdated);

	}

	@Test
	public void testEditImageWithInstanceNotFoundException() throws InstanceNotFoundException, ItsNotYourImageException {
		UserImpl user = signUp("userEditImageNotfound", "passEditImageNotfound", 1, "mujer", "ferrol");

		ImageImpl image2 = createImage(user, new byte[] { 4, 5, 6 }, "esto es otra descripcion");

		assertThrows(InstanceNotFoundException.class, () -> {
			imageService.editImage(image2, -1L, user.getId());
		});
	}

	@Test
	public void testEditImageWithInvalidImageException() throws InstanceNotFoundException, ItsNotYourImageException {
		UserImpl user = signUp("userEditImageNo", "passEditImage", 1, "mujer", "ferrol");
		UserImpl user2 = signUp("invalid", "invalid", 1, "mujer", "ferrol");

		ImageImpl image1 = createImage(user, new byte[] { 4, 5, 6 }, "esto es otra descripcion");
		ImageImpl image2 = createImage(user2, new byte[] { 4, 5, 6 }, "esto es otra descripcion");

		ImageImpl imageCreated = imageService.addImage(image2, user2.getId());

		assertThrows(ItsNotYourImageException.class, () -> {
			imageService.editImage(image1, imageCreated.getImageId(), user.getId());
		});
	}

	// ----- removeImage -----

	@Test
	public void testRemoveImage() throws InstanceNotFoundException, ItsNotYourImageException {
		UserImpl user = signUp("userRemoveImage", "passRemoveImage", 1, "hombre", "coruna");

		ImageImpl i = createImage(user, new byte[] { 1, 2, 3 }, "esto es una descripcion");

		ImageImpl imageCreated = imageService.addImage(i, user.getId());

		imageService.removeImage(imageCreated, user.getId());

	}

	@Test
	public void testRemoveImageWithInstanceNotFoundException() throws InstanceNotFoundException, ItsNotYourImageException {
		UserImpl user = signUp("userRemoveImageINFE", "passRemoveImageINFE", 1, "hombre", "coruna");

		ImageImpl i = createImage(user, new byte[] { 1, 2, 3 }, "esto es una descripcion");

		ImageImpl imageCreated = imageService.addImage(i, user.getId());

		user.setId(-1L);

		assertThrows(InstanceNotFoundException.class, () -> {
			imageService.removeImage(imageCreated, user.getId());
		});
	}

	@Test
	public void testRemoveImageWithInvalidImageException() throws InstanceNotFoundException, ItsNotYourImageException {
		UserImpl user = signUp("userRemoveImageIIE", "passRemoveImageIIE", 1, "hombre", "coruna");
		UserImpl user2 = signUp("userRemoveImageIIE2", "passRemoveImageIIE2", 1, "hombre", "coruna");
		
		ImageImpl i = createImage(user,new byte[] {1,2,3}, "esto es una descripcion");
		
		ImageImpl imageCreated = imageService.addImage(i,user.getId());
		
		assertThrows(ItsNotYourImageException.class,() -> {
			imageService.removeImage(imageCreated,user2.getId());
		});
	}
		
	//----- getImagesById -----
		
	@Test
	public void testGetImagesById() throws InstanceNotFoundException {

		UserImpl user = signUp("userTestGet", "userTestGet", 2, "hombre", "coruna");

		ImageImpl i1 = createImage(user, new byte[] { 1, 2, 3 }, "esto es una descripcion");
		ImageImpl i2 = createImage(user, new byte[] { 1, 2, 3 }, "esto es una descripcion");
		ImageImpl i3 = createImage(user, new byte[] { 1, 2, 3 }, "esto es una descripcion");
		ImageImpl i4 = createImage(user, new byte[] { 1, 2, 3 }, "esto es una descripcion");
		ImageImpl i5 = createImage(user, new byte[] { 1, 2, 3 }, "esto es una descripcion");

		ImageImpl imageCreated1 = imageService.addImage(i1, user.getId());
		ImageImpl imageCreated2 = imageService.addImage(i2, user.getId());
		ImageImpl imageCreated3 = imageService.addImage(i3, user.getId());
		ImageImpl imageCreated4 = imageService.addImage(i4, user.getId());
		ImageImpl imageCreated5 = imageService.addImage(i5, user.getId());

		
		Block<ImageImpl> imageListResult = imageService.getImagesByUserId(user.getId(), 0);

		List <ImageImpl> imageList = new ArrayList<ImageImpl>();
		imageList.add(imageCreated1);
		imageList.add(imageCreated2);
		imageList.add(imageCreated3);
		imageList.add(imageCreated4);
		imageList.add(imageCreated5);
		
		assertEquals(imageListResult.getItems(),imageList);
		
		UserImpl user2 = signUp("userTestGet2", "userTestGet2", 12, "hombre", "coruna");

		ImageImpl i6 = createImage(user2, new byte[] { 1, 2, 3 }, "esto es una descripcion");
		ImageImpl i7 = createImage(user2, new byte[] { 1, 2, 3 }, "esto es una descripcion");
		ImageImpl i8 = createImage(user2, new byte[] { 1, 2, 3 }, "esto es una descripcion");

		ImageImpl imageCreated6 = imageService.addImage(i6, user2.getId());
		ImageImpl imageCreated7 = imageService.addImage(i7, user2.getId());
		ImageImpl imageCreated8 = imageService.addImage(i8, user2.getId());

		
		Block<ImageImpl> imageListResult2 = imageService.getImagesByUserId(user2.getId(), 0);

		List <ImageImpl> imageList2 = new ArrayList<ImageImpl>();
		imageList2.add(imageCreated6);
		imageList2.add(imageCreated7);
		imageList2.add(imageCreated8);
		
		assertEquals(imageListResult2.getItems(),imageList2);
	}

}
