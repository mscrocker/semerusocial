package es.udc.fi.dc.fd.test.unit.controller.entity;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.Period;

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

import es.udc.fi.dc.fd.controller.entity.UserController;
import es.udc.fi.dc.fd.controller.exception.DuplicateInstanceException;
import es.udc.fi.dc.fd.controller.exception.IncorrectLoginException;
import es.udc.fi.dc.fd.controller.exception.InstanceNotFoundException;
import es.udc.fi.dc.fd.dtos.LoginParamsDto;
import es.udc.fi.dc.fd.dtos.RegisterParamsDto;
import es.udc.fi.dc.fd.dtos.UserConversor;
import es.udc.fi.dc.fd.model.persistence.UserImpl;
import es.udc.fi.dc.fd.service.UserService;
import es.udc.fi.dc.fd.test.config.UrlConfig;

@RunWith(JUnitPlatform.class)
public final class TestUserController {

	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
	public static final String PASSWORD = "pass";
	public static final String USER_NAME = "name";

	/********************************************************************************************/

	private MockMvc mockMvc;

	private UserService userServiceMock;

	/**
	 * Default constructor.
	 */
	public TestUserController() {
		super();
	}

	/**
	 * Returns a controller with mocked dependencies.
	 *
	 * @return a mocked controller
	 */
	private UserController getController() {
		final UserService service; // Mocked service

		service = Mockito.mock(UserService.class);

		userServiceMock = service;

		return new UserController(service, messageSource());
	}

	/******************************************************************************/

	private MessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();

		messageSource.setBasename("i18n/messages");
		messageSource.setUseCodeAsDefaultMessage(true);

