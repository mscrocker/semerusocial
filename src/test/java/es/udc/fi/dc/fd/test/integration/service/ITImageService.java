package es.udc.fi.dc.fd.test.integration.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import es.udc.fi.dc.fd.controller.exception.InstanceNotFoundException;
import es.udc.fi.dc.fd.controller.exception.ItsNotYourImageException;
import es.udc.fi.dc.fd.model.persistence.ImageImpl;
import es.udc.fi.dc.fd.model.persistence.UserImpl;
import es.udc.fi.dc.fd.service.Block;
import es.udc.fi.dc.fd.service.BlockImageByUserId;
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

	private UserImpl createUser(String userName, String password, LocalDateTime date, String sex, String city) {
		return new UserImpl(userName, password, date, sex, city);
	}
	private LocalDateTime getDateTime(int day, int month, int year) {
		return LocalDateTime.of(year, month, day, 00, 01);
	}

	private ImageImpl createImage(UserImpl user, byte[] image, String description) {
		return new ImageImpl(user, image, description);
	}

	private UserImpl signUp(String userName, String password, int age, String sex, String city) {

		UserImpl user = createUser(userName, password, getDateTime(1,1,2000), sex, city);

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
	public void testAddImageWithInstanceNotFoundException() {
		UserImpl user = createUser("userAddImgNotFoundException", "passAddImgNotFoundException", getDateTime(1,1,2000), "hombre", "coruna");

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

		assertEquals(image2.getDescription(), imageUpdated.getDescription());
		assertEquals(image2.getUser(), imageUpdated.getUser());
	}

	@Test
	public void testEditImageWithInstanceNotFoundException() {
		UserImpl user = signUp("userEditImageNotfound", "passEditImageNotfound", 1, "mujer", "ferrol");

		ImageImpl image2 = createImage(user, new byte[] { 4, 5, 6 }, "esto es otra descripcion");

		assertThrows(InstanceNotFoundException.class, () -> {
			imageService.editImage(image2, -1L, user.getId());
		});
	}

	@Test
	public void testEditImageWithInvalidImageException() throws InstanceNotFoundException {
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

		imageService.removeImage(imageCreated.getImageId(), user.getId());
		
		assertThrows(InstanceNotFoundException.class, () -> {
			imageService.removeImage(imageCreated.getImageId(), user.getId());
		});

	}

	@Test
	public void testRemoveImageWithInstanceNotFoundException() throws InstanceNotFoundException {
		UserImpl user = signUp("userRemoveImageINFE", "passRemoveImageINFE", 1, "hombre", "coruna");

		ImageImpl i = createImage(user, new byte[] { 1, 2, 3 }, "esto es una descripcion");

		ImageImpl imageCreated = imageService.addImage(i, user.getId());

		user.setId(-1L);

		assertThrows(InstanceNotFoundException.class, () -> {
			imageService.removeImage(imageCreated.getImageId(), user.getId());
		});
	}

	@Test
	public void testRemoveImageWithInvalidImageException() throws InstanceNotFoundException {
		UserImpl user = signUp("userRemoveImageIIE", "passRemoveImageIIE", 1, "hombre", "coruna");
		UserImpl user2 = signUp("userRemoveImageIIE2", "passRemoveImageIIE2", 1, "hombre", "coruna");
		
		ImageImpl i = createImage(user,new byte[] {1,2,3}, "esto es una descripcion");
		
		ImageImpl imageCreated = imageService.addImage(i,user.getId());
		
		assertThrows(ItsNotYourImageException.class,() -> {
			imageService.removeImage(imageCreated.getImageId(), user2.getId());
		});
	}
		
	//----- getImagesById -----
		
	@Test
	public void testGetImagesById() throws InstanceNotFoundException, ItsNotYourImageException {
		UserImpl user = signUp("userTestGet", "userTestGet", 2, "hombre", "coruna");

		ImageImpl i1 = createImage(user, new byte[] { 1, 2, 3 }, "esto es una descripcion1");
		ImageImpl i2 = createImage(user, new byte[] { 1, 2, 3 }, "esto es una descripcion2");
		ImageImpl i3 = createImage(user, new byte[] { 1, 2, 3 }, "esto es una descripcion3");
		ImageImpl i4 = createImage(user, new byte[] { 1, 2, 3 }, "esto es una descripcion4");
		ImageImpl i5 = createImage(user, new byte[] { 1, 2, 3 }, "esto es una descripcion5");
		ImageImpl i6 = createImage(user, new byte[] { 1, 2, 3 }, "esto es una descripcion6");
		ImageImpl i7 = createImage(user, new byte[] { 1, 2, 3 }, "esto es una descripcion7");
		ImageImpl i8 = createImage(user, new byte[] { 1, 2, 3 }, "esto es una descripcion8");
		ImageImpl i9 = createImage(user, new byte[] { 1, 2, 3 }, "esto es una descripcion9");
		ImageImpl i10 = createImage(user, new byte[] { 1, 2, 3 }, "esto es una descripcion10");

		ImageImpl imageCreated1 = imageService.addImage(i1, user.getId());
		ImageImpl imageCreated2 = imageService.addImage(i2, user.getId());
		ImageImpl imageCreated3 = imageService.addImage(i3, user.getId());
		ImageImpl imageCreated4 = imageService.addImage(i4, user.getId());
		ImageImpl imageCreated5 = imageService.addImage(i5, user.getId());
		ImageImpl imageCreated6 = imageService.addImage(i6, user.getId());
		ImageImpl imageCreated7 = imageService.addImage(i7, user.getId());
		ImageImpl imageCreated8 = imageService.addImage(i8, user.getId());
		ImageImpl imageCreated9 = imageService.addImage(i9, user.getId());
		ImageImpl imageCreated10 = imageService.addImage(i10, user.getId());
		
		Block<ImageImpl> imageListResult = imageService.getImagesByUserId(user.getId(), 0);

		List <ImageImpl> imageList = new ArrayList<ImageImpl>();
		imageList.add(imageCreated1);
		imageList.add(imageCreated2);
		imageList.add(imageCreated3);
		imageList.add(imageCreated4);
		imageList.add(imageCreated5);
		imageList.add(imageCreated6);
		imageList.add(imageCreated7);
		imageList.add(imageCreated8);
		imageList.add(imageCreated9);
		imageList.add(imageCreated10);
		
		assertEquals(imageListResult.getImages(),imageList);
		
		ImageImpl i11 = createImage(user, new byte[] { 1, 2, 3 }, "esto es una descripcion11");
		ImageImpl imageCreated11 = imageService.addImage(i11, user.getId());
		imageList.add(imageCreated11);
		imageListResult = imageService.getImagesByUserId(user.getId(), 0);
		
		assertEquals(imageListResult.getExistMoreImages(),true);
		
		UserImpl user2 = signUp("userTestGet2", "userTestGet2", 12, "hombre", "coruna");

		ImageImpl i12 = createImage(user2, new byte[] { 1, 2, 3 }, "esto es una descripcion12");
		ImageImpl i13 = createImage(user2, new byte[] { 1, 2, 3 }, "esto es una descripcion13");
		ImageImpl i14 = createImage(user2, new byte[] { 1, 2, 3 }, "esto es una descripcion14");
		ImageImpl i15 = createImage(user2, new byte[] { 1, 2, 3 }, "esto es una descripcion15");

		ImageImpl imageCreated12 = imageService.addImage(i12, user2.getId());
		ImageImpl imageCreated13 = imageService.addImage(i13, user2.getId());
		ImageImpl imageCreated14 = imageService.addImage(i14, user2.getId());
		ImageImpl imageCreated15 = imageService.addImage(i15, user2.getId());
		
		Block<ImageImpl> imageListResult2 = imageService.getImagesByUserId(user2.getId(), 0);

		List <ImageImpl> imageList2 = new ArrayList<ImageImpl>();
		imageList2.add(imageCreated12);
		imageList2.add(imageCreated13);
		imageList2.add(imageCreated14);
		imageList2.add(imageCreated15);
		
		assertEquals(imageListResult2.getImages(),imageList2);
	}
	
	@Test
	public void testGetImagesByIdWithInstanceNotFoundException() throws InstanceNotFoundException {
		UserImpl user = signUp("userTestGetImagesINFE", "userTestGetGetImagesINFE", 2, "hombre", "coruna");

		ImageImpl i1 = createImage(user, new byte[] { 1, 2, 3 }, "esto es una descripcion");
		
		imageService.addImage(i1, user.getId());
		
		user.setId(-1L);
		
		assertThrows(InstanceNotFoundException.class,() -> {
			imageService.getImagesByUserId(user.getId(), 0);
		});
	}
	
	//----- getImageById -----
	
	@Test
	public void testGetImageById() throws InstanceNotFoundException, ItsNotYourImageException {
		UserImpl user = signUp("userTestGetImage", "userTestGetImage", 2, "hombre", "coruna");

		ImageImpl i1 = createImage(user, new byte[] { 1, 2, 3 }, "esto es una descripcion1");
		ImageImpl i2 = createImage(user, new byte[] { 1, 2, 3 }, "esto es una descripcion2");
		ImageImpl i3 = createImage(user, new byte[] { 1, 2, 3 }, "esto es una descripcion3");
		
		ImageImpl imageCreated1 = imageService.addImage(i1, user.getId());
		ImageImpl imageCreated2 = imageService.addImage(i2, user.getId());
		ImageImpl imageCreated3 = imageService.addImage(i3, user.getId());
		
		BlockImageByUserId<ImageImpl> imageResult1 = imageService.getImageByUserId(imageCreated1.getImageId(), user.getId());
		BlockImageByUserId<ImageImpl> imageResult2 = imageService.getImageByUserId(imageCreated2.getImageId(), user.getId());
		BlockImageByUserId<ImageImpl> imageResult3 = imageService.getImageByUserId(imageCreated3.getImageId(), user.getId());
		
		assertEquals(imageCreated1,imageResult1.getImage());
		assertEquals(imageCreated2,imageResult2.getImage());
		assertEquals(imageCreated3,imageResult3.getImage());
		assertEquals(imageCreated1.getImageId(), imageResult2.getPrevId());
		assertEquals(imageCreated3.getImageId(), imageResult2.getNextId());
	}

	@Test
	public void testGetImageByIdWithInstanceNotFoundException() throws InstanceNotFoundException {
		UserImpl user = signUp("userTestGetImageINFE", "userTestGetImageINFE", 2, "hombre", "coruna");

		ImageImpl i1 = createImage(user, new byte[] { 1, 2, 3 }, "esto es una descripcion");
		
		ImageImpl imageResult=imageService.addImage(i1, user.getId());
		
		imageResult.setImageId(-1L);
		
		assertThrows(InstanceNotFoundException.class,() -> {
			imageService.getImageByUserId(imageResult.getImageId(), user.getId());
		});
	}
	
	@Test
	public void testGetImageByIdItsNotYourImageException() throws InstanceNotFoundException {
		UserImpl user1 = signUp("userTestGetImageINYI1", "userTestGetImageINYI1", 2, "hombre", "coruna");
		UserImpl user2 = signUp("userTestGetImageINYI2", "userTestGetImageINYI2", 2, "hombre", "coruna");

		ImageImpl i1 = createImage(user1, new byte[] { 1, 2, 3 }, "esto es una descripcion");

		ImageImpl imageResult=imageService.addImage(i1, user1.getId());
				
		assertThrows(ItsNotYourImageException.class,() -> {
			imageService.getImageByUserId(imageResult.getImageId(), user2.getId());
		});
	}
	
	//----- getFirstImageIdByUserId -----
	
	@Test
	public void testGetFirstImageIdByUserId() throws InstanceNotFoundException {
		UserImpl user1 = signUp("userGetFirstImageId1", "passGetFirstImageId1", 2, "hombre", "coruna");
		UserImpl user2 = signUp("userGetFirstImageId2", "passGetFirstImageId2", 2, "hombre", "coruna");

		ImageImpl i1 = createImage(user1, new byte[] { 1, 2, 3 }, "esto es una descripcion1");
		ImageImpl i2 = createImage(user1, new byte[] { 1, 2, 3 }, "esto es una descripcion2");
		ImageImpl i3 = createImage(user1, new byte[] { 1, 2, 3 }, "esto es una descripcion3");
		
		assertTrue(imageService.getFirstImageIdByUserId(user1.getId())==null);
		
		ImageImpl imageCreated1 = imageService.addImage(i1, user1.getId());
		imageService.addImage(i2, user1.getId());
		imageService.addImage(i3, user1.getId());
		
		Long idResult1 = imageService.getFirstImageIdByUserId(user1.getId());
		
		assertEquals(idResult1, imageCreated1.getImageId());
		
		ImageImpl i4 = createImage(user2, new byte[] { 1, 2, 3 }, "esto es una descripcion4");
		
		assertTrue(imageService.getFirstImageIdByUserId(user2.getId())==null);
		
		ImageImpl imageCreated4 = imageService.addImage(i4, user2.getId());
		
		Long idResult2 = imageService.getFirstImageIdByUserId(user2.getId());
		
		assertEquals(idResult2, imageCreated4.getImageId());
	}
	
	@Test
	public void testGetFirstImageIdByUserIdWithInstanceNotFoundException() {
		assertThrows(InstanceNotFoundException.class,() -> {
			imageService.getFirstImageIdByUserId(-1L);
		});
	}
	
}
