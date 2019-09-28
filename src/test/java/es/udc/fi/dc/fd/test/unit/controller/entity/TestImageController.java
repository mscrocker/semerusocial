package es.udc.fi.dc.fd.test.unit.controller.entity;

import java.nio.charset.Charset;

import org.junit.jupiter.api.BeforeEach;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import es.udc.fi.dc.fd.controller.entity.ImageController;
import es.udc.fi.dc.fd.service.ImageService;

@RunWith(JUnitPlatform.class)
public class TestImageController {
	
	/**
	 * Returns a controller with mocked dependencies.
	 * 
	 * @return a mocked controller
	 */
	private final ImageController getController() {
		final ImageService service; // Mocked service
		final MessageSource messageSource;

		service = Mockito.mock(ImageService.class);
		messageSource = Mockito.mock(MessageSource.class);
		this.imageServiceMock = service;
		this.messageSource = messageSource;

		return new ImageController(service,messageSource);
	}

	private MockMvc mockMvc;
	private ImageService imageServiceMock;
	private MessageSource messageSource;

	@BeforeEach
	public void setup() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/jsp/view/");
		viewResolver.setSuffix(".jsp");

		mockMvc = MockMvcBuilders.standaloneSetup(getController())
				.setViewResolvers(viewResolver).build();
	}

	public TestImageController() {
		super();
	}
	
	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
	
	/****TESTS ADD ********************************************************************************/
	/*
	@Test
	public void TestImageController_Add() throws InstanceNotFoundException, Exception {
		String userName = "user";
		byte[] image = {1,2,3,4};
		int age = 20;
		String city = "Madrid";
		String description = "pRueba de imagen";
		String sex = "F";
		
		
		//Usuario que hace la petición
		UserImpl user = new UserImpl(userName, "pass", 1, "mujer", "coruna");
		user.setId(1L);
		
		//Imagen de entrada al controller
		ImageCreationDto imageDto = new ImageCreationDto();
		imageDto.setAge(age);
		imageDto.setCity(city);
		imageDto.setDescription(description);
		imageDto.setImage(image);
		imageDto.setSex(sex);
		imageDto.setUser(user);
		
		//ImagenDto que devuelve el servicio 
		ImageImpl imageImpl = new ImageImpl();
		imageImpl.setAge(age);
		imageImpl.setCity(city);
		imageImpl.setDescription(description);
		imageImpl.setImage(image);
		imageImpl.setImageId(1L);
		imageImpl.setSex(sex);
		imageImpl.setUser(user);
		
		//Cuando se llame a addImage se va a devolver ImageImpl
		when(imageServiceMock.addImage(any(ImageImpl.class), any(Long.class))).thenReturn(imageImpl);
		
		mockMvc.perform(post(UrlConfig.URL_IMAGE_ADD_POST).contentType(APPLICATION_JSON_UTF8)
				.requestAttr("userId", 1L)
				.content(Utils.convertObjectToJsonBytes(imageDto)))
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				//.andExpect(jsonPath("$.image").value(image))			//TODO:arreglar
				.andExpect(jsonPath("$.age").value(age))
				.andExpect(jsonPath("$.sex").value(sex))
				.andExpect(jsonPath("$.city").value(city))
				.andExpect(jsonPath("$.description").value(description))
				.andExpect(jsonPath("$.user").value(user));
		
	}
	*/
	//TODO: ¿Hacer caso de InstanceNotFoundException? No necesaria ya que debería ser Unautorized
	
	
}
