package es.udc.fi.dc.fd.test.unit.controller.entity;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

import java.io.IOException;
import java.nio.charset.Charset;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.udc.fi.dc.fd.controller.entity.UserController;
import es.udc.fi.dc.fd.controller.exception.DuplicateInstanceException;
import es.udc.fi.dc.fd.controller.exception.IncorrectLoginException;
import es.udc.fi.dc.fd.dtos.LoginParamsDto;
import es.udc.fi.dc.fd.model.persistence.UserImpl;
import es.udc.fi.dc.fd.service.UserService;
import es.udc.fi.dc.fd.test.config.UrlConfig;

@RunWith(JUnitPlatform.class)
public final class TestUserController {

	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
	public static final String PASSWORD = "pass";
	public static final String USER_NAME = "name";

	/******************************************************************************/

	private MessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();

		messageSource.setBasename("i18n/messages");
		messageSource.setUseCodeAsDefaultMessage(true);

		return messageSource;
	}

	/**
	 * Returns a controller with mocked dependencies.
	 * 
	 * @return a mocked controller
	 */
	private final UserController getController() {
		final UserService service; // Mocked service

		service = Mockito.mock(UserService.class);

		this.userServiceMock = service;

		return new UserController(service, messageSource());
	}

	/********************************************************************************************/

	private MockMvc mockMvc;
	private UserService userServiceMock;

	@BeforeEach
	public void setup() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/jsp/view/");
		viewResolver.setSuffix(".jsp");

		mockMvc = MockMvcBuilders.standaloneSetup(getController())
				.setViewResolvers(viewResolver).build();
	}
	
	/**
	 * Default constructor.
	 */
	public TestUserController() {
		super();
	}
	
	/****TESTS SIGNUP ********************************************************************************/

	@Test
	public void TestUserController_SignUp() throws IOException, DuplicateInstanceException, Exception {
		UserImpl user = new UserImpl(USER_NAME, PASSWORD);
		
		when(userServiceMock.signUp(any(UserImpl.class))).thenReturn(1L);
		
		//Comprueba lo devuelto por el Controlador
		mockMvc.perform(post(UrlConfig.URL_USER_REGISTER_POST).contentType(APPLICATION_JSON_UTF8)
				.content(convertObjectToJsonBytes(user)))
				.andExpect(status().isCreated())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.userName").value(USER_NAME))
				.andExpect(jsonPath("$.password").value(PASSWORD))
				.andExpect(jsonPath("$.jwt").isString()); //TODO: más comprobaciones?
				//.andExpect(header().string("Location", "/users/signUp/1"));	//TODO: devuelve todo ->http://localhost:8080/users/signUp/1
		
	
		 ArgumentCaptor<UserImpl> dtoCaptor = ArgumentCaptor.forClass(UserImpl.class); 
		
		 //Comprueba que se llama 1 única vez al servicio y que no se llama a otros métodos del servicio
		 verify(userServiceMock, times(1)).signUp(dtoCaptor.capture()); 
		 verifyNoMoreInteractions(userServiceMock);
		 
		 //Comprueba los valores de lo captado
		 UserImpl dtoArgument = dtoCaptor.getValue();
		 assertThat(dtoArgument.getPassword(), is(PASSWORD));
		 assertThat(dtoArgument.getUserName(), is(USER_NAME));
	}

	@Test
	public void TestUserController_SignUp_DuplicateInstanceException() throws IOException, DuplicateInstanceException, Exception{
		UserImpl user = new UserImpl(USER_NAME, PASSWORD);
		
		//Lanza un error cada vez que llamas a signUp
		doThrow(new DuplicateInstanceException(" ",user))
				.when(userServiceMock).signUp(any(UserImpl.class));
		
		mockMvc.perform(post(UrlConfig.URL_USER_REGISTER_POST)
				.contentType(APPLICATION_JSON_UTF8)
				.content(convertObjectToJsonBytes(user)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.globalError").value("project.exceptions.DuplicateInstanceException"))
				.andExpect(jsonPath("$.fieldErrors").isEmpty());
		
	}
	
	/**** TEST LOGIN ***********************************************************************************/
	
	@Test
	public void TestUserController_login() throws IOException, IncorrectLoginException, Exception {
		LoginParamsDto login = new LoginParamsDto();
		login.setUserName(USER_NAME);
		login.setPassword(PASSWORD);
				
		UserImpl user = new UserImpl(USER_NAME, PASSWORD);
		when(userServiceMock.login(any(LoginParamsDto.class))).thenReturn(user);
		
		//Comprueba lo devuelto por el Controlador
		mockMvc.perform(post(UrlConfig.URL_USER_LOGIN_POST).contentType(APPLICATION_JSON_UTF8)
				.content(convertObjectToJsonBytes(login)))
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.userName").value(USER_NAME))
				.andExpect(jsonPath("$.password").value(PASSWORD))
				.andExpect(jsonPath("$.jwt").isString()); //TODO: más comprobaciones?
				
	
		 ArgumentCaptor<LoginParamsDto> dtoCaptor = ArgumentCaptor.forClass(LoginParamsDto.class); 
		
		 //Comprueba que se llama 1 única vez al servicio y que no se llama a otros métodos del servicio
		 verify(userServiceMock, times(1)).login(dtoCaptor.capture()); 
		 verifyNoMoreInteractions(userServiceMock);
		 
		 //Comprueba los valores de lo captado
		 LoginParamsDto dtoArgument = dtoCaptor.getValue();
		 assertThat(dtoArgument.getPassword(), is(PASSWORD));
		 assertThat(dtoArgument.getUserName(), is(USER_NAME));
	}
	
	@Test
	public void TestUserController_login_IncorrectLoginException() throws IOException, IncorrectLoginException, Exception{
		LoginParamsDto login = new LoginParamsDto();
		login.setUserName(USER_NAME);
		login.setPassword(PASSWORD);
				
		doThrow(new IncorrectLoginException(USER_NAME, PASSWORD))
				.when(userServiceMock).login(any(LoginParamsDto.class));

		mockMvc.perform(post(UrlConfig.URL_USER_LOGIN_POST)
				.contentType(APPLICATION_JSON_UTF8)
				.content(convertObjectToJsonBytes(login)))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.globalError").value("project.exceptions.IncorrectLoginException"))
				.andExpect(jsonPath("$.fieldErrors").isEmpty());
		
	}
	
	/*** UTILS ***********************************************************************************/
	
	public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		return mapper.writeValueAsBytes(object);
	}


}
