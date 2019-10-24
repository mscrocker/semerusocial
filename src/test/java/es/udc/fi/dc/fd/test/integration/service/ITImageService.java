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
import es.udc.fi.dc.fd.controller.exception.InstanceNotFoundException;
import es.udc.fi.dc.fd.controller.exception.InvalidDateException;
import es.udc.fi.dc.fd.controller.exception.ItsNotYourImageException;
import es.udc.fi.dc.fd.model.persistence.ImageImpl;
import es.udc.fi.dc.fd.model.persistence.UserImpl;
import es.udc.fi.dc.fd.service.Block;
import es.udc.fi.dc.fd.service.BlockImageByUserId;
import es.udc.fi.dc.fd.service.ImageService;
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
public class ITImageService {

	@Autowired
	private UserService userService;

	@Autowired
	private ImageService imageService;

	@Autowired
	public ITImageService() {
		super();
	}

	private UserImpl createUser(String userName, String password, LocalDateTime date, String sex, String city,
			String description) {
		return new UserImpl(userName, password, date, sex, city, description);
	}

	private LocalDateTime getDateTime(int day, int month, int year) {
		return LocalDateTime.of(year, month, day, 00, 01);
	}

	private ImageImpl createImage(UserImpl user, byte[] image) {
		return new ImageImpl(user, image, "jpg");
	}

	private UserImpl signUp(String userName, String password, int age, String sex, String city) {

		final UserImpl user = createUser(userName, password, getDateTime(1, 1, 2000), sex, city, "descripcion");

		try {
			userService.signUp(user);
		} catch (DuplicateInstanceException | InvalidDateException e) {
			throw new RuntimeException(e);
		}

		return user;

	}

	// ----- addImage -----

	@Test
	public void testAddImage() throws InstanceNotFoundException, DuplicateInstanceException {
		final UserImpl user = signUp("userAddImage", "passAddImage", 1, "hombre", "coruna");

		final ImageImpl i = createImage(user, new byte[] { 1, 2, 3 });

		final ImageImpl imageCreated = imageService.addImage(i, user.getId());

		assertEquals(i, imageCreated);
	}

	@Test
	public void testAddImageWithInstanceNotFoundException() {
		final UserImpl user = createUser("userAddImgNotFoundException", "passAddImgNotFoundException",
				getDateTime(1, 1, 2000), "hombre", "coruna", "descripcion");

//		user.setId(-1L);

		final ImageImpl i = createImage(user, new byte[] { 1, 2, 3 });

		assertThrows(InstanceNotFoundException.class, () -> {
			imageService.addImage(i, -1L);
		});
	}

	// ----- removeImage -----

	@Test
	public void testRemoveImage() throws InstanceNotFoundException, ItsNotYourImageException {
		final UserImpl user = signUp("userRemoveImage", "passRemoveImage", 1, "hombre", "coruna");

		final ImageImpl i = createImage(user, new byte[] { 1, 2, 3 });

		final ImageImpl imageCreated = imageService.addImage(i, user.getId());

		imageService.removeImage(imageCreated.getImageId(), user.getId());

		assertThrows(InstanceNotFoundException.class, () -> {
			imageService.removeImage(imageCreated.getImageId(), user.getId());
		});

	}

	@Test
	public void testRemoveImageWithInstanceNotFoundException() throws InstanceNotFoundException {
		final UserImpl user = signUp("userRemoveImageINFE", "passRemoveImageINFE", 1, "hombre", "coruna");

		final ImageImpl i = createImage(user, new byte[] { 1, 2, 3 });

		final ImageImpl imageCreated = imageService.addImage(i, user.getId());

		// user.setId(-1L);

		assertThrows(InstanceNotFoundException.class, () -> {
			imageService.removeImage(imageCreated.getImageId(), -1L);
		});
	}

	@Test
	public void testRemoveImageWithInvalidImageException() throws InstanceNotFoundException {
		final UserImpl user = signUp("userRemoveImageIIE", "passRemoveImageIIE", 1, "hombre", "coruna");
		final UserImpl user2 = signUp("userRemoveImageIIE2", "passRemoveImageIIE2", 1, "hombre", "coruna");

		final ImageImpl i = createImage(user, new byte[] { 1, 2, 3 });

		final ImageImpl imageCreated = imageService.addImage(i, user.getId());

		assertThrows(ItsNotYourImageException.class, () -> {
			imageService.removeImage(imageCreated.getImageId(), user2.getId());
		});
	}

