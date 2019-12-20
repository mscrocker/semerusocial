package es.udc.fi.dc.fd.test.unit.controller.entity;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import es.udc.fi.dc.fd.controller.chat.ChatController;
import es.udc.fi.dc.fd.controller.exception.InstanceNotFoundException;
import es.udc.fi.dc.fd.controller.exception.ItsNotYourFriendException;
import es.udc.fi.dc.fd.controller.exception.RequestParamException;
import es.udc.fi.dc.fd.controller.exception.ValidationException;
import es.udc.fi.dc.fd.dtos.MessageDetailsDto;
import es.udc.fi.dc.fd.model.FriendChatTitle;
import es.udc.fi.dc.fd.service.Block;
import es.udc.fi.dc.fd.service.ChatService;
import es.udc.fi.dc.fd.service.UserService;
import es.udc.fi.dc.fd.test.config.UrlConfig;

@RunWith(JUnitPlatform.class)
public class TestChatController {

	private ChatService chatServiceMock;

	private MockMvc mockMvc;

	private final ChatController getController() {
		final ChatService service; // To Mocked service
		final UserService userService; // To Mocked service
		service = Mockito.mock(ChatService.class);
		userService = Mockito.mock(UserService.class);
		this.chatServiceMock = service;

		return new ChatController(userService, service, messageSource());
	}

	private MessageSource messageSource() {
		final ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();

		messageSource.setBasename("i18n/messages");
		messageSource.setUseCodeAsDefaultMessage(true);

		return messageSource;
	}

	public TestChatController() {
		super();
	}

	private static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	/*************************************************************
	 * FOR SETUP
	 *********************************************************/

	private static final Long USERID_OK = 1L;
	private static final Long USERID_NOTFOUND = -1L;
	private static final Long FRIENDID_OK = 2L;
	private static final Long USERID2_OK = 3L;
	private static final int PAGE = 0;
	private static final int PAGE_INVALID = -1;
	private static final int SIZE = 20;
	private static Block<MessageDetailsDto> CONVERSATION_BLOCK;
	private static Block<FriendChatTitle> FRIENDCHAT_BLOCK;

	@BeforeEach
	public void setup() throws InstanceNotFoundException, ItsNotYourFriendException, ValidationException, RequestParamException {
		final InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/jsp/view/");
		viewResolver.setSuffix(".jsp");

		mockMvc = MockMvcBuilders.standaloneSetup(getController()).setViewResolvers(viewResolver).build();

		// Initial values
		final List<MessageDetailsDto> items = new ArrayList<>();
		final Block<MessageDetailsDto> block = new Block<>(items, false);
		CONVERSATION_BLOCK = block;

		final List<FriendChatTitle> itemsFrie = new ArrayList<>();
		final Block<FriendChatTitle> blockFrie = new Block<>(itemsFrie, false);
		FRIENDCHAT_BLOCK = blockFrie;


		// Services mocked
		when(chatServiceMock.getConversation(USERID_OK, FRIENDID_OK, PAGE, SIZE)).thenReturn(CONVERSATION_BLOCK);
		when(chatServiceMock.getConversation(USERID_NOTFOUND, FRIENDID_OK, PAGE, SIZE)).thenThrow(new InstanceNotFoundException(null, USERID_NOTFOUND));
		when(chatServiceMock.getConversation(USERID_OK, USERID_OK, PAGE, SIZE)).thenThrow(new ValidationException(null));
		when(chatServiceMock.getConversation(USERID_OK, USERID2_OK, PAGE, SIZE)).thenThrow(new ItsNotYourFriendException(null));

		when(chatServiceMock.getUserConversations(USERID_OK, PAGE)).thenReturn(FRIENDCHAT_BLOCK);
		when(chatServiceMock.getUserConversations(USERID_NOTFOUND, PAGE))
		.thenThrow(new InstanceNotFoundException(null, USERID_NOTFOUND));
		when(chatServiceMock.getUserConversations(USERID_OK, PAGE_INVALID)).thenThrow(new RequestParamException(""));


	}

	/*************************************************************
	 * TESTS
	 **************************************************************/

