package es.udc.fi.dc.fd.test.unit.controller.entity;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.AfterClass;
import org.junit.BeforeClass;
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

import es.udc.fi.dc.fd.controller.entity.FriendController;
import es.udc.fi.dc.fd.controller.exception.InstanceNotFoundException;
import es.udc.fi.dc.fd.controller.exception.RequestParamException;
import es.udc.fi.dc.fd.model.SexCriteriaEnum;
import es.udc.fi.dc.fd.model.persistence.UserImpl;
import es.udc.fi.dc.fd.service.BlockFriendList;
import es.udc.fi.dc.fd.service.FriendService;
import es.udc.fi.dc.fd.test.config.UrlConfig;

@RunWith(JUnitPlatform.class)
public class TestFriendController {
	FriendService friendServiceMock;
	private MockMvc mockMvc;

	private final FriendController getController() {
		final FriendService service; // To Mocked service
		service = Mockito.mock(FriendService.class);
		this.friendServiceMock = service;

		return new FriendController(service, messageSource());
	}

	private MessageSource messageSource() {
		final ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();

		messageSource.setBasename("i18n/messages");
		messageSource.setUseCodeAsDefaultMessage(true);

		return messageSource;
	}

	@BeforeEach
	public void setup() {
		final InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/jsp/view/");
		viewResolver.setSuffix(".jsp");

		mockMvc = MockMvcBuilders.standaloneSetup(getController()).setViewResolvers(viewResolver).build();
	}

	public TestFriendController() {
		super();
	}

	private LocalDateTime getDateTimeFromAge(int age) {
		assert age > 0;
		return LocalDate.now().minusYears(age).atStartOfDay();
	}

	private UserImpl CreateUser(long id) {
		final UserImpl user = new UserImpl();
		user.setCriteriaMaxAge(99);
		user.setCriteriaMinAge(18);
		user.setPassword("pass");
		user.setCriteriaSex(SexCriteriaEnum.ANY);
		user.setDate(getDateTimeFromAge(SUGGESTIONAGE));
		user.setDescription(SUGGESTIONDESCRIPTION);
		user.setCity(SUGGESTIONCITY);
		user.setId(id);
		user.setSex(SUGGESTIONSEX);
		user.setUserName(SUGGESTIONNAME);
		return user;
	}

	private static ValidatorFactory validatorFactory;
	private static Validator validator;

	@BeforeClass
	public static void createValidator() {
		validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
	}

	@AfterClass
	public static void close() {
		validatorFactory.close();
	}

	private static final Long USERID_OK = 1L;
	private static final Long USERID_NOTFOUND = 2L;
	private static final Long OBJECT_OK = 3L;
	private static final Long OBJECT_NOTFOUND = 4L;
	private static final Long OBJECT_ACCEPTED = 5L;
	private static final Long OBJECT_REJECTED = 6L;
	private static final Long USERID_NM = 8L;
	private static final Long USERID_FRIEND1 = 9L;

	private static final Long SUGGESTIONID = 9L;
	private static final int SUGGESTIONAGE = 20;
	private static final String SUGGESTIONSEX = "Patata";
	private static final String SUGGESTIONNAME = "Patatón";
	private static final String SUGGESTIONDESCRIPTION = "Desc";
	private static final String SUGGESTIONCITY = "Coruña";

	private static UserImpl SUGGESTION = null;
	private static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	/****
	 * GET FRIEND LIST TESTS
	 *
	 ****************************************************************/