	// ----- getImagesById -----

	@Test
	public void testGetImagesById() throws InstanceNotFoundException, ItsNotYourImageException {
		final UserImpl user = signUp("userTestGet", "userTestGet", 2, "hombre", "coruna");

		final ImageImpl i1 = createImage(user, new byte[] { 1, 2, 3 });
		final ImageImpl i2 = createImage(user, new byte[] { 1, 2, 3 });
		final ImageImpl i3 = createImage(user, new byte[] { 1, 2, 3 });
		final ImageImpl i4 = createImage(user, new byte[] { 1, 2, 3 });
		final ImageImpl i5 = createImage(user, new byte[] { 1, 2, 3 });
		final ImageImpl i6 = createImage(user, new byte[] { 1, 2, 3 });
		final ImageImpl i7 = createImage(user, new byte[] { 1, 2, 3 });
		final ImageImpl i8 = createImage(user, new byte[] { 1, 2, 3 });
		final ImageImpl i9 = createImage(user, new byte[] { 1, 2, 3 });
		final ImageImpl i10 = createImage(user, new byte[] { 1, 2, 3 });

		final ImageImpl imageCreated1 = imageService.addImage(i1, user.getId());
		final ImageImpl imageCreated2 = imageService.addImage(i2, user.getId());
		final ImageImpl imageCreated3 = imageService.addImage(i3, user.getId());
		final ImageImpl imageCreated4 = imageService.addImage(i4, user.getId());
		final ImageImpl imageCreated5 = imageService.addImage(i5, user.getId());
		final ImageImpl imageCreated6 = imageService.addImage(i6, user.getId());
		final ImageImpl imageCreated7 = imageService.addImage(i7, user.getId());
		final ImageImpl imageCreated8 = imageService.addImage(i8, user.getId());
		final ImageImpl imageCreated9 = imageService.addImage(i9, user.getId());
		final ImageImpl imageCreated10 = imageService.addImage(i10, user.getId());

		Block<ImageImpl> imageListResult = imageService.getImagesByUserId(user.getId(), 0);

		final List<ImageImpl> imageList = new ArrayList<>();
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

		assertEquals(imageListResult.getImages(), imageList);

		final ImageImpl i11 = createImage(user, new byte[] { 1, 2, 3 });
		final ImageImpl imageCreated11 = imageService.addImage(i11, user.getId());
		imageList.add(imageCreated11);
		imageListResult = imageService.getImagesByUserId(user.getId(), 0);

		assertEquals(imageListResult.getExistMoreImages(), true);

		final UserImpl user2 = signUp("userTestGet2", "userTestGet2", 12, "hombre", "coruna");

		final ImageImpl i12 = createImage(user2, new byte[] { 1, 2, 3 });
		final ImageImpl i13 = createImage(user2, new byte[] { 1, 2, 3 });
		final ImageImpl i14 = createImage(user2, new byte[] { 1, 2, 3 });
		final ImageImpl i15 = createImage(user2, new byte[] { 1, 2, 3 });

		final ImageImpl imageCreated12 = imageService.addImage(i12, user2.getId());
		final ImageImpl imageCreated13 = imageService.addImage(i13, user2.getId());
		final ImageImpl imageCreated14 = imageService.addImage(i14, user2.getId());
		final ImageImpl imageCreated15 = imageService.addImage(i15, user2.getId());

		final Block<ImageImpl> imageListResult2 = imageService.getImagesByUserId(user2.getId(), 0);

		final List<ImageImpl> imageList2 = new ArrayList<>();
		imageList2.add(imageCreated12);
		imageList2.add(imageCreated13);
		imageList2.add(imageCreated14);
		imageList2.add(imageCreated15);

		assertEquals(imageListResult2.getImages(), imageList2);
	}

	@Test
	public void testGetImagesByIdWithInstanceNotFoundException() throws InstanceNotFoundException {
		final UserImpl user = signUp("userTestGetImagesINFE", "userTestGetGetImagesINFE", 2, "hombre", "coruna");

		final ImageImpl i1 = createImage(user, new byte[] { 1, 2, 3 });

		imageService.addImage(i1, user.getId());

		// user.setId(-1L);

		assertThrows(InstanceNotFoundException.class, () -> {
			imageService.getImagesByUserId(-1L, 0);
		});
	}

	// ----- getImageById -----

