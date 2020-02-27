package es.udc.fi.dc.fd.controller.entity;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Locale;
import java.util.Optional;

import javax.validation.constraints.Min;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import es.udc.fi.dc.fd.controller.exception.AlreadyAceptedException;
import es.udc.fi.dc.fd.controller.exception.AlreadyBlockedException;
import es.udc.fi.dc.fd.controller.exception.AlreadyRejectedException;
import es.udc.fi.dc.fd.controller.exception.CantFindMoreFriendsException;
import es.udc.fi.dc.fd.controller.exception.InstanceNotFoundException;
import es.udc.fi.dc.fd.controller.exception.InvalidRecommendationException;
import es.udc.fi.dc.fd.controller.exception.ItsNotYourFriendException;
import es.udc.fi.dc.fd.controller.exception.NoMoreSuggestionFound;
import es.udc.fi.dc.fd.controller.exception.RequestParamException;
import es.udc.fi.dc.fd.dtos.BlockDto;
import es.udc.fi.dc.fd.dtos.ErrorsDto;
import es.udc.fi.dc.fd.dtos.FriendConversor;
import es.udc.fi.dc.fd.dtos.FullUserProfileDto;
import es.udc.fi.dc.fd.dtos.IdDto;
import es.udc.fi.dc.fd.dtos.RatedFriendDto;
import es.udc.fi.dc.fd.dtos.SearchUsersDto;
import es.udc.fi.dc.fd.dtos.UnratedFriendDto;
import es.udc.fi.dc.fd.model.persistence.FriendListOut;
import es.udc.fi.dc.fd.model.persistence.SuggestedSearchCriteria;
import es.udc.fi.dc.fd.model.persistence.UserImpl;
import es.udc.fi.dc.fd.service.Block;
import es.udc.fi.dc.fd.service.FriendService;

@RestController
@RequestMapping("/friends")

public class FriendController {

	private final static String ALREADY_REJECTED_EXCEPTION = "project.exceptions.AlreadyRejectedException";
	private final static String ALREADY_ACEPTED_EXCEPTION = "project.exceptions.AlreadyAceptedException";
	private final static String INSTANCE_NOT_FOUND_EXCEPTION_CODE = "project.exceptions.InstanceNotFoundException";
	private final static String INVALID_RECOMMENDATION_EXCEPTION = "project.exceptions.InvalidRecommendationException";
	private final static String NO_MORE_SUGGESTION_FOUND = "project.exceptions.NoMoreSuggestionFound";
	private static final String REQUEST_PARAM_EXCEPTION_CODE = "project.exceptions.RequestParamException";
	private static final String ITS_NOT_YOUR_FRIEND_CODE = "project.exceptions.ItsNotYourFriendException";
	private static final String ALREADY_BLOCKED_CODE = "project.exceptions.AlreadyBlockedException";
	private static final String CANT_FIND_MORE_CODE = "project.exceptions.CantFindMoreFriendsException";

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

