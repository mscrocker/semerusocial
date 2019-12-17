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
import es.udc.fi.dc.fd.dtos.ImageDataDto;
import es.udc.fi.dc.fd.model.persistence.ImageImpl;
import es.udc.fi.dc.fd.model.persistence.UserImpl;
import es.udc.fi.dc.fd.service.Block;
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
		final ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();

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
		} catch (final UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		}

	}

	private MockMvc mockMvc;
	private ImageService imageServiceMock;

	@BeforeEach
	public void setup() {
		final InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
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

	private UserImpl createUser(String userName, String password, LocalDateTime date, String sex, String city, String description) {
		return new UserImpl(userName, password, date, sex, city, description);
	}

	private LocalDateTime getDateTime(int day, int month, int year) {
		return LocalDateTime.of(year, month, day, 00, 01);
	}

	/****
	 * TESTS ADD
	 ********************************************************************************/

	@Test
	public void TestImageController_Add() throws InstanceNotFoundException, Exception {

		final String image = getImageWithMetadata();
		final byte[] converted = getBytes();

		// Usuario que hace la petición
		final UserImpl user = createUser(USER_NAME, PASSWORD, getDateTime(1, 2, 2000), "mujer", "coruna",
				"descripcion");
		user.setId(1L);

		// Imagen de entrada al controller
		final ImageDataDto imageDto = new ImageDataDto();
		imageDto.setData(image);

		// Imagen que devuelve el servicio
		final ImageImpl imageImpl = new ImageImpl();
		// TODO
		imageImpl.setData(converted);
		imageImpl.setImageId(1L);
		imageImpl.setUser(user);

		// Cuando se llame a addImage se va a devolver ImageImpl
		when(imageServiceMock.addImage(any(ImageImpl.class), any(Long.class))).thenReturn(imageImpl);

		mockMvc.perform(post(UrlConfig.URL_IMAGE_ADD_POST).contentType(APPLICATION_JSON_UTF8).requestAttr("userId", 1L)
				.content(Utils.convertObjectToJsonBytes(imageDto))).andExpect(status().isCreated());

		final ArgumentCaptor<ImageImpl> dtoCaptor = ArgumentCaptor.forClass(ImageImpl.class);
		final ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);

		verify(imageServiceMock, times(1)).addImage(dtoCaptor.capture(), userIdCaptor.capture());
		verifyNoMoreInteractions(imageServiceMock);

		final ImageImpl imageCapt = dtoCaptor.getValue();
		assertThat(imageCapt.getData(), is(converted));
		assertThat(userIdCaptor.getValue(), is(1L));
	}

	@Test
	public void TestImageController_Add_InstanceNotFoundException() throws InstanceNotFoundException, Exception {
		final String image = getImageWithMetadata();
		final byte[] converted = getBytes();

		// Imagen de entrada al controller
		final ImageDataDto imageDto = new ImageDataDto();
		imageDto.setData(image);

		doThrow(new InstanceNotFoundException("User", 1L)).when(imageServiceMock).addImage(any(ImageImpl.class),
				any(Long.class));

		mockMvc.perform(post(UrlConfig.URL_IMAGE_ADD_POST).contentType(APPLICATION_JSON_UTF8).requestAttr("userId", 1L)
				.content(Utils.convertObjectToJsonBytes(imageDto))).andExpect(status().isNotFound())
		.andExpect(jsonPath("$.globalError").value("project.exceptions.InstanceNotFoundException"))
		.andExpect(jsonPath("$.fieldErrors").isEmpty());

		final ArgumentCaptor<ImageImpl> dtoCaptor = ArgumentCaptor.forClass(ImageImpl.class);
		final ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);

		verify(imageServiceMock, times(1)).addImage(dtoCaptor.capture(), userIdCaptor.capture());
		verifyNoMoreInteractions(imageServiceMock);

		final ImageImpl imageCapt = dtoCaptor.getValue();
		assertThat(imageCapt.getData(), is(converted));
		assertThat(userIdCaptor.getValue(), is(1L));

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

		final ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
		final ArgumentCaptor<Long> imageIdCaptor = ArgumentCaptor.forClass(Long.class);

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

		final ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
		final ArgumentCaptor<Long> imageIdCaptor = ArgumentCaptor.forClass(Long.class);

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

		final ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
		final ArgumentCaptor<Long> imageIdCaptor = ArgumentCaptor.forClass(Long.class);

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

		final byte[] converted1 = getBytes();
		final ImageImpl returnedImage1 = new ImageImpl();
		final ImageImpl returnedImage2 = new ImageImpl();

		final UserImpl user = createUser(USER_NAME, PASSWORD, getDateTime(1, 2, 2000), "mujer", "coruna",
				"descripcion");
		user.setId(1L);
		returnedImage1.setData(converted1);
		returnedImage1.setImageId(1L);
		returnedImage1.setUser(user);

		returnedImage2.setData(converted1);
		returnedImage2.setImageId(1L);
		returnedImage2.setUser(user);

		final List<ImageImpl> images = new ArrayList<>();
		images.add(0, returnedImage1);
		images.add(0, returnedImage2);
		final Block<ImageImpl> blockImages = new Block<>(images, false);

		when(imageServiceMock.getImagesByUserId(any(Long.class), any(int.class))).thenReturn(blockImages);

		mockMvc.perform(get(UrlConfig.URL_IMAGE_GETIMAGESBYID_GET).contentType(APPLICATION_JSON_UTF8)
				.requestAttr("userId", 1L).param("page", "0")).andExpect(status().isOk())
		.andExpect(content().contentType(APPLICATION_JSON_UTF8)).andExpect(jsonPath("$.elements").isArray())
		.andExpect(jsonPath("$.elements", hasSize(2))).andExpect(jsonPath("$.existMoreElements").value(false));

		final ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
		final ArgumentCaptor<Integer> pageCaptor = ArgumentCaptor.forClass(int.class);

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

		final ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
		final ArgumentCaptor<Integer> pageCaptor = ArgumentCaptor.forClass(int.class);

		verify(imageServiceMock, times(1)).getImagesByUserId(userIdCaptor.capture(), pageCaptor.capture());
		verifyNoMoreInteractions(imageServiceMock);
		assertThat(userIdCaptor.getValue(), is(1L));
		assertThat(pageCaptor.getValue(), is(0));

	}

	
}
