package es.udc.fi.dc.fd.test.unit.controller.entity;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import es.udc.fi.dc.fd.controller.entity.ImageController;
import es.udc.fi.dc.fd.controller.exception.InstanceNotFoundException;
import es.udc.fi.dc.fd.controller.exception.ItsNotYourImageException;
import es.udc.fi.dc.fd.dtos.ImageCreationDto;
import es.udc.fi.dc.fd.dtos.ImageEditionDto;
import es.udc.fi.dc.fd.model.persistence.ImageImpl;
import es.udc.fi.dc.fd.model.persistence.UserImpl;
import es.udc.fi.dc.fd.service.Block;
import es.udc.fi.dc.fd.service.BlockImageByUserId;
import es.udc.fi.dc.fd.service.ImageService;
import es.udc.fi.dc.fd.test.config.UrlConfig;

@RunWith(JUnitPlatform.class)
public class TestImageController {

	/**
	 * Returns a controller with mocked dependencies.
	 *
	 * @return a mocked controller
	 */
	private final ImageController getController() {
		final ImageService service; // Mocked service

		service = Mockito.mock(ImageService.class);

		imageServiceMock = service;

		return new ImageController(service, messageSource());
	}

	private MessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();

		messageSource.setBasename("i18n/messages");
		messageSource.setUseCodeAsDefaultMessage(true);

