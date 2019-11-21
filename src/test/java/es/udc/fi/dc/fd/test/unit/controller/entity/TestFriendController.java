package es.udc.fi.dc.fd.test.unit.controller.entity;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
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

import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import es.udc.fi.dc.fd.controller.exception.AlreadyAceptedException;
import es.udc.fi.dc.fd.controller.exception.AlreadyBlockedException;
import es.udc.fi.dc.fd.controller.exception.AlreadyRejectedException;
import es.udc.fi.dc.fd.controller.exception.InstanceNotFoundException;
import es.udc.fi.dc.fd.controller.exception.InvalidRecommendationException;
import es.udc.fi.dc.fd.controller.exception.ItsNotYourFriendException;
import es.udc.fi.dc.fd.controller.exception.NoMoreSuggestionFound;
import es.udc.fi.dc.fd.controller.exception.RequestParamException;
import es.udc.fi.dc.fd.dtos.IdDto;
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

	private static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	/****
	 * GET FRIEND LIST TESTS
	 ****************************************************************/

	@Test
	public void TestFriendController_GetFriendList()
			throws InstanceNotFoundException, RequestParamException, Exception {

		final UserImpl friend1 = CreateUser(USERID_FRIEND1);
		final List<UserImpl> listFriends = new ArrayList<>();
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

	/****
	 * ACEPT REQUEST TESTS
	 ****************************************************************/

	@DisplayName("AceptRequest - ok (OK)")
	public void TestFriendController_AceptRequest()
			throws InstanceNotFoundException, InvalidRecommendationException, AlreadyRejectedException, Exception {

		// @formatter:off
		mockMvc.perform(post(UrlConfig.URL_FRIEND_ACCEPT_POST)
				.contentType(APPLICATION_JSON_UTF8)
				.requestAttr("userId", USERID_OK)
				.content(Utils.convertObjectToJsonBytes(new IdDto(OBJECT_OK))))
		.andExpect(status().isOk());
		// @formatter:on

		final ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
		final ArgumentCaptor<Long> objectCaptor = ArgumentCaptor.forClass(Long.class);

		// Comprueba que final se llama 1 final única vez al servicio + no llama final a
		// otros
		verify(friendServiceMock, times(1)).acceptRecommendation(userIdCaptor.capture(), objectCaptor.capture());
		verifyNoMoreInteractions(friendServiceMock);

		assertThat(userIdCaptor.getValue(), is(USERID_OK));
		assertThat(objectCaptor.getValue(), is(OBJECT_OK));

	}

	@Test
	@DisplayName("AceptRequest - InstanceNotFoundException (NOT_FOUND)")
	public void TestFriendController_AceptRequest_InstanceNotFoundException()
			throws InstanceNotFoundException, InvalidRecommendationException, AlreadyRejectedException, Exception {

		doThrow(new InstanceNotFoundException("", USERID_NOTFOUND)).when(friendServiceMock)
		.acceptRecommendation(USERID_NOTFOUND, OBJECT_OK);

		// @formatter:off
		mockMvc.perform(post(UrlConfig.URL_FRIEND_ACCEPT_POST)
				.contentType(APPLICATION_JSON_UTF8)
				.requestAttr("userId", USERID_NOTFOUND)
				.content(Utils.convertObjectToJsonBytes(new IdDto(OBJECT_OK))))
		.andExpect(status().isNotFound());
		// @formatter:on

		final ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
		final ArgumentCaptor<Long> objectCaptor = ArgumentCaptor.forClass(Long.class);

		// Comprueba que final se llama 1 final única vez al servicio + no llama final a
		// otros
		verify(friendServiceMock, times(1)).acceptRecommendation(userIdCaptor.capture(), objectCaptor.capture());
		verifyNoMoreInteractions(friendServiceMock);

		assertThat(userIdCaptor.getValue(), is(USERID_NOTFOUND));
		assertThat(objectCaptor.getValue(), is(OBJECT_OK));

	}

	@Test
	@DisplayName("AceptRequest - InvalidRecommendationException (NOT_FOUND)")
	public void TestFriendController_AceptRequest_InvalidRecommendationException()
			throws InstanceNotFoundException, InvalidRecommendationException, AlreadyRejectedException, Exception {

		doThrow(new InvalidRecommendationException("", OBJECT_NOTFOUND)).when(friendServiceMock)
		.acceptRecommendation(USERID_OK, OBJECT_NOTFOUND);

		// @formatter:off
		mockMvc.perform(post(UrlConfig.URL_FRIEND_ACCEPT_POST)
				.contentType(APPLICATION_JSON_UTF8)
				.requestAttr("userId", USERID_OK)
				.content(Utils.convertObjectToJsonBytes(new IdDto(OBJECT_NOTFOUND))))
		.andExpect(status().isNotFound());
		// @formatter:on

		final ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
		final ArgumentCaptor<Long> objectCaptor = ArgumentCaptor.forClass(Long.class);

		// Comprueba que final se llama 1 final única vez al servicio + no llama final a
		// otros
		verify(friendServiceMock, times(1)).acceptRecommendation(userIdCaptor.capture(), objectCaptor.capture());
		verifyNoMoreInteractions(friendServiceMock);

		assertThat(userIdCaptor.getValue(), is(USERID_OK));
		assertThat(objectCaptor.getValue(), is(OBJECT_NOTFOUND));
	}

	@Test
	@DisplayName("AceptRequest - AlreadyRejectedException (BAD_REQUEST)")
	public void TestFriendController_AceptRequest_AlreadyRejectedException()
			throws InstanceNotFoundException, InvalidRecommendationException, AlreadyRejectedException, Exception {

		doThrow(new AlreadyRejectedException("", OBJECT_REJECTED)).when(friendServiceMock)
		.acceptRecommendation(USERID_OK, OBJECT_REJECTED);

		// @formatter:off
		mockMvc.perform(post(UrlConfig.URL_FRIEND_ACCEPT_POST)
				.contentType(APPLICATION_JSON_UTF8)
				.requestAttr("userId", USERID_OK)
				.content(Utils.convertObjectToJsonBytes(new IdDto(OBJECT_REJECTED))))
		.andExpect(status().isBadRequest());
		// @formatter:on

		final ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
		final ArgumentCaptor<Long> objectCaptor = ArgumentCaptor.forClass(Long.class);

		// Comprueba que final se llama 1 final única vez al servicio + no llama final a
		// otros
		verify(friendServiceMock, times(1)).acceptRecommendation(userIdCaptor.capture(), objectCaptor.capture());
		verifyNoMoreInteractions(friendServiceMock);

		assertThat(userIdCaptor.getValue(), is(USERID_OK));
		assertThat(objectCaptor.getValue(), is(OBJECT_REJECTED));

	}

	@Test
	@DisplayName("AceptRequest - AlreadyAceptedException (BAD_REQUEST)")
	public void TestFriendController_AceptRequest_AlreadyAceptedException()
			throws InstanceNotFoundException, InvalidRecommendationException, AlreadyRejectedException, Exception {

		doThrow(new AlreadyAceptedException("", OBJECT_ACCEPTED)).when(friendServiceMock)
		.acceptRecommendation(USERID_OK, OBJECT_ACCEPTED);

		// @formatter:off
		mockMvc.perform(post(UrlConfig.URL_FRIEND_ACCEPT_POST)
				.contentType(APPLICATION_JSON_UTF8)
				.requestAttr("userId", USERID_OK)
				.content(Utils.convertObjectToJsonBytes(new IdDto(OBJECT_ACCEPTED))))
		.andExpect(status().isBadRequest());
		// @formatter:on

		final ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
		final ArgumentCaptor<Long> objectCaptor = ArgumentCaptor.forClass(Long.class);

		// Comprueba que final se llama 1 final única vez al servicio + no llama final a
		// otros
		verify(friendServiceMock, times(1)).acceptRecommendation(userIdCaptor.capture(), objectCaptor.capture());
		verifyNoMoreInteractions(friendServiceMock);

		assertThat(userIdCaptor.getValue(), is(USERID_OK));
		assertThat(objectCaptor.getValue(), is(OBJECT_ACCEPTED));

	}

	/****
	 * REJECT REQUEST TESTS
	 *
	 * URL_FRIEND_REJECT_POST rejectRecommendation
	 ****************************************************************/

	@Test
	@DisplayName("RejectRequest - ok (OK)")
	public void TestFriendController_RejectRequest()
			throws InstanceNotFoundException, InvalidRecommendationException, AlreadyRejectedException, Exception {

		// @formatter:off
		mockMvc.perform(post(UrlConfig.URL_FRIEND_REJECT_POST)
				.contentType(APPLICATION_JSON_UTF8)
				.requestAttr("userId", USERID_OK)
				.content(Utils.convertObjectToJsonBytes(new IdDto(OBJECT_OK))))
		.andExpect(status().isOk());
		// @formatter:on

		final ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
		final ArgumentCaptor<Long> objectCaptor = ArgumentCaptor.forClass(Long.class);

		// Comprueba que final se llama 1 final única vez al servicio + no llama final a
		// otros
		verify(friendServiceMock, times(1)).rejectRecommendation(userIdCaptor.capture(), objectCaptor.capture());
		verifyNoMoreInteractions(friendServiceMock);

		assertThat(userIdCaptor.getValue(), is(USERID_OK));
		assertThat(objectCaptor.getValue(), is(OBJECT_OK));
	}

	@Test
	@DisplayName("RejectRequest - InstanceNotFoundException (NOT_FOUND)")
	public void TestFriendController_RejectRequest_InstanceNotFoundException()
			throws InstanceNotFoundException, InvalidRecommendationException, AlreadyRejectedException, Exception {

		doThrow(new InstanceNotFoundException("", USERID_NOTFOUND)).when(friendServiceMock)
		.rejectRecommendation(USERID_NOTFOUND, OBJECT_OK);

		// @formatter:off
		mockMvc.perform(post(UrlConfig.URL_FRIEND_REJECT_POST)
				.contentType(APPLICATION_JSON_UTF8)
				.requestAttr("userId", USERID_NOTFOUND)
				.content(Utils.convertObjectToJsonBytes(new IdDto(OBJECT_OK))))
		.andExpect(status().isNotFound());
		// @formatter:on

		final ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
		final ArgumentCaptor<Long> objectCaptor = ArgumentCaptor.forClass(Long.class);

		// Comprueba que final se llama 1 final única vez al servicio + no llama final a
		// otros
		verify(friendServiceMock, times(1)).rejectRecommendation(userIdCaptor.capture(), objectCaptor.capture());
		verifyNoMoreInteractions(friendServiceMock);

		assertThat(userIdCaptor.getValue(), is(USERID_NOTFOUND));
		assertThat(objectCaptor.getValue(), is(OBJECT_OK));

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

	@DisplayName("RejectRequest - InvalidRecommendationException (BAD_REQUEST)")
	public void TestFriendController_RejectRequest_InvalidRecommendationException()
			throws InstanceNotFoundException, InvalidRecommendationException, AlreadyRejectedException, Exception {

		doThrow(new InvalidRecommendationException("", OBJECT_NOTFOUND)).when(friendServiceMock)
		.rejectRecommendation(USERID_OK, OBJECT_NOTFOUND);

		// @formatter:off
		mockMvc.perform(post(UrlConfig.URL_FRIEND_REJECT_POST )
				.contentType(APPLICATION_JSON_UTF8)
				.requestAttr("userId", USERID_OK)
				.content(Utils.convertObjectToJsonBytes(new IdDto(OBJECT_NOTFOUND))))
		.andExpect(status().isNotFound());
		// @formatter:on

		final ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
		final ArgumentCaptor<Long> objectCaptor = ArgumentCaptor.forClass(Long.class);

		// Comprueba que final se llama 1 final única vez al servicio + no llama final a
		// otros
		verify(friendServiceMock, times(1)).rejectRecommendation(userIdCaptor.capture(), objectCaptor.capture());
		verifyNoMoreInteractions(friendServiceMock);

		assertThat(userIdCaptor.getValue(), is(USERID_OK));
		assertThat(objectCaptor.getValue(), is(OBJECT_NOTFOUND));
	}

	@Test
	@DisplayName("RejectRequest - AlreadyRejectedException (BAD_REQUEST)")
	public void TestFriendController_RejectRequest_AlreadyRejectedException()
			throws InstanceNotFoundException, InvalidRecommendationException, AlreadyRejectedException, Exception {

		doThrow(new AlreadyRejectedException("", OBJECT_REJECTED)).when(friendServiceMock)
		.rejectRecommendation(USERID_OK, OBJECT_REJECTED);

		// @formatter:off
		mockMvc.perform(post(UrlConfig.URL_FRIEND_REJECT_POST)
				.contentType(APPLICATION_JSON_UTF8)
				.requestAttr("userId", USERID_OK)
				.content(Utils.convertObjectToJsonBytes(new IdDto(OBJECT_REJECTED))))
		.andExpect(status().isBadRequest());
		// @formatter:on

		final ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
		final ArgumentCaptor<Long> objectCaptor = ArgumentCaptor.forClass(Long.class);

		// Comprueba que final se llama 1 final única vez al servicio + no llama final a
		// otros
		verify(friendServiceMock, times(1)).rejectRecommendation(userIdCaptor.capture(), objectCaptor.capture());
		verifyNoMoreInteractions(friendServiceMock);

		assertThat(userIdCaptor.getValue(), is(USERID_OK));
		assertThat(objectCaptor.getValue(), is(OBJECT_REJECTED));

	}

	@Test
	@DisplayName("RejectRequest - AlreadyAceptedException (BAD_REQUEST)")
	public void TestFriendController_RejectRequest_AlreadyAceptedException()
			throws InstanceNotFoundException, InvalidRecommendationException, AlreadyRejectedException, Exception {

		doThrow(new AlreadyAceptedException("", OBJECT_ACCEPTED)).when(friendServiceMock)
		.rejectRecommendation(USERID_OK, OBJECT_ACCEPTED);

		// @formatter:off
		mockMvc.perform(post(UrlConfig.URL_FRIEND_REJECT_POST)
				.contentType(APPLICATION_JSON_UTF8)
				.requestAttr("userId", USERID_OK)
				.content(Utils.convertObjectToJsonBytes(new IdDto(OBJECT_ACCEPTED))))
		.andExpect(status().isBadRequest());
		// @formatter:on

		final ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
		final ArgumentCaptor<Long> objectCaptor = ArgumentCaptor.forClass(Long.class);

		// Comprueba que final se llama 1 final única vez al servicio + no llama final a
		// otros
		verify(friendServiceMock, times(1)).rejectRecommendation(userIdCaptor.capture(), objectCaptor.capture());
		verifyNoMoreInteractions(friendServiceMock);

		assertThat(userIdCaptor.getValue(), is(USERID_OK));
		assertThat(objectCaptor.getValue(), is(OBJECT_ACCEPTED));

	}

	/****
	 * ACEPT REQUEST TESTS
	 *
	 ****************************************************************/
	@Test
	public void TestFriendController_SuggestFriend()
			throws InstanceNotFoundException, NoMoreSuggestionFound, Exception {
		final UserImpl suggestion = CreateUser(SUGGESTIONID);
		when(friendServiceMock.suggestFriend(USERID_OK)).thenReturn(Optional.of(suggestion));

		// @formatter:off
		mockMvc.perform(get(UrlConfig.URL_FRIEND_SUGGESTION_GET)
				.contentType(APPLICATION_JSON_UTF8)
				.requestAttr("userId", USERID_OK)
				)
		.andExpect(status().isOk())
		.andExpect(content().contentType(APPLICATION_JSON_UTF8))
		.andExpect(jsonPath("$.userName").value(SUGGESTIONNAME))
		.andExpect(jsonPath("$.id").value(SUGGESTIONID))
		.andExpect(jsonPath("$.age").value(SUGGESTIONAGE))
		.andExpect(jsonPath("$.sex").value(SUGGESTIONSEX))
		.andExpect(jsonPath("$.city").value(SUGGESTIONCITY))
		.andExpect(jsonPath("$.description").value(SUGGESTIONDESCRIPTION));
		// @formatter:on

		final ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
		// Comprueba que final se llama 1 final única vez al servicio + no llama final a
		// otros
		verify(friendServiceMock, times(1)).suggestFriend(userIdCaptor.capture());
		verifyNoMoreInteractions(friendServiceMock);

		assertThat(userIdCaptor.getValue(), is(USERID_OK));

	}

	@Test
	public void TestFriendController_SuggestFriend_InstanceNotFoundException()
			throws InstanceNotFoundException, NoMoreSuggestionFound, Exception {

		doThrow(new InstanceNotFoundException("", USERID_NOTFOUND)).when(friendServiceMock)
		.suggestFriend(USERID_NOTFOUND);

		// @formatter:off
		mockMvc.perform(get(UrlConfig.URL_FRIEND_SUGGESTION_GET)
				.contentType(APPLICATION_JSON_UTF8)
				.requestAttr("userId", USERID_NOTFOUND)
				)
		.andExpect(status().isNotFound());
		// @formatter:on

		final ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
		// Comprueba que final se llama 1 final única vez al servicio + no llama final a
		// otros
		verify(friendServiceMock, times(1)).suggestFriend(userIdCaptor.capture());
		verifyNoMoreInteractions(friendServiceMock);

		assertThat(userIdCaptor.getValue(), is(USERID_NOTFOUND));
	}

	@Test
	public void TestFriendController_SuggestFriend_NoMoreSuggestionFound()
			throws InstanceNotFoundException, NoMoreSuggestionFound, Exception {

		when(friendServiceMock.suggestFriend(USERID_NM)).thenReturn(Optional.empty());

		// @formatter:off
		mockMvc.perform(get(UrlConfig.URL_FRIEND_SUGGESTION_GET)
				.contentType(APPLICATION_JSON_UTF8)
				.requestAttr("userId", USERID_NM)
				)
		.andExpect(status().isBadRequest());
		// @formatter:on

		final ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
		// Comprueba que final se llama 1 final única vez al servicio + no llama final a
		// otrosURL_FRIEND_SUGGESTION_GET
		verify(friendServiceMock, times(1)).suggestFriend(userIdCaptor.capture());
		verifyNoMoreInteractions(friendServiceMock);

		assertThat(userIdCaptor.getValue(), is(USERID_NM));
	}

	@Test
	public void TestFriendController_BlockUser()
			throws InstanceNotFoundException, NoMoreSuggestionFound, Exception {
		final IdDto idDto = new IdDto(2);

		// @formatter:off
		mockMvc.perform(post(UrlConfig.URL_FRIEND_BLOCK_POST)
				.contentType(APPLICATION_JSON_UTF8)
				.requestAttr("userId", 1L)
				.content(Utils.convertObjectToJsonBytes(idDto)))
		.andExpect(status().isNoContent());
		// @formatter:on

		final ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
		final ArgumentCaptor<Long> friendIdCaptor = ArgumentCaptor.forClass(Long.class);
		verify(friendServiceMock, times(1)).blockUser(userIdCaptor.capture(), friendIdCaptor.capture());
		verifyNoMoreInteractions(friendServiceMock);
		assertThat(userIdCaptor.getValue(), is(1L));
		assertThat(friendIdCaptor.getValue(), is(2L));
	}

	@Test
	public void TestFriendController_BlockUser_InstanceNotFoundException()
			throws InstanceNotFoundException, NoMoreSuggestionFound, Exception {
		final IdDto idDto = new IdDto(2);

		doThrow(new InstanceNotFoundException("", USERID_NOTFOUND)).when(friendServiceMock)
		.blockUser(USERID_NOTFOUND, 2L);

		// @formatter:off
		mockMvc.perform(post(UrlConfig.URL_FRIEND_BLOCK_POST)
				.contentType(APPLICATION_JSON_UTF8)
				.requestAttr("userId", USERID_NOTFOUND)
				.content(Utils.convertObjectToJsonBytes(idDto)))
		.andExpect(status().isNotFound());
		// @formatter:on

		final ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
		final ArgumentCaptor<Long> friendIdCaptor = ArgumentCaptor.forClass(Long.class);
		verify(friendServiceMock, times(1)).blockUser(userIdCaptor.capture(), friendIdCaptor.capture());
		verifyNoMoreInteractions(friendServiceMock);
		assertThat(userIdCaptor.getValue(), is(USERID_NOTFOUND));
		assertThat(friendIdCaptor.getValue(), is(2L));
	}

	@Test
	public void TestFriendController_BlockUser_AlreadyBlockedException()
			throws InstanceNotFoundException, NoMoreSuggestionFound, Exception {
		final IdDto idDto = new IdDto(2);

		doThrow(new AlreadyBlockedException("Already blocked")).when(friendServiceMock).blockUser(USERID_OK,
				2L);

		// @formatter:off
		mockMvc.perform(post(UrlConfig.URL_FRIEND_BLOCK_POST)
				.contentType(APPLICATION_JSON_UTF8)
				.requestAttr("userId", USERID_OK)
				.content(Utils.convertObjectToJsonBytes(idDto)))
		.andExpect(status().isForbidden());
		// @formatter:on

		final ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
		final ArgumentCaptor<Long> friendIdCaptor = ArgumentCaptor.forClass(Long.class);
		verify(friendServiceMock, times(1)).blockUser(userIdCaptor.capture(), friendIdCaptor.capture());
		verifyNoMoreInteractions(friendServiceMock);
		assertThat(userIdCaptor.getValue(), is(USERID_OK));
		assertThat(friendIdCaptor.getValue(), is(2L));
	}

	@Test
	public void TestFriendController_BlockUser_ItsNotYourFriendException()
			throws InstanceNotFoundException, NoMoreSuggestionFound, Exception {
		final IdDto idDto = new IdDto(2);

		doThrow(new ItsNotYourFriendException("It's not your friend")).when(friendServiceMock).blockUser(USERID_OK, 2L);

		// @formatter:off
		mockMvc.perform(post(UrlConfig.URL_FRIEND_BLOCK_POST)
				.contentType(APPLICATION_JSON_UTF8)
				.requestAttr("userId", USERID_OK)
				.content(Utils.convertObjectToJsonBytes(idDto)))
		.andExpect(status().isNotFound());
		// @formatter:on

		final ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
		final ArgumentCaptor<Long> friendIdCaptor = ArgumentCaptor.forClass(Long.class);
		verify(friendServiceMock, times(1)).blockUser(userIdCaptor.capture(), friendIdCaptor.capture());
		verifyNoMoreInteractions(friendServiceMock);
		assertThat(userIdCaptor.getValue(), is(USERID_OK));
		assertThat(friendIdCaptor.getValue(), is(2L));
	}

}