		return messageSource;
	}

	@BeforeEach
	public void setup() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/jsp/view/");
		viewResolver.setSuffix(".jsp");

		mockMvc = MockMvcBuilders.standaloneSetup(getController()).setViewResolvers(viewResolver).build();
	}

	private UserImpl createUser(String userName, String password, LocalDateTime date, String sex, String city) {
		return new UserImpl(userName, password, date, sex, city);
	}
	private LocalDateTime getDateTime(int day, int month, int year) {
		return LocalDateTime.of(year, month, day, 00, 01);
	}
	
	/****
	 * TEST LOGIN
	 ***********************************************************************************/

	@Test
	public void TestUserController_login() throws IOException, IncorrectLoginException, Exception {
		LoginParamsDto login = new LoginParamsDto();
		login.setUserName(USER_NAME);
		login.setPassword(PASSWORD);

		UserImpl user = createUser(USER_NAME, PASSWORD, getDateTime(1,1,2000), "mujer", "coruna");
		when(userServiceMock.login(any(LoginParamsDto.class))).thenReturn(user);

		// Comprueba lo devuelto por el Controlador
		mockMvc.perform(post(UrlConfig.URL_USER_LOGIN_POST).contentType(APPLICATION_JSON_UTF8)
				.content(Utils.convertObjectToJsonBytes(login))).andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.userName").value(USER_NAME))
				.andExpect(jsonPath("$.jwt").isString()); 

		ArgumentCaptor<LoginParamsDto> dtoCaptor = ArgumentCaptor.forClass(LoginParamsDto.class);

		// Comprueba que se llama 1 única vez al servicio y que no se llama a otros
		// métodos del servicio
		verify(userServiceMock, times(1)).login(dtoCaptor.capture());
		verifyNoMoreInteractions(userServiceMock);

		// Comprueba los valores de lo captado
		LoginParamsDto dtoArgument = dtoCaptor.getValue();
		assertThat(dtoArgument.getPassword(), is(PASSWORD));
		assertThat(dtoArgument.getUserName(), is(USER_NAME));
	}

	@Test
	public void TestUserController_login_IncorrectLoginException()
			throws IOException, IncorrectLoginException, Exception {
		LoginParamsDto login = new LoginParamsDto();
		login.setUserName(USER_NAME);
		login.setPassword(PASSWORD);

		doThrow(new IncorrectLoginException(USER_NAME, PASSWORD)).when(userServiceMock)
				.login(any(LoginParamsDto.class));

		mockMvc.perform(post(UrlConfig.URL_USER_LOGIN_POST).contentType(APPLICATION_JSON_UTF8)
				.content(Utils.convertObjectToJsonBytes(login))).andExpect(status().isNotFound())
				.andExpect(jsonPath("$.globalError").value("project.exceptions.IncorrectLoginException"))
				.andExpect(jsonPath("$.fieldErrors").isEmpty());

		ArgumentCaptor<LoginParamsDto> dtoCaptor = ArgumentCaptor.forClass(LoginParamsDto.class);

		// Comprueba que se llama 1 única vez al servicio y que no se llama a otros
		// métodos del servicio
		verify(userServiceMock, times(1)).login(dtoCaptor.capture());
		verifyNoMoreInteractions(userServiceMock);

		// Comprueba los valores de lo captado
		LoginParamsDto dtoArgument = dtoCaptor.getValue();
		assertThat(dtoArgument.getPassword(), is(PASSWORD));
		assertThat(dtoArgument.getUserName(), is(USER_NAME));
	}

	/****
	 * TESTS SIGNUP
	 ********************************************************************************/

	@Test
	public void TestUserController_SignUp() throws IOException, DuplicateInstanceException, Exception {
		
		RegisterParamsDto params = new RegisterParamsDto(USER_NAME, PASSWORD, 1, 2, 2000 , "mujer",
				"coruna");
		
		when(userServiceMock.signUp(any(UserImpl.class))).thenReturn(1L);

		// Comprueba lo devuelto por el Controlador
		mockMvc.perform(post(UrlConfig.URL_USER_REGISTER_POST).contentType(APPLICATION_JSON_UTF8)
				.content(Utils.convertObjectToJsonBytes(params)))
				.andExpect(status().isCreated()).andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.userName").value(USER_NAME))
				.andExpect(jsonPath("$.jwt").isString()); 
		
		ArgumentCaptor<UserImpl> dtoCaptor = ArgumentCaptor.forClass(UserImpl.class);

		// Comprueba que se llama 1 única vez al servicio y que no se llama a otros
		// métodos del servicio
		verify(userServiceMock, times(1)).signUp(dtoCaptor.capture());
		verifyNoMoreInteractions(userServiceMock);

		// Comprueba los valores de lo captado
		UserImpl dtoArgument = dtoCaptor.getValue();
		assertThat(dtoArgument.getPassword(), is(PASSWORD));
		assertThat(dtoArgument.getUserName(), is(USER_NAME));
	}

	@Test
	public void TestUserController_SignUp_DuplicateInstanceException()
			throws IOException, DuplicateInstanceException, Exception {
		RegisterParamsDto params = new RegisterParamsDto(USER_NAME, PASSWORD, 1, 2, 2000 , "mujer",
				"coruna");

		// Lanza un error cada vez que llamas a signUp
		doThrow(new DuplicateInstanceException(" ", UserConversor.fromRegisterDto(params))).when(userServiceMock)
				.signUp(any(UserImpl.class));

		mockMvc.perform(post(UrlConfig.URL_USER_REGISTER_POST).contentType(APPLICATION_JSON_UTF8)
				.content(Utils.convertObjectToJsonBytes(params))).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.globalError").value("project.exceptions.DuplicateInstanceException"))
				.andExpect(jsonPath("$.fieldErrors").isEmpty());

		ArgumentCaptor<UserImpl> dtoCaptor = ArgumentCaptor.forClass(UserImpl.class);

		// Comprueba que se llama 1 única vez al servicio y que no se llama a otros
		// métodos del servicio
		verify(userServiceMock, times(1)).signUp(dtoCaptor.capture());
		verifyNoMoreInteractions(userServiceMock);

		// Comprueba los valores de lo captado
		UserImpl dtoArgument = dtoCaptor.getValue();
		assertThat(dtoArgument.getPassword(), is(PASSWORD));
		assertThat(dtoArgument.getUserName(), is(USER_NAME));
	}
	
	/****
	 * TESTS GETUSERDATA
	 ********************************************************************************/

	@Test
	public void TestUserController_GetUserData() throws InstanceNotFoundException,IOException, Exception {
		LocalDateTime today = LocalDateTime.now();
		LocalDateTime two= today.minusMonths(24);
		UserImpl user = createUser(USER_NAME, PASSWORD, two, "mujer", "coruna");
		Period age = Period.between(two.toLocalDate(), today.toLocalDate());

		when(userServiceMock.loginFromUserId(any(Long.class))).thenReturn(user);
		
		mockMvc.perform(get(UrlConfig.URL_USER_GET_USER_DATA)
				.contentType(APPLICATION_JSON_UTF8)
				.requestAttr("userId", 1L))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.age").value(age.getYears()))
				.andExpect(jsonPath("$.sex").value("mujer"))
				.andExpect(jsonPath("$.city").value("coruna"));
		
		ArgumentCaptor<Long> dtoCaptor = ArgumentCaptor.forClass(Long.class);

		// Comprueba que se llama 1 única vez al servicio y que no se llama a otros
		// métodos del servicio
		verify(userServiceMock, times(1)).loginFromUserId(dtoCaptor.capture());
		verifyNoMoreInteractions(userServiceMock);

		// Comprueba los valores de lo captado
		Long userId = dtoCaptor.getValue();
		assertThat(userId, is(1L));
		
	}
	
	@Test
	public void TestUserController_GetUserData_InstanceNotFoundException() throws InstanceNotFoundException,IOException, Exception {
		
		long userId = 1L;
		
		
		doThrow(new InstanceNotFoundException(USER_NAME, userId)).when(userServiceMock)
		.loginFromUserId(any(Long.class));

		mockMvc.perform(get(UrlConfig.URL_USER_GET_USER_DATA)
				.contentType(APPLICATION_JSON_UTF8)
				.requestAttr("userId", 1L))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.globalError").value("project.exceptions.InstanceNotFoundException"))
				.andExpect(jsonPath("$.fieldErrors").isEmpty());
		
		ArgumentCaptor<Long> dtoCaptor = ArgumentCaptor.forClass(Long.class);

		// Comprueba que se llama 1 única vez al servicio y que no se llama a otros
		// métodos del servicio
		verify(userServiceMock, times(1)).loginFromUserId(dtoCaptor.capture());
		verifyNoMoreInteractions(userServiceMock);

		// Comprueba los valores de lo captado
		Long userId2 = dtoCaptor.getValue();
		assertThat(userId2, is(1L));
	}

}
