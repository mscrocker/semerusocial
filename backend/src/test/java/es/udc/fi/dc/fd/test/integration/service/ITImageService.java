package es.udc.fi.dc.fd.test.integration.service;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import es.udc.fi.dc.fd.controller.exception.DuplicateInstanceException;
import es.udc.fi.dc.fd.controller.exception.InstanceNotFoundException;
import es.udc.fi.dc.fd.controller.exception.InvalidDateException;
import es.udc.fi.dc.fd.controller.exception.ItsNotYourImageException;
import es.udc.fi.dc.fd.model.persistence.ImageImpl;
import es.udc.fi.dc.fd.model.persistence.UserImpl;
import es.udc.fi.dc.fd.service.Block;
import es.udc.fi.dc.fd.service.ImageService;
import es.udc.fi.dc.fd.service.UserService;
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

@RunWith(JUnitPlatform.class)
@ExtendWith(SpringExtension.class)
@TestExecutionListeners( {DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class})
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:context/service.xml", "classpath:context/persistence.xml",
    "classpath:context/application-context.xml"})
@TestPropertySource( {"classpath:config/persistence-access.properties", "classpath:config/service.properties"})
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

    final ImageImpl i = createImage(user, new byte[] {1, 2, 3});

    final ImageImpl imageCreated = imageService.addImage(i, user.getId());

    assertEquals(i, imageCreated);
  }

  @Test
  public void testAddImageWithInstanceNotFoundException() {
    final UserImpl user = createUser("userAddImgNotFoundException", "passAddImgNotFoundException",
        getDateTime(1, 1, 2000), "hombre", "coruna", "descripcion");

    //		user.setId(-1L);

    final ImageImpl i = createImage(user, new byte[] {1, 2, 3});

    assertThrows(InstanceNotFoundException.class, () -> {
      imageService.addImage(i, -1L);
    });
  }

  // ----- removeImage -----

  @Test
  public void testRemoveImage() throws InstanceNotFoundException, ItsNotYourImageException {
    final UserImpl user = signUp("userRemoveImage", "passRemoveImage", 1, "hombre", "coruna");

    final ImageImpl i = createImage(user, new byte[] {1, 2, 3});

    final ImageImpl imageCreated = imageService.addImage(i, user.getId());

    imageService.removeImage(imageCreated.getImageId(), user.getId());

    assertThrows(InstanceNotFoundException.class, () -> {
      imageService.removeImage(imageCreated.getImageId(), user.getId());
    });

  }

  @Test
  public void testRemoveImageWithInstanceNotFoundException() throws InstanceNotFoundException {
    final UserImpl user = signUp("userRemoveImageINFE", "passRemoveImageINFE", 1, "hombre", "coruna");

    final ImageImpl i = createImage(user, new byte[] {1, 2, 3});

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

    final ImageImpl i = createImage(user, new byte[] {1, 2, 3});

    final ImageImpl imageCreated = imageService.addImage(i, user.getId());

    assertThrows(ItsNotYourImageException.class, () -> {
      imageService.removeImage(imageCreated.getImageId(), user2.getId());
    });
  }

  // ----- getImagesById -----

  @Test
  public void testGetImagesById() throws InstanceNotFoundException, ItsNotYourImageException {
    final UserImpl user = signUp("userTestGet", "userTestGet", 2, "hombre", "coruna");

    final ImageImpl i1 = createImage(user, new byte[] {1, 2, 3});
    final ImageImpl i2 = createImage(user, new byte[] {1, 2, 3});
    final ImageImpl i3 = createImage(user, new byte[] {1, 2, 3});
    final ImageImpl i4 = createImage(user, new byte[] {1, 2, 3});
    final ImageImpl i5 = createImage(user, new byte[] {1, 2, 3});
    final ImageImpl i6 = createImage(user, new byte[] {1, 2, 3});
    final ImageImpl i7 = createImage(user, new byte[] {1, 2, 3});
    final ImageImpl i8 = createImage(user, new byte[] {1, 2, 3});
    final ImageImpl i9 = createImage(user, new byte[] {1, 2, 3});
    final ImageImpl i10 = createImage(user, new byte[] {1, 2, 3});

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

    assertEquals(imageListResult.getElements(), imageList);

    final ImageImpl i11 = createImage(user, new byte[] {1, 2, 3});
    final ImageImpl imageCreated11 = imageService.addImage(i11, user.getId());
    imageList.add(imageCreated11);
    imageListResult = imageService.getImagesByUserId(user.getId(), 0);

    assertEquals(imageListResult.isExistMoreElements(), true);

    final UserImpl user2 = signUp("userTestGet2", "userTestGet2", 12, "hombre", "coruna");

    final ImageImpl i12 = createImage(user2, new byte[] {1, 2, 3});
    final ImageImpl i13 = createImage(user2, new byte[] {1, 2, 3});
    final ImageImpl i14 = createImage(user2, new byte[] {1, 2, 3});
    final ImageImpl i15 = createImage(user2, new byte[] {1, 2, 3});

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

    assertEquals(imageListResult2.getElements(), imageList2);
  }

  @Test
  public void testGetImagesByIdWithInstanceNotFoundException() throws InstanceNotFoundException {
    final UserImpl user = signUp("userTestGetImagesINFE", "userTestGetGetImagesINFE", 2, "hombre", "coruna");

    final ImageImpl i1 = createImage(user, new byte[] {1, 2, 3});

    imageService.addImage(i1, user.getId());

    // user.setId(-1L);

    assertThrows(InstanceNotFoundException.class, () -> {
      imageService.getImagesByUserId(-1L, 0);
    });
  }

  // getAnonymousCarrusel

  @Test
  public void testgetAnonymousCarrusel()
      throws DuplicateInstanceException, InvalidDateException, InstanceNotFoundException {
    final UserImpl user1 = createUser("userAnon1", "userInvalidRate1", getDateTime(1, 1, 2000), "masculino",
        "coruña",
        "descripcion");
    final UserImpl user2 = createUser("userAnon2", "userInvalidRate1", getDateTime(1, 1, 2000), "masculino",
        "coruña",
        "descripcion");
    final UserImpl user3 = createUser("userAnon3", "userInvalidRate1", getDateTime(1, 1, 2000), "masculino",
        "coruña",
        "descripcion");
    final UserImpl user4 = createUser("userAnon4", "userInvalidRate1", getDateTime(1, 1, 2000), "masculino",
        "coruña",
        "descripcion");
    final UserImpl user5 = createUser("userAnon5", "userInvalidRate1", getDateTime(1, 1, 2000), "masculino",
        "coruña",
        "descripcion");
    final UserImpl user6 = createUser("userAnon6", "userInvalidRate1", getDateTime(1, 1, 2000), "masculino",
        "coruña",
        "descripcion");
    final UserImpl user7 = createUser("userAnon7", "userInvalidRate1", getDateTime(1, 1, 2000), "masculino",
        "coruña",
        "descripcion");
    final UserImpl user8 = createUser("userAnon8", "userInvalidRate1", getDateTime(1, 1, 2000), "masculino",
        "coruña",
        "descripcion");
    final UserImpl user9 = createUser("userAnon9", "userInvalidRate1", getDateTime(1, 1, 2000), "masculino",
        "coruña",
        "descripcion");
    final UserImpl user10 = createUser("userAnon10", "userInvalidRate1", getDateTime(1, 1, 2000), "masculino",
        "coruña",
        "descripcion");
    final UserImpl user11 = createUser("userAnon11", "userInvalidRate1", getDateTime(1, 1, 2000), "masculino",
        "coruña",
        "descripcion");
    final UserImpl user12 = createUser("userAnon12", "userInvalidRate1", getDateTime(1, 1, 2000), "masculino",
        "lugo", "descripcion");

    user1.setRating(1.0);
    user2.setRating(1.5);
    user3.setRating(2.0);
    user4.setRating(2.5);
    user5.setRating(3.0);
    user6.setRating(3.5);
    user7.setRating(4.0);
    user8.setRating(4.1);
    user9.setRating(4.2);
    user10.setRating(4.3);
    user11.setRating(4.4);
    user11.setRating(5.0);

    userService.signUp(user1);
    userService.signUp(user2);
    userService.signUp(user3);
    userService.signUp(user4);
    userService.signUp(user5);
    userService.signUp(user6);
    userService.signUp(user7);
    userService.signUp(user8);
    userService.signUp(user9);
    userService.signUp(user10);
    userService.signUp(user11);
    userService.signUp(user12);

    final ImageImpl imagen1 = createImage(user1, new byte[] {1, 2, 3});
    final ImageImpl imagen2 = createImage(user2, new byte[] {2, 3, 4});
    final ImageImpl imagen3 = createImage(user3, new byte[] {3, 4, 5});
    final ImageImpl imagen4 = createImage(user4, new byte[] {4, 5, 6});
    final ImageImpl imagen5 = createImage(user5, new byte[] {5, 6, 7});
    final ImageImpl imagen6 = createImage(user6, new byte[] {6, 7, 8});
    final ImageImpl imagen7 = createImage(user7, new byte[] {7, 8, 9});
    final ImageImpl imagen8 = createImage(user8, new byte[] {8, 9, 10});
    final ImageImpl imagen9 = createImage(user9, new byte[] {9, 10, 11});
    final ImageImpl imagen10 = createImage(user10, new byte[] {10, 11, 12});
    final ImageImpl imagen11 = createImage(user11, new byte[] {11, 12, 13});
    final ImageImpl imagen112 = createImage(user11, new byte[] {11, 13, 14});

    imageService.addImage(imagen1, user1.getId());
    imageService.addImage(imagen2, user2.getId());
    imageService.addImage(imagen3, user3.getId());
    imageService.addImage(imagen4, user4.getId());
    imageService.addImage(imagen5, user5.getId());
    imageService.addImage(imagen6, user6.getId());
    imageService.addImage(imagen7, user7.getId());
    imageService.addImage(imagen8, user8.getId());
    imageService.addImage(imagen9, user9.getId());
    imageService.addImage(imagen10, user10.getId());
    imageService.addImage(imagen11, user11.getId());
    imageService.addImage(imagen112, user11.getId());

    final List<ImageImpl> expected1 = new ArrayList<>();
    expected1.add(imagen2);
    expected1.add(imagen3);
    expected1.add(imagen4);
    expected1.add(imagen5);
    expected1.add(imagen6);
    expected1.add(imagen7);
    expected1.add(imagen8);
    expected1.add(imagen9);
    expected1.add(imagen10);
    expected1.add(imagen11);

    final Block<ImageImpl> result1 = imageService.getAnonymousCarrusel("coruña", 0);

    assertEquals(expected1, result1.getElements());
    assertEquals(expected1.size(), result1.getElements().size());
    assertTrue(result1.isExistMoreElements());

    final List<ImageImpl> expected2 = new ArrayList<>();
    expected2.add(imagen112);

    final Block<ImageImpl> result2 = imageService.getAnonymousCarrusel("coruña", 1);

    assertEquals(expected2, result2.getElements());
    assertEquals(expected2.size(), result2.getElements().size());
    assertFalse(result2.isExistMoreElements());
  }

}
