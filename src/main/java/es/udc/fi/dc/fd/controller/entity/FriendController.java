package es.udc.fi.dc.fd.controller.entity;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import es.udc.fi.dc.fd.controller.exception.AlreadyRejectedException;
import es.udc.fi.dc.fd.controller.exception.InstanceNotFoundException;
import es.udc.fi.dc.fd.controller.exception.InvalidRecommendationException;
import es.udc.fi.dc.fd.controller.exception.NoMoreSuggestionFound;
import es.udc.fi.dc.fd.dtos.ErrorsDto;
import es.udc.fi.dc.fd.dtos.FriendDto;
import es.udc.fi.dc.fd.dtos.IdDto;
import es.udc.fi.dc.fd.service.FriendService;

@RestController
@RequestMapping("/backend/friends")

public class FriendController {

	private final static String ALREADY_REJECTED_EXCEPTION = "project.exceptions.AlreadyRejectedException";
	private final static String INSTANCE_NOT_FOUND_EXCEPTION_CODE = "project.exceptions.InstanceNotFoundException";
	private final static String INVALID_RECOMMENDATION_EXCEPTION = "project.exceptions.InvalidRecommendationException";
	private final static String NO_MORE_SUGGESTION_FOUND = "project.exceptions.NoMoreSuggestionFound";

	@Autowired
	private final MessageSource messageSource;

	@Autowired
	private final FriendService friendService;

	public FriendController(final FriendService friendService, final MessageSource messageSource) {
		super();

		this.friendService = checkNotNull(friendService,
				"Received a null pointer as friendService in FriendController");

		this.messageSource = checkNotNull(messageSource,
				"Received a null pointer as messageSource in FriendController");
	}

	@ExceptionHandler(InvalidRecommendationException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public ErrorsDto handleInvalidRecommendationException(InvalidRecommendationException exception, Locale locale) {

		final String nameMessage = messageSource.getMessage(exception.getName(), null, exception.getName(), locale);
		final String errorMessage = messageSource.getMessage(INVALID_RECOMMENDATION_EXCEPTION,
				new Object[] { nameMessage, exception.getKey().toString() }, INVALID_RECOMMENDATION_EXCEPTION, locale);

		return new ErrorsDto(errorMessage);

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

	@ExceptionHandler(AlreadyRejectedException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorsDto handleAlreadyRejectedException(AlreadyRejectedException exception, Locale locale) {

		final String nameMessage = messageSource.getMessage(exception.getName(), null, exception.getName(), locale);
		final String errorMessage = messageSource.getMessage(ALREADY_REJECTED_EXCEPTION,
				new Object[] { nameMessage, exception.getKey().toString() }, ALREADY_REJECTED_EXCEPTION, locale);

		return new ErrorsDto(errorMessage);

	}

	@ExceptionHandler(NoMoreSuggestionFound.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorsDto handleNotSuggestionsFound(NoMoreSuggestionFound exception, Locale locale) {

		final String nameMessage = messageSource.getMessage(exception.getName(), null, exception.getName(), locale);
		final String errorMessage = messageSource.getMessage(NO_MORE_SUGGESTION_FOUND,
				new Object[] { nameMessage, exception.getKey().toString() }, ALREADY_REJECTED_EXCEPTION, locale);

		return new ErrorsDto(errorMessage);

	}

	@ResponseStatus(value = HttpStatus.OK)
	@PostMapping("/accept")
	public void acceptRequest(@RequestAttribute Long userId, @Validated @RequestBody IdDto params)
			throws InstanceNotFoundException, InvalidRecommendationException, AlreadyRejectedException {

		final long objectId = params.getId();
		friendService.acceptRecommendation(userId, objectId);
	}

	@ResponseStatus(value = HttpStatus.OK)
	@PostMapping("/reject")
	public void rejectRequest(@RequestAttribute Long userId, @Validated @RequestBody IdDto params)
			throws InstanceNotFoundException, InvalidRecommendationException, AlreadyRejectedException {

		final long objectId = params.getId();
		friendService.rejectRecommendation(userId, objectId);
	}

	@ResponseStatus(value = HttpStatus.OK)
	@GetMapping("/suggestion")
	public FriendDto suggestFriend(@RequestAttribute Long userId)
			throws InstanceNotFoundException, NoMoreSuggestionFound {

		final Optional<FriendDto> friend = friendService.suggestFriend(userId);

		if (friend.isEmpty()) {
			throw new NoMoreSuggestionFound("There are no more users suggested with the current criteria", null);
		}
		return friend.get();
	}

}