	@ExceptionHandler(RequestParamException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorsDto handleRequestParamException(RequestParamException exception, Locale locale) {
		final String errorMessage = messageSource.getMessage(REQUEST_PARAM_EXCEPTION_CODE, null,
			REQUEST_PARAM_EXCEPTION_CODE, locale);

		return new ErrorsDto(errorMessage);
	}

	@ExceptionHandler(InvalidRecommendationException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public ErrorsDto handleInvalidRecommendationException(InvalidRecommendationException exception, Locale locale) {

		final String nameMessage = messageSource.getMessage(exception.getName(), null, exception.getName(), locale);
		final String errorMessage = messageSource.getMessage(INVALID_RECOMMENDATION_EXCEPTION,
			new Object[] {nameMessage, exception.getKey().toString()}, INVALID_RECOMMENDATION_EXCEPTION, locale);

		return new ErrorsDto(errorMessage);

	}

	@ExceptionHandler(InstanceNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public ErrorsDto handleInstanceNotFoundException(InstanceNotFoundException exception, Locale locale) {

		final String nameMessage = messageSource.getMessage(exception.getName(), null, exception.getName(), locale);
		final String errorMessage = messageSource.getMessage(INSTANCE_NOT_FOUND_EXCEPTION_CODE,
			new Object[] {nameMessage, exception.getKey().toString()}, INSTANCE_NOT_FOUND_EXCEPTION_CODE, locale);

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

	@ExceptionHandler(AlreadyRejectedException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorsDto handleAlreadyRejectedException(AlreadyRejectedException exception, Locale locale) {

		final String nameMessage = messageSource.getMessage(exception.getName(), null, exception.getName(), locale);
		final String errorMessage = messageSource.getMessage(ALREADY_REJECTED_EXCEPTION,
			new Object[] {nameMessage, exception.getKey().toString()}, ALREADY_REJECTED_EXCEPTION, locale);

		return new ErrorsDto(errorMessage);

	}

	@ExceptionHandler(AlreadyAceptedException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorsDto handleAlreadyAveptedException(AlreadyAceptedException exception, Locale locale) {

		final String nameMessage = messageSource.getMessage(exception.getName(), null, exception.getName(), locale);
		final String errorMessage = messageSource.getMessage(ALREADY_ACEPTED_EXCEPTION,
			new Object[] {nameMessage, exception.getKey().toString()}, ALREADY_ACEPTED_EXCEPTION, locale);

		return new ErrorsDto(errorMessage);

	}

	@ExceptionHandler(NoMoreSuggestionFound.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorsDto handleNotSuggestionsFound(NoMoreSuggestionFound exception, Locale locale) {

		final String nameMessage = messageSource.getMessage(exception.getName(), null, exception.getName(), locale);
		final String errorMessage = messageSource.getMessage(NO_MORE_SUGGESTION_FOUND,
			new Object[] {nameMessage, exception.getKey().toString()}, NO_MORE_SUGGESTION_FOUND, locale);

		return new ErrorsDto(errorMessage);

	}

	@ExceptionHandler(AlreadyBlockedException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ResponseBody
	public ErrorsDto handleAlreadyBlockedException(AlreadyBlockedException exception, Locale locale) {
		final String errorMessage = messageSource.getMessage(ALREADY_BLOCKED_CODE, null, ALREADY_BLOCKED_CODE, locale);

		return new ErrorsDto(errorMessage);
	}

	@ExceptionHandler(CantFindMoreFriendsException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorsDto handleCantFindMoreFriendsException(CantFindMoreFriendsException exception, Locale locale) {
		final String errorMessage = messageSource.getMessage(CANT_FIND_MORE_CODE, null, CANT_FIND_MORE_CODE, locale);

		return new ErrorsDto(errorMessage);
	}

	@ResponseStatus(value = HttpStatus.OK)
	@PostMapping("/accept")
	public void acceptRequest(@RequestAttribute Long userId, @Validated @RequestBody IdDto params)
		throws InstanceNotFoundException, InvalidRecommendationException, AlreadyRejectedException,
		AlreadyAceptedException {

		final long objectId = params.getId();
		friendService.acceptRecommendation(userId, objectId);
	}

	@ResponseStatus(value = HttpStatus.OK)
	@PostMapping("/reject")
	public void rejectRequest(@RequestAttribute Long userId, @Validated @RequestBody IdDto params)
		throws InstanceNotFoundException, InvalidRecommendationException, AlreadyRejectedException,
		AlreadyAceptedException {

		final long objectId = params.getId();
		friendService.rejectRecommendation(userId, objectId);
	}

	@ResponseStatus(value = HttpStatus.OK)
	@GetMapping("/suggestion")
	public UnratedFriendDto suggestFriend(@RequestAttribute Long userId)
		throws InstanceNotFoundException, NoMoreSuggestionFound {

		final Optional<UserImpl> friend = friendService.suggestFriend(userId);

		if (friend.isEmpty()) {
			throw new NoMoreSuggestionFound("There are no more users suggested with the current criteria", userId);
		}
		return FriendConversor.fromUserImpl(friend.get());
	}

	@GetMapping("/friendList")
	public BlockDto<RatedFriendDto> getFriendList(@RequestAttribute Long userId,
												  @RequestParam(defaultValue = "0") @Min(0) int page, @RequestParam(defaultValue = "10") @Min(1) int size)
		throws InstanceNotFoundException, RequestParamException {
		final Block<FriendListOut> friends = friendService.getFriendList(userId, page, size);

		return FriendConversor.toGetFriendListOutDto(friends);
	}

	@PostMapping("/block")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void blockUser(@RequestAttribute Long userId, @Validated @RequestBody IdDto params)
		throws InstanceNotFoundException, ItsNotYourFriendException, AlreadyBlockedException {
		friendService.blockUser(userId, params.getId());
	}

	@ResponseStatus(value = HttpStatus.OK)
	@GetMapping("/suggestNewCriteria")
	public SuggestedSearchCriteria suggestNewCriteria(@RequestAttribute Long userId)
		throws InstanceNotFoundException, CantFindMoreFriendsException {
		return friendService.suggestNewCriteria(userId);
	}

	@GetMapping("/searchUsers")
	public BlockDto<FullUserProfileDto> searchUsersByMetadataAndKeywords(@RequestBody @Validated SearchUsersDto params,
																		 @RequestParam(defaultValue = "0") @Min(0) int page,
																		 @RequestParam(defaultValue = "10") @Min(1) int size) {
		final Block<UserImpl> users = friendService.searchUsersByMetadataAndKeywords(params, page, size);

		return FriendConversor.toFullUserProfileDto(users);
	}

}
