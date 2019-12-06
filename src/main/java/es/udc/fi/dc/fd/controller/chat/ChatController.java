package es.udc.fi.dc.fd.controller.chat;

import static com.google.common.base.Preconditions.checkNotNull;

import java.security.Principal;
import java.util.Locale;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import es.udc.fi.dc.fd.controller.exception.InstanceNotFoundException;
import es.udc.fi.dc.fd.controller.exception.ItsNotYourFriendException;
import es.udc.fi.dc.fd.controller.exception.RequestParamException;
import es.udc.fi.dc.fd.controller.exception.ValidationException;
import es.udc.fi.dc.fd.dtos.BlockDto;
import es.udc.fi.dc.fd.dtos.ErrorsDto;
import es.udc.fi.dc.fd.dtos.FriendHeaderDto;
import es.udc.fi.dc.fd.dtos.MessageConversor;
import es.udc.fi.dc.fd.dtos.MessageDetailsDto;
import es.udc.fi.dc.fd.jwt.JwtInfo;
import es.udc.fi.dc.fd.model.persistence.UserImpl;
import es.udc.fi.dc.fd.service.Block;
import es.udc.fi.dc.fd.service.ChatService;
import es.udc.fi.dc.fd.service.UserService;

@Controller
public class ChatController {
	private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

	private static final String INSTANCE_NOT_FOUND_EXCEPTION_CODE = "project.exceptions.InstanceNotFoundException";
	private static final String ITS_NOT_YOUR_FRIEND_CODE = "project.exceptions.ItsNotYourFriendException";
	private static final String VALIDATION_EXCEPTION_CODE = "project.exceptions.ValidationException";
	private static final String REQUEST_PARAM_EXCEPTION_CODE = "project.exceptions.RequestParamException";

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@Autowired
	private final UserService userService;

	@Autowired
	private final ChatService chatService;

	@Autowired
	private final MessageSource messageSource;

	public ChatController(final UserService userService, final ChatService chatService,
			final MessageSource messageSource) {
		super();

		this.userService = checkNotNull(userService, "Received a null pointer as userService in ChatController");
		this.chatService = checkNotNull(chatService, "Received a null pointer as chatService in ChatController");
		this.messageSource = checkNotNull(messageSource, "Received a null pointer as messageSource in ChatController");
	}

	@ExceptionHandler(InstanceNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public ErrorsDto handleInstanceNotFoundException(InstanceNotFoundException exception, Locale locale) {

		final String nameMessage = messageSource.getMessage(exception.getName(), null, exception.getName(), locale);
		final String errorMessage = messageSource.getMessage(INSTANCE_NOT_FOUND_EXCEPTION_CODE,
				new Object[] { nameMessage, exception.getKey().toString() }, INSTANCE_NOT_FOUND_EXCEPTION_CODE, locale);

		return new ErrorsDto(errorMessage);

	}

	@ExceptionHandler(ItsNotYourFriendException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public ErrorsDto handleItsNotYourFriendException(ItsNotYourFriendException exception, Locale locale) {
		final String errorMessage = messageSource.getMessage(ITS_NOT_YOUR_FRIEND_CODE, null, ITS_NOT_YOUR_FRIEND_CODE,
				locale);

		return new ErrorsDto(errorMessage);
	}

	@ExceptionHandler(ValidationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorsDto handleItsNotYourFriendException(ValidationException exception, Locale locale) {
		final String errorMessage = messageSource.getMessage(VALIDATION_EXCEPTION_CODE, null, VALIDATION_EXCEPTION_CODE,
				locale);

		return new ErrorsDto(errorMessage);
	}

	@ExceptionHandler(RequestParamException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorsDto handleRequestParamException(RequestParamException exception, Locale locale) {
		final String errorMessage = messageSource.getMessage(REQUEST_PARAM_EXCEPTION_CODE, null,
				REQUEST_PARAM_EXCEPTION_CODE, locale);

		return new ErrorsDto(errorMessage);
	}

	@MessageMapping("/chat.sendMessage")
	public void sendMessage(@Payload ChatMessage chatMessage, Principal user) {
		final JwtInfo ownerUser = ((JwtInfo) user);

		try {
			final UserImpl receiver = userService.loginFromUserId(chatMessage.getReceiverId());
			chatMessage.setSenderId(ownerUser.getUserId());
			chatService.sendMessage(ownerUser.getUserId(), chatMessage.getReceiverId(), chatMessage.getContent());
			messagingTemplate.convertAndSendToUser(receiver.getUserName(), "/queue/reply", chatMessage);

		} catch (InstanceNotFoundException | ItsNotYourFriendException | ValidationException e) {
			logger.info("Illegal access to chat");
			final ChatMessage chatMessage2 = new ChatMessage();
			chatMessage2.setType(MessageType.ERROR);
			chatMessage2.setSenderId(ownerUser.getUserId());
			chatMessage2.setContent("Tried to send a message to an invalid person");
			messagingTemplate.convertAndSendToUser(ownerUser.getName(), "/queue/reply", chatMessage2);
			return;
		}
	}

	@GetMapping("/backend/chat/friendHeaders")
	@ResponseBody
	public BlockDto<FriendHeaderDto> getFriendHeaders(@RequestAttribute Long userId, @RequestParam Integer page)
			throws InstanceNotFoundException, RequestParamException {
		return MessageConversor.toFriendHeadersDto(chatService.getUserConversations(userId, page));
	}

	// @formatter:off
	@GetMapping("/backend/chat/conversation")
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public Block<MessageDetailsDto> getConversation(@RequestAttribute Long userId,
			@RequestParam @Min(1) @NotNull Long friendId, @RequestParam(defaultValue = "0") @Min(0) int page,
			@RequestParam(defaultValue = "10") @Min(1) int size)
			throws InstanceNotFoundException, ItsNotYourFriendException, ValidationException {
		// @formatter:on

		return chatService.getConversation(userId, friendId, page, size);
	}
}