	@Test
	public void TestFriendController_GetFriendList()
			throws InstanceNotFoundException, RequestParamException, Exception {

		final UserImpl friend1 = CreateUser(USERID_FRIEND1);
		final List <UserImpl> listFriends = new ArrayList<>();
		listFriends.add(friend1);

		final BlockFriendList<UserImpl> block = new BlockFriendList<>(listFriends, false);
		when(friendServiceMock.getFriendList(USERID_OK, 0, 10)).thenReturn(block);

		// @formatter:off
		mockMvc.perform(get(UrlConfig.URL_FRIEND_FRIENDLIST_GET)
				.contentType(APPLICATION_JSON_UTF8)
				.requestAttr("userId", USERID_OK)
				.requestAttr("page", 0)
				.requestAttr("size", 0))
		.andExpect(status().isOk())
		.andExpect(content().contentType(APPLICATION_JSON_UTF8))
		.andExpect(jsonPath("$.friends").isNotEmpty())
		.andExpect(jsonPath("$.existMoreFriends").value(false));
		// @formatter:on

		final ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
		final ArgumentCaptor<Integer> pageCaptor = ArgumentCaptor.forClass(int.class);
		final ArgumentCaptor<Integer> sizeCaptor = ArgumentCaptor.forClass(int.class);

		// Comprueba que final se llama 1 final única vez al servicio + no llama final a
		// otros
		verify(friendServiceMock, times(1)).getFriendList(userIdCaptor.capture(), pageCaptor.capture(),
				sizeCaptor.capture());
		verifyNoMoreInteractions(friendServiceMock);

		assertThat(userIdCaptor.getValue(), is(USERID_OK));
		assertThat(pageCaptor.getValue(), is(0));
		assertThat(sizeCaptor.getValue(), is(10));
	}

	@Test
	public void TestFriendController_GetFriendList_InstanceNotFoundException()
			throws InstanceNotFoundException, RequestParamException, Exception {

		final List<UserImpl> listFriends = new ArrayList<>();
		final BlockFriendList<UserImpl> block = new BlockFriendList<>(listFriends, false);

		doThrow(new InstanceNotFoundException("", USERID_NOTFOUND)).when(friendServiceMock)
		.getFriendList(USERID_NOTFOUND, 1, 10);

		// @formatter:off
		mockMvc.perform(get(UrlConfig.URL_FRIEND_FRIENDLIST_GET)
				.contentType(APPLICATION_JSON_UTF8)
				.requestAttr("userId", USERID_NOTFOUND)
				.param("page", "1")
				.param("size", "10"))
		.andExpect(status().isNotFound())
		.andExpect(content().contentType(APPLICATION_JSON_UTF8))
		.andExpect(jsonPath("$.globalError").value("project.exceptions.InstanceNotFoundException"));
		// @formatter:on

		final ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
		final ArgumentCaptor<Integer> pageCaptor = ArgumentCaptor.forClass(int.class);
		final ArgumentCaptor<Integer> sizeCaptor = ArgumentCaptor.forClass(int.class);

		// Comprueba que final se llama 1 final única vez al servicio + no llama final a
		// otros
		verify(friendServiceMock, times(1)).getFriendList(userIdCaptor.capture(), pageCaptor.capture(),
				sizeCaptor.capture());
		verifyNoMoreInteractions(friendServiceMock);

		assertThat(userIdCaptor.getValue(), is(USERID_NOTFOUND));
		assertThat(pageCaptor.getValue(), is(1));
		assertThat(sizeCaptor.getValue(), is(10));

	}

	@Test
	public void TestFriendController_GetFriendList_RequestParamException()
			throws InstanceNotFoundException, RequestParamException, Exception {

		doThrow(new RequestParamException("")).when(friendServiceMock).getFriendList(USERID_OK, -1, 10);

		// @formatter:off
		mockMvc.perform(get(UrlConfig.URL_FRIEND_FRIENDLIST_GET)
				.contentType(APPLICATION_JSON_UTF8)
				.requestAttr("userId", USERID_OK)
				.param("page", "-1")
				.param("size", "10"))
		.andExpect(status().isBadRequest())
		.andExpect(content().contentType(APPLICATION_JSON_UTF8))
		.andExpect(jsonPath("$.globalError").value("project.exceptions.RequestParamException"));
		// @formatter:on

		final ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
		final ArgumentCaptor<Integer> pageCaptor = ArgumentCaptor.forClass(int.class);
		final ArgumentCaptor<Integer> sizeCaptor = ArgumentCaptor.forClass(int.class);

		// Comprueba que final se llama 1 final única vez al servicio + no llama final a
		// otros
		verify(friendServiceMock, times(1)).getFriendList(userIdCaptor.capture(), pageCaptor.capture(),
				sizeCaptor.capture());
		verifyNoMoreInteractions(friendServiceMock);

		assertThat(userIdCaptor.getValue(), is(USERID_OK));
		assertThat(pageCaptor.getValue(), is(-1));
		assertThat(sizeCaptor.getValue(), is(10));

	}
}
