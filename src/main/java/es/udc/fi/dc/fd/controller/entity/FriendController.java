package es.udc.fi.dc.fd.controller.entity;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Locale;

import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import es.udc.fi.dc.fd.controller.exception.InstanceNotFoundException;
import es.udc.fi.dc.fd.controller.exception.RequestParamException;
import es.udc.fi.dc.fd.dtos.BlockGetFriendListDto;
import es.udc.fi.dc.fd.dtos.ErrorsDto;
import es.udc.fi.dc.fd.dtos.FriendConversor;
import es.udc.fi.dc.fd.dtos.GetFriendListOutDto;
import es.udc.fi.dc.fd.model.persistence.UserImpl;
import es.udc.fi.dc.fd.service.BlockFriendList;
import es.udc.fi.dc.fd.service.FriendService;

@RestController
@RequestMapping("/backend/friends")
public class FriendController {

	private final static String INSTANCE_NOT_FOUND_EXCEPTION_CODE = "project.exceptions.InstanceNotFoundException";
	private static final String REQUEST_PARAM_EXCEPTION_CODE = "project.exceptions.RequestParamException";

	private final FriendService friendService;

	private final MessageSource messageSource;

	@Autowired
	public FriendController(final FriendService friendService, final MessageSource messageSource) {
		super();

		this.friendService = checkNotNull(friendService, "Received a null pointer as friendService in UserController");

		this.messageSource = checkNotNull(messageSource, "Received a null pointer as messageSource in UserController");
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

	@ExceptionHandler(RequestParamException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorsDto handleRequestParamException(RequestParamException exception, Locale locale) {
		final String errorMessage = messageSource.getMessage(REQUEST_PARAM_EXCEPTION_CODE, null,
				REQUEST_PARAM_EXCEPTION_CODE, locale);

		return new ErrorsDto(errorMessage);
	}

	@GetMapping("/friendList")
	public BlockGetFriendListDto<GetFriendListOutDto> getFriendList(@RequestAttribute Long userId,
			@RequestParam(defaultValue = "0") @Min(0) int page, @RequestParam(defaultValue = "10") @Min(1) int size)
					throws InstanceNotFoundException, RequestParamException {
		final BlockFriendList<UserImpl> friends = friendService.getFriendList(userId, page, size);

		return FriendConversor.toGetFriendListOutDto(friends);
	}

}