		return messageSource;
	}

	private String getImage() {
		return "/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDAAMCAgICAgMCAgID";
	}

	private String getImageWithMetadata() {
		return "data:image/jpeg;base64, /9j/4AAQSkZJRgABAQAAAQABAAD/2wBDAAMCAgICAgMCAgID";
	}

	private byte[] getBytes() {
		try {
			return java.util.Base64.getDecoder().decode(getImage().getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		}

	}

	private MockMvc mockMvc;
	private ImageService imageServiceMock;

	@BeforeEach
	public void setup() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/jsp/view/");
		viewResolver.setSuffix(".jsp");

		mockMvc = MockMvcBuilders.standaloneSetup(getController()).setViewResolvers(viewResolver).build();
	}

	public TestImageController() {
		super();
	}

	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
	public static final String PASSWORD = "pass";
	public static final String USER_NAME = "name";

	private UserImpl createUser(String userName, String password, LocalDateTime date, String sex, String city) {
		return new UserImpl(userName, password, date, sex, city);
	}

	private LocalDateTime getDateTime(int day, int month, int year) {
		return LocalDateTime.of(year, month, day, 00, 01);
	}

	/****
	 * TESTS ADD
	 ********************************************************************************/

	@Test
	public void TestImageController_Add() throws InstanceNotFoundException, Exception {

		String image = getImageWithMetadata();
		byte[] converted = getBytes();
		String description = "Prueba de imagen";

		// Usuario que hace la petición
		UserImpl user = createUser(USER_NAME, PASSWORD, getDateTime(1, 2, 2000), "mujer", "coruna");
		user.setId(1L);

		// Imagen de entrada al controller
		ImageCreationDto imageDto = new ImageCreationDto();
		imageDto.setDescription(description);
		imageDto.setData(image);

		// Imagen que devuelve el servicio
		ImageImpl imageImpl = new ImageImpl();
		imageImpl.setDescription(description);
		// TODO
		imageImpl.setData(converted);
		imageImpl.setImageId(1L);
		imageImpl.setUser(user);

		// Cuando se llame a addImage se va a devolver ImageImpl
		when(imageServiceMock.addImage(any(ImageImpl.class), any(Long.class))).thenReturn(imageImpl);

		mockMvc.perform(post(UrlConfig.URL_IMAGE_ADD_POST).contentType(APPLICATION_JSON_UTF8).requestAttr("userId", 1L)
				.content(Utils.convertObjectToJsonBytes(imageDto))).andExpect(status().isCreated());

		ArgumentCaptor<ImageImpl> dtoCaptor = ArgumentCaptor.forClass(ImageImpl.class);
		ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);

		verify(imageServiceMock, times(1)).addImage(dtoCaptor.capture(), userIdCaptor.capture());
		verifyNoMoreInteractions(imageServiceMock);

		ImageImpl imageCapt = dtoCaptor.getValue();
		assertThat(imageCapt.getData(), is(converted));
		assertThat(imageCapt.getDescription(), is(description));
		assertThat(userIdCaptor.getValue(), is(1L));
	}

	@Test
	public void TestImageController_Add_InstanceNotFoundException() throws InstanceNotFoundException, Exception {
		String image = getImageWithMetadata();
		byte[] converted = getBytes();
		String description = "Prueba de imagen";

		// Imagen de entrada al controller
		ImageCreationDto imageDto = new ImageCreationDto();
		imageDto.setDescription(description);
		imageDto.setData(image);

		doThrow(new InstanceNotFoundException("User", 1L)).when(imageServiceMock).addImage(any(ImageImpl.class),
				any(Long.class));

		mockMvc.perform(post(UrlConfig.URL_IMAGE_ADD_POST).contentType(APPLICATION_JSON_UTF8).requestAttr("userId", 1L)
				.content(Utils.convertObjectToJsonBytes(imageDto))).andExpect(status().isNotFound())
				.andExpect(jsonPath("$.globalError").value("project.exceptions.InstanceNotFoundException"))
				.andExpect(jsonPath("$.fieldErrors").isEmpty());

		ArgumentCaptor<ImageImpl> dtoCaptor = ArgumentCaptor.forClass(ImageImpl.class);
		ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);

		verify(imageServiceMock, times(1)).addImage(dtoCaptor.capture(), userIdCaptor.capture());
		verifyNoMoreInteractions(imageServiceMock);

		ImageImpl imageCapt = dtoCaptor.getValue();
		assertThat(imageCapt.getData(), is(converted));
		assertThat(imageCapt.getDescription(), is(description));
		assertThat(userIdCaptor.getValue(), is(1L));

	}

	/**** TEST EDITIMAGE ******************************************************/

	@Test
	public void TestImageController_EditImage() throws InstanceNotFoundException, ItsNotYourImageException, Exception {

		getImage();
		byte[] converted = getBytes();

		String description = "Prueba de imagen";
		// ImagenDto que devuelve el servicio
		UserImpl user = createUser(USER_NAME, PASSWORD, getDateTime(1, 2, 2000), "mujer", "coruna");
		user.setId(1L);

		ImageImpl imageImpl = new ImageImpl();
		imageImpl.setDescription(description);
		imageImpl.setData(converted);
		imageImpl.setImageId(1L);
		imageImpl.setUser(user);
		imageImpl.setImageId(1L);

		when(imageServiceMock.editImage(any(ImageImpl.class), any(Long.class), any(Long.class))).thenReturn(imageImpl);

		ImageEditionDto imageContr = new ImageEditionDto(description);

		mockMvc.perform(put(UrlConfig.URL_IMAGE_EDIT_PUT, 2).contentType(APPLICATION_JSON_UTF8)
				.requestAttr("userId", 1L).content(Utils.convertObjectToJsonBytes(imageContr)))
				.andExpect(status().isNoContent());

		ArgumentCaptor<ImageImpl> dtoCaptor = ArgumentCaptor.forClass(ImageImpl.class);
		ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
		ArgumentCaptor<Long> imageIdCaptor = ArgumentCaptor.forClass(Long.class);

		verify(imageServiceMock, times(1)).editImage(dtoCaptor.capture(), imageIdCaptor.capture(),
				userIdCaptor.capture());
		verifyNoMoreInteractions(imageServiceMock);

		ImageImpl imageCapt = dtoCaptor.getValue();
		assertThat(imageCapt.getDescription(), is(description));
		assertThat(userIdCaptor.getValue(), is(1L));
		assertThat(imageIdCaptor.getValue(), is(2L));

	}

	@Test
	public void TestImageController_EditImage_InstanceNotFoundException()
			throws InstanceNotFoundException, ItsNotYourImageException, Exception {

		getImage();
		byte[] converted = getBytes();
		String description = "Prueba de imagen";

		// ImagenDto que devuelve el servicio
		UserImpl user = createUser(USER_NAME, PASSWORD, getDateTime(1, 2, 2000), "mujer", "coruna");
		user.setId(1L);

		ImageImpl imageImpl = new ImageImpl();
		imageImpl.setDescription(description);
		imageImpl.setData(converted);
		imageImpl.setImageId(1L);
		imageImpl.setUser(user);
		imageImpl.setImageId(1L);

		when(imageServiceMock.editImage(any(ImageImpl.class), any(Long.class), any(Long.class)))
				.thenThrow(new InstanceNotFoundException("user", 1L));

		ImageEditionDto imageContr = new ImageEditionDto(description);

		mockMvc.perform(put(UrlConfig.URL_IMAGE_EDIT_PUT, 2).contentType(APPLICATION_JSON_UTF8)
				.requestAttr("userId", 1L).content(Utils.convertObjectToJsonBytes(imageContr)))
				.andExpect(status().isNotFound());

		ArgumentCaptor<ImageImpl> dtoCaptor = ArgumentCaptor.forClass(ImageImpl.class);
		ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
		ArgumentCaptor<Long> imageIdCaptor = ArgumentCaptor.forClass(Long.class);

		verify(imageServiceMock, times(1)).editImage(dtoCaptor.capture(), imageIdCaptor.capture(),
				userIdCaptor.capture());
		verifyNoMoreInteractions(imageServiceMock);

		ImageImpl imageCapt = dtoCaptor.getValue();
		assertThat(imageCapt.getDescription(), is(description));
		assertThat(userIdCaptor.getValue(), is(1L));
		assertThat(imageIdCaptor.getValue(), is(2L));
	}

	@Test
	public void TestImageController_EditImage_ItsNotYourImageException()
			throws InstanceNotFoundException, ItsNotYourImageException, Exception {
		getImage();
		byte[] converted = getBytes();
		String description = "Prueba de imagen";
		// ImagenDto que devuelve el servicio
		UserImpl user = createUser(USER_NAME, PASSWORD, getDateTime(1, 2, 2000), "mujer", "coruna");
		user.setId(1L);

		ImageImpl imageImpl = new ImageImpl();
		imageImpl.setDescription(description);
		imageImpl.setData(converted);
		imageImpl.setImageId(1L);
		imageImpl.setUser(user);
		imageImpl.setImageId(1L);

		when(imageServiceMock.editImage(any(ImageImpl.class), any(Long.class), any(Long.class)))
				.thenThrow(new ItsNotYourImageException(""));

		ImageEditionDto imageContr = new ImageEditionDto(description);

		mockMvc.perform(put(UrlConfig.URL_IMAGE_EDIT_PUT, 2).contentType(APPLICATION_JSON_UTF8)
				.requestAttr("userId", 1L).content(Utils.convertObjectToJsonBytes(imageContr)))
				.andExpect(status().isForbidden());

		ArgumentCaptor<ImageImpl> dtoCaptor = ArgumentCaptor.forClass(ImageImpl.class);
		ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
		ArgumentCaptor<Long> imageIdCaptor = ArgumentCaptor.forClass(Long.class);

		verify(imageServiceMock, times(1)).editImage(dtoCaptor.capture(), imageIdCaptor.capture(),
				userIdCaptor.capture());
		verifyNoMoreInteractions(imageServiceMock);

		ImageImpl imageCapt = dtoCaptor.getValue();
		assertThat(imageCapt.getDescription(), is(description));
		assertThat(userIdCaptor.getValue(), is(1L));
		assertThat(imageIdCaptor.getValue(), is(2L));
	}

	/***
	 * TEST REMOVEIMAGE
	 *
	 * @throws Exception
	 *****************************************************/

	@Test
	public void TestImageController_RemoveImage()
			throws InstanceNotFoundException, ItsNotYourImageException, Exception {

		mockMvc.perform(delete(UrlConfig.URL_IMAGE_REMOVE_DELETE, 2).contentType(APPLICATION_JSON_UTF8)
				.requestAttr("userId", 1L)).andExpect(status().isNoContent());

		ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
		ArgumentCaptor<Long> imageIdCaptor = ArgumentCaptor.forClass(Long.class);

		verify(imageServiceMock, times(1)).removeImage(imageIdCaptor.capture(), userIdCaptor.capture());
		verifyNoMoreInteractions(imageServiceMock);
		assertThat(userIdCaptor.getValue(), is(1L));
		assertThat(imageIdCaptor.getValue(), is(2L));
	}

	@Test
	public void TestImageController_RemoveImage_InstanceNotFoundException()
			throws InstanceNotFoundException, ItsNotYourImageException, Exception {

		doThrow(new InstanceNotFoundException("user", 1L)).when(imageServiceMock).removeImage(any(Long.class),
				any(Long.class));
		mockMvc.perform(delete(UrlConfig.URL_IMAGE_REMOVE_DELETE, 2).contentType(APPLICATION_JSON_UTF8)
				.requestAttr("userId", 1L)).andExpect(status().isNotFound());

		ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
		ArgumentCaptor<Long> imageIdCaptor = ArgumentCaptor.forClass(Long.class);

		verify(imageServiceMock, times(1)).removeImage(imageIdCaptor.capture(), userIdCaptor.capture());
		verifyNoMoreInteractions(imageServiceMock);
		assertThat(userIdCaptor.getValue(), is(1L));
		assertThat(imageIdCaptor.getValue(), is(2L));
	}

	@Test
	public void TestImageController_RemoveImage_ItsNotYourImageException()
			throws InstanceNotFoundException, ItsNotYourImageException, Exception {

		doThrow(new ItsNotYourImageException(" ")).when(imageServiceMock).removeImage(any(Long.class), any(Long.class));
		mockMvc.perform(delete(UrlConfig.URL_IMAGE_REMOVE_DELETE, 2).contentType(APPLICATION_JSON_UTF8)
				.requestAttr("userId", 1L)).andExpect(status().isForbidden());

		ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
		ArgumentCaptor<Long> imageIdCaptor = ArgumentCaptor.forClass(Long.class);

		verify(imageServiceMock, times(1)).removeImage(imageIdCaptor.capture(), userIdCaptor.capture());
		verifyNoMoreInteractions(imageServiceMock);
		assertThat(userIdCaptor.getValue(), is(1L));
		assertThat(imageIdCaptor.getValue(), is(2L));

	}

	/****
	 * IMAGE GETIMAGESBYID
	 ***********************************************************/

	@Test
	public void TestImageController_getImagesById() throws InstanceNotFoundException, Exception {

		String description1 = "asd";
		getImage();
		byte[] converted1 = getBytes();
		ImageImpl returnedImage1 = new ImageImpl();
		ImageImpl returnedImage2 = new ImageImpl();

		UserImpl user = createUser(USER_NAME, PASSWORD, getDateTime(1, 2, 2000), "mujer", "coruna");
		user.setId(1L);
		returnedImage1.setDescription(description1);
		returnedImage1.setData(converted1);
		returnedImage1.setImageId(1L);
		returnedImage1.setUser(user);

		returnedImage2.setDescription(description1);
		returnedImage2.setData(converted1);
		returnedImage2.setImageId(1L);
		returnedImage2.setUser(user);

		List<ImageImpl> images = new ArrayList<>();
		images.add(0, returnedImage1);
		images.add(0, returnedImage2);
		Block<ImageImpl> blockImages = new Block<>(images, false);

		when(imageServiceMock.getImagesByUserId(any(Long.class), any(int.class))).thenReturn(blockImages);

		mockMvc.perform(get(UrlConfig.URL_IMAGE_GETIMAGESBYID_GET).contentType(APPLICATION_JSON_UTF8)
				.requestAttr("userId", 1L).param("page", "0")).andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8)).andExpect(jsonPath("$.images").isArray())
				.andExpect(jsonPath("$.images", hasSize(2))).andExpect(jsonPath("$.existMoreImages").value(false));

		ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
		ArgumentCaptor<Integer> pageCaptor = ArgumentCaptor.forClass(int.class);

		verify(imageServiceMock, times(1)).getImagesByUserId(userIdCaptor.capture(), pageCaptor.capture());
		verifyNoMoreInteractions(imageServiceMock);
		assertThat(userIdCaptor.getValue(), is(1L));
		assertThat(pageCaptor.getValue(), is(0));

	}

	@Test
	public void TestImageController_getImagesById_InstanceNotFoundException()
			throws InstanceNotFoundException, Exception {

		when(imageServiceMock.getImagesByUserId(any(Long.class), any(int.class)))
				.thenThrow(new InstanceNotFoundException(null, 1L));

		mockMvc.perform(get(UrlConfig.URL_IMAGE_GETIMAGESBYID_GET).contentType(APPLICATION_JSON_UTF8)
				.requestAttr("userId", 1L).param("page", "0")).andExpect(status().isNotFound())
				.andExpect(jsonPath("$.globalError").value("project.exceptions.InstanceNotFoundException"))
				.andExpect(jsonPath("$.fieldErrors").isEmpty());

		ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
		ArgumentCaptor<Integer> pageCaptor = ArgumentCaptor.forClass(int.class);

		verify(imageServiceMock, times(1)).getImagesByUserId(userIdCaptor.capture(), pageCaptor.capture());
		verifyNoMoreInteractions(imageServiceMock);
		assertThat(userIdCaptor.getValue(), is(1L));
		assertThat(pageCaptor.getValue(), is(0));

	}

	/**** TESTS GETIMAGEBYID *********************************************/

	@Test
	public void TestImageController_getImageById()
			throws InstanceNotFoundException, ItsNotYourImageException, Exception {

		String description1 = "asd";
		String image = getImage();
		// resultList.add(image);
		byte[] converted = getBytes();
		ImageImpl imageImpl = new ImageImpl();

		UserImpl user = createUser(USER_NAME, PASSWORD, getDateTime(1, 2, 2000), "mujer", "coruna");
		user.setId(2L);
		imageImpl.setDescription(description1);
		imageImpl.setData(converted);
		imageImpl.setImageId(1L);
		imageImpl.setUser(user);

		BlockImageByUserId<ImageImpl> blockImages = new BlockImageByUserId<>(imageImpl, null, 2L);

		when(imageServiceMock.getImageByUserId(any(Long.class), any(Long.class))).thenReturn(blockImages);

		mockMvc.perform(get(UrlConfig.URL_IMAGE_GETIMAGEBYID_GET, "1").contentType(APPLICATION_JSON_UTF8)
				.requestAttr("userId", 2L)).andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8)).andExpect(jsonPath("$.prevId").isEmpty())
				.andExpect(jsonPath("$.nextId").value(2)).andExpect(jsonPath("$.image.data").value(image))
				.andExpect(jsonPath("$.image.description").value(description1));

		ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
		ArgumentCaptor<Long> imageIdCaptor = ArgumentCaptor.forClass(Long.class);

		verify(imageServiceMock, times(1)).getImageByUserId(imageIdCaptor.capture(), userIdCaptor.capture());
		verifyNoMoreInteractions(imageServiceMock);
		assertThat(userIdCaptor.getValue(), is(2L));
		assertThat(imageIdCaptor.getValue(), is(1L));

	}

	@Test
	public void TestImageController_getImageById_InstanceNotFoundException()
			throws InstanceNotFoundException, ItsNotYourImageException, Exception {

		when(imageServiceMock.getImageByUserId(any(Long.class), any(Long.class)))
				.thenThrow(new InstanceNotFoundException(null, 1L));

		mockMvc.perform(get(UrlConfig.URL_IMAGE_GETIMAGEBYID_GET, "1").contentType(APPLICATION_JSON_UTF8)
				.requestAttr("userId", 2L)).andExpect(status().isNotFound())
				.andExpect(jsonPath("$.globalError").value("project.exceptions.InstanceNotFoundException"))
				.andExpect(jsonPath("$.fieldErrors").isEmpty());

		ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
		ArgumentCaptor<Long> imageIdCaptor = ArgumentCaptor.forClass(Long.class);

		verify(imageServiceMock, times(1)).getImageByUserId(imageIdCaptor.capture(), userIdCaptor.capture());
		verifyNoMoreInteractions(imageServiceMock);
		assertThat(userIdCaptor.getValue(), is(2L));
		assertThat(imageIdCaptor.getValue(), is(1L));

	}

	@Test
	public void TestImageController_getImageById_ItsNotYourImageException()
			throws InstanceNotFoundException, ItsNotYourImageException, Exception {

		when(imageServiceMock.getImageByUserId(any(Long.class), any(Long.class)))
				.thenThrow(new ItsNotYourImageException(" "));

		mockMvc.perform(get(UrlConfig.URL_IMAGE_GETIMAGEBYID_GET, "1").contentType(APPLICATION_JSON_UTF8)
				.requestAttr("userId", 2L)).andExpect(status().isForbidden())
				.andExpect(jsonPath("$.globalError").value("project.exceptions.ItsNotYourImageException"))
				.andExpect(jsonPath("$.fieldErrors").isEmpty());

		ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
		ArgumentCaptor<Long> imageIdCaptor = ArgumentCaptor.forClass(Long.class);

		verify(imageServiceMock, times(1)).getImageByUserId(imageIdCaptor.capture(), userIdCaptor.capture());
		verifyNoMoreInteractions(imageServiceMock);
		assertThat(userIdCaptor.getValue(), is(2L));
		assertThat(imageIdCaptor.getValue(), is(1L));

	}

	/***** TESTS GET FIRST IMAGE BY USER ID **************************/

	@Test
	public void TestImageController_getFirstImageIdByUserId() throws InstanceNotFoundException, Exception {
		long userId = 44L;

		when(imageServiceMock.getFirstImageIdByUserId(any(Long.class))).thenReturn(1L);

		mockMvc.perform(
				get(UrlConfig.URL_IMAGE_GETFIRST_GET).contentType(APPLICATION_JSON_UTF8).requestAttr("userId", userId))
				.andExpect(status().isOk()).andExpect(jsonPath("$.imageId").value(1L));

		ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);

		verify(imageServiceMock, times(1)).getFirstImageIdByUserId(userIdCaptor.capture());
		verifyNoMoreInteractions(imageServiceMock);
		assertThat(userIdCaptor.getValue(), is(userId));

	}

	@Test
	public void TestImageController_getFirstImageIdByUserId_InstanceNotFoundException()
			throws InstanceNotFoundException, Exception {

		when(imageServiceMock.getFirstImageIdByUserId(any(Long.class)))
				.thenThrow(new InstanceNotFoundException(null, 1L));

		mockMvc.perform(
				get(UrlConfig.URL_IMAGE_GETFIRST_GET).contentType(APPLICATION_JSON_UTF8).requestAttr("userId", 1L))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.globalError").value("project.exceptions.InstanceNotFoundException"))
				.andExpect(jsonPath("$.fieldErrors").isEmpty());

		ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);

		verify(imageServiceMock, times(1)).getFirstImageIdByUserId(userIdCaptor.capture());
		verifyNoMoreInteractions(imageServiceMock);
		assertThat(userIdCaptor.getValue(), is(1L));
	}
}