	/****************
	 * TESTS GET CONVERSATION
	 *
	 ***************************************/
	@Test
	public void testGetConversation()
			throws InstanceNotFoundException, ItsNotYourFriendException, ValidationException, Exception {
		// @formatter:off
		mockMvc.perform(get(UrlConfig.URL_CHAT_GETCONVERSATION_GET)
				.contentType(APPLICATION_JSON_UTF8)
				.requestAttr("userId", USERID_OK)
				.param("friendId", Long.toString(FRIENDID_OK))
				.param("page", Integer.toString(PAGE))
				.param("size", Integer.toString(SIZE)))
		.andExpect(status().isOk())
		.andExpect(content().contentType(APPLICATION_JSON_UTF8))
		.andExpect(jsonPath("$.elements").isEmpty())
		.andExpect(jsonPath("$.existMoreElements").value(false));
		// @formatter:on
	}

	@Test
	public void testGetConversationInstanceNotFoundException()
			throws InstanceNotFoundException, ItsNotYourFriendException, ValidationException, Exception {
		// @formatter:off
		mockMvc.perform(get(UrlConfig.URL_CHAT_GETCONVERSATION_GET)
				.contentType(APPLICATION_JSON_UTF8)
				.requestAttr("userId", USERID_NOTFOUND)
				.param("friendId",  Long.toString(FRIENDID_OK))
				.param("page", Integer.toString(PAGE))
				.param("size", Integer.toString(SIZE)))
		.andExpect(status().isNotFound())
		.andExpect(content().contentType(APPLICATION_JSON_UTF8));
		// @formatter:on
	}

	@Test
	public void testGetConversationValidationException()
			throws InstanceNotFoundException, ItsNotYourFriendException, ValidationException, Exception {
		// @formatter:off
		mockMvc.perform(get(UrlConfig.URL_CHAT_GETCONVERSATION_GET)
				.contentType(APPLICATION_JSON_UTF8)
				.requestAttr("userId", USERID_OK)
				.param("friendId",  Long.toString(USERID_OK))
				.param("page", Integer.toString(PAGE))
				.param("size", Integer.toString(SIZE)))
		.andExpect(status().isBadRequest())
		.andExpect(content().contentType(APPLICATION_JSON_UTF8));
		// @formatter:on
	}

	@Test
	public void testGetConversationItsNotYourFriendException()
			throws InstanceNotFoundException, ItsNotYourFriendException, ValidationException, Exception {
		// @formatter:off
		mockMvc.perform(get(UrlConfig.URL_CHAT_GETCONVERSATION_GET)
				.contentType(APPLICATION_JSON_UTF8)
				.requestAttr("userId", USERID_OK)
				.param("friendId",  Long.toString(USERID2_OK))
				.param("page", Integer.toString(PAGE))
				.param("size", Integer.toString(SIZE)))
		.andExpect(status().isNotFound())
		.andExpect(content().contentType(APPLICATION_JSON_UTF8));
		// @formatter:on
	}

	/****************
	 * TESTS GET FRIENDS HEADER
	 *
	 ***************************************/

	@Test
	public void testGetFriendHeaders()
			throws InstanceNotFoundException, RequestParamException, Exception {
		// @formatter:off
		mockMvc.perform(get(UrlConfig.URL_CHAT_FRIENDHEADERS_GET)
				.contentType(APPLICATION_JSON_UTF8)
				.requestAttr("userId", USERID_OK)
				.param("page", Integer.toString(PAGE)))
		.andExpect(status().isOk())
		.andExpect(content().contentType(APPLICATION_JSON_UTF8))
		.andExpect(jsonPath("$.elements").isEmpty())
		.andExpect(jsonPath("$.existMoreElements").value(false));
		// @formatter:on
	}

	@Test
	public void testGetFriendHeadersInstanceNotFoundException()
			throws InstanceNotFoundException, RequestParamException, Exception {
		// @formatter:off
		mockMvc.perform(get(UrlConfig.URL_CHAT_FRIENDHEADERS_GET)
				.contentType(APPLICATION_JSON_UTF8)
				.requestAttr("userId", USERID_NOTFOUND)
				.param("page", Integer.toString(PAGE)))
		.andExpect(status().isNotFound());
		// @formatter:on
	}

	@Test
	public void testGetFriendHeadersRequestParamException()
			throws InstanceNotFoundException, RequestParamException, Exception {
		// @formatter:off
		mockMvc.perform(get(UrlConfig.URL_CHAT_FRIENDHEADERS_GET)
				.contentType(APPLICATION_JSON_UTF8)
				.requestAttr("userId", USERID_OK)
				.param("page", Integer.toString(PAGE_INVALID)))
		.andExpect(status().isBadRequest());
		// @formatter:on
	}

}