	@Test
	public void testGetImageById() throws InstanceNotFoundException, ItsNotYourImageException {
		final UserImpl user = signUp("userTestGetImage", "userTestGetImage", 2, "hombre", "coruna");

		final ImageImpl i1 = createImage(user, new byte[] { 1, 2, 3 });
		final ImageImpl i2 = createImage(user, new byte[] { 1, 2, 3 });
		final ImageImpl i3 = createImage(user, new byte[] { 1, 2, 3 });

		final ImageImpl imageCreated1 = imageService.addImage(i1, user.getId());
		final ImageImpl imageCreated2 = imageService.addImage(i2, user.getId());
		final ImageImpl imageCreated3 = imageService.addImage(i3, user.getId());

		final BlockImageByUserId<ImageImpl> imageResult1 = imageService.getImageByUserId(imageCreated1.getImageId(),
				user.getId());
		final BlockImageByUserId<ImageImpl> imageResult2 = imageService.getImageByUserId(imageCreated2.getImageId(),
				user.getId());
		final BlockImageByUserId<ImageImpl> imageResult3 = imageService.getImageByUserId(imageCreated3.getImageId(),
				user.getId());

		assertEquals(imageCreated1, imageResult1.getImage());
		assertEquals(imageCreated2, imageResult2.getImage());
		assertEquals(imageCreated3, imageResult3.getImage());
		assertEquals(imageCreated1.getImageId(), imageResult2.getPrevId());
		assertEquals(imageCreated3.getImageId(), imageResult2.getNextId());
	}

	@Test
	public void testGetImageByIdWithInstanceNotFoundException() throws InstanceNotFoundException {
		final UserImpl user = signUp("userTestGetImageINFE", "userTestGetImageINFE", 2, "hombre", "coruna");

		final ImageImpl i1 = createImage(user, new byte[] { 1, 2, 3 });

		imageService.addImage(i1, user.getId());

		// imageResult.setImageId(-1L);

		assertThrows(InstanceNotFoundException.class, () -> {
			imageService.getImageByUserId(-1L, user.getId());
		});
	}

	@Test
	public void testGetImageByIdItsNotYourImageException() throws InstanceNotFoundException {
		final UserImpl user1 = signUp("userTestGetImageINYI1", "userTestGetImageINYI1", 2, "hombre", "coruna");
		final UserImpl user2 = signUp("userTestGetImageINYI2", "userTestGetImageINYI2", 2, "hombre", "coruna");

		final ImageImpl i1 = createImage(user1, new byte[] { 1, 2, 3 });

		final ImageImpl imageResult = imageService.addImage(i1, user1.getId());

		assertThrows(ItsNotYourImageException.class, () -> {
			imageService.getImageByUserId(imageResult.getImageId(), user2.getId());
		});
	}

	// ----- getFirstImageIdByUserId -----

	@Test
	public void testGetFirstImageIdByUserId() throws InstanceNotFoundException {
		final UserImpl user1 = signUp("userGetFirstImageId1", "passGetFirstImageId1", 2, "hombre", "coruna");
		final UserImpl user2 = signUp("userGetFirstImageId2", "passGetFirstImageId2", 2, "hombre", "coruna");

		final ImageImpl i1 = createImage(user1, new byte[] { 1, 2, 3 });
		final ImageImpl i2 = createImage(user1, new byte[] { 1, 2, 3 });
		final ImageImpl i3 = createImage(user1, new byte[] { 1, 2, 3 });

		assertTrue(imageService.getFirstImageIdByUserId(user1.getId()) == null);

		final ImageImpl imageCreated1 = imageService.addImage(i1, user1.getId());
		imageService.addImage(i2, user1.getId());
		imageService.addImage(i3, user1.getId());

		final Long idResult1 = imageService.getFirstImageIdByUserId(user1.getId());

		assertEquals(idResult1, imageCreated1.getImageId());

		final ImageImpl i4 = createImage(user2, new byte[] { 1, 2, 3 });

		assertTrue(imageService.getFirstImageIdByUserId(user2.getId()) == null);

		final ImageImpl imageCreated4 = imageService.addImage(i4, user2.getId());

		final Long idResult2 = imageService.getFirstImageIdByUserId(user2.getId());

		assertEquals(idResult2, imageCreated4.getImageId());
	}

	@Test
	public void testGetFirstImageIdByUserIdWithInstanceNotFoundException() {
		assertThrows(InstanceNotFoundException.class, () -> {
			imageService.getFirstImageIdByUserId(-1L);
		});
	}

}
