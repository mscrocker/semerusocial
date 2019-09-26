package es.udc.fi.dc.fd.test.unit.controller.entity;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import es.udc.fi.dc.fd.controller.entity.ImageController;
import es.udc.fi.dc.fd.controller.exception.InstanceNotFoundException;
import es.udc.fi.dc.fd.dtos.ImageCreationDto;
import es.udc.fi.dc.fd.model.persistence.ImageImpl;
import es.udc.fi.dc.fd.model.persistence.UserImpl;
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
		this.imageServiceMock = service;

		return new ImageController(service);
	}

	private MockMvc mockMvc;
	private ImageService imageServiceMock;

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
	
	@Test
	public void TestImageController_Add() throws InstanceNotFoundException, Exception {
		String userName = "user";
		byte[] image = {1,2,3,4};
		int age = 20;
		String city = "Madrid";
		String description = "pRueba de imagen";
		String sex = "F";
		
		
		//Usuario que hace la petición
		UserImpl user = new UserImpl(userName, "pass");
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
	
	//TODO: ¿Hacer caso de InstanceNotFoundException? No necesaria ya que debería ser Unautorized
	
	
}
