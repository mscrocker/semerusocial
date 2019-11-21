package es.udc.fi.dc.fd.controller.entity;

import static com.google.common.base.Preconditions.checkNotNull;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import es.udc.fi.dc.fd.controller.exception.DuplicateInstanceException;
import es.udc.fi.dc.fd.controller.exception.IncorrectLoginException;
import es.udc.fi.dc.fd.controller.exception.InstanceNotFoundException;
import es.udc.fi.dc.fd.controller.exception.InvalidAgeException;
import es.udc.fi.dc.fd.controller.exception.InvalidDateException;
import es.udc.fi.dc.fd.controller.exception.InvalidRateException;
import es.udc.fi.dc.fd.controller.exception.ItsNotYourFriendException;
import es.udc.fi.dc.fd.controller.exception.NotRatedException;
import es.udc.fi.dc.fd.dtos.ErrorsDto;
import es.udc.fi.dc.fd.dtos.FieldErrorDto;
import es.udc.fi.dc.fd.dtos.LoginParamsDto;
import es.udc.fi.dc.fd.dtos.RateDto;
import es.udc.fi.dc.fd.dtos.RegisterParamsDto;
import es.udc.fi.dc.fd.dtos.SearchCriteriaConversor;
import es.udc.fi.dc.fd.dtos.SearchCriteriaDto;
import es.udc.fi.dc.fd.dtos.UpdateProfileInDto;
import es.udc.fi.dc.fd.dtos.UserAuthenticatedDto;
import es.udc.fi.dc.fd.dtos.UserConversor;
import es.udc.fi.dc.fd.dtos.UserDataDto;
import es.udc.fi.dc.fd.jwt.JwtGenerator;
import es.udc.fi.dc.fd.jwt.JwtGeneratorImpl;
import es.udc.fi.dc.fd.jwt.JwtInfo;
import es.udc.fi.dc.fd.model.persistence.SearchCriteria;
import es.udc.fi.dc.fd.model.persistence.UserImpl;
import es.udc.fi.dc.fd.service.UserService;

@RestController
@RequestMapping("/backend/users")
public class UserController {

	private final static String DUPLICATE_INSTANCE_EXCEPTION_CODE = "project.exceptions.DuplicateInstanceException";
	private final static String INCORRECT_LOGIN_EXCEPTION_CODE = "project.exceptions.IncorrectLoginException";
	private final static String INSTANCE_NOT_FOUND_EXCEPTION_CODE = "project.exceptions.InstanceNotFoundException";
	private final static String INVALID_DATE_EXCEPTION_CODE = "project.exceptions.InvalidDateException";
	private final static String INVALID_AGE_EXCEPTION_CODE = "project.exceptions.InvalidAgeException";
	private final static String INVALID_RATE_EXCEPTION_CODE = "project.exceptions.InvalidRateException";
	private final static String INVALID_ITS_NOT_YOUR_FRIEND_EXCEPTION_CODE = "project.exceptions.ItsNotYourFriendException";

	private final JwtGenerator jwtGenerator = JwtGenerator();

	private final MessageSource messageSource;

	private final UserService userService;

	@Bean
	JwtGenerator JwtGenerator() {
		return new JwtGeneratorImpl();
	}

	@Autowired
	public UserController(final UserService userService, final MessageSource messageSource) {
		super();

		this.userService = checkNotNull(userService, "Received a null pointer as userService in UserController");

		this.messageSource = checkNotNull(messageSource, "Received a null pointer as messageSource in UserController");
	}

	private String generateServiceToken(UserImpl user) {
		final JwtInfo jwtInfo = new JwtInfo(user.getId(), user.getUserName());

		return jwtGenerator.generate(jwtInfo);
	}

	@ExceptionHandler(DuplicateInstanceException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorsDto handleDuplicateInstanceException(DuplicateInstanceException exception, Locale locale) {
		final String nameMessage = messageSource.getMessage(exception.getName(), null, exception.getName(), locale);
		final String errorMessage = messageSource.getMessage(DUPLICATE_INSTANCE_EXCEPTION_CODE,
				new Object[] { nameMessage, exception.getKey().toString() }, DUPLICATE_INSTANCE_EXCEPTION_CODE, locale);

		return new ErrorsDto(errorMessage);
	}

	@ExceptionHandler(IncorrectLoginException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public ErrorsDto handleIncorrectLoginException(IncorrectLoginException exception, Locale locale) {
		final String errorMessage = messageSource.getMessage(INCORRECT_LOGIN_EXCEPTION_CODE, null,
				INCORRECT_LOGIN_EXCEPTION_CODE, locale);

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

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorsDto handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {

		final List<FieldErrorDto> fieldErrors = exception.getBindingResult().getFieldErrors().stream()
				.map(error -> new FieldErrorDto(error.getField(), error.getDefaultMessage()))
				.collect(Collectors.toList());

		return new ErrorsDto(fieldErrors);

	}

	@ExceptionHandler(InvalidRateException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorsDto handleInvalidRateException(InvalidRateException exception, Locale locale) {
		final String errorMessage = messageSource.getMessage(INVALID_RATE_EXCEPTION_CODE, null,
				INVALID_RATE_EXCEPTION_CODE, locale);

		return new ErrorsDto(errorMessage);
	}

	@ExceptionHandler(ItsNotYourFriendException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorsDto handleItsNotYourFriendException(ItsNotYourFriendException exception, Locale locale) {
		final String errorMessage = messageSource.getMessage(INVALID_ITS_NOT_YOUR_FRIEND_EXCEPTION_CODE, null,
				INVALID_ITS_NOT_YOUR_FRIEND_EXCEPTION_CODE, locale);

		return new ErrorsDto(errorMessage);
	}

	@ExceptionHandler(InvalidDateException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorsDto handleInvalidDateException(InvalidDateException exception, Locale locale) {
		final String errorMessage = messageSource.getMessage(INVALID_DATE_EXCEPTION_CODE, null,
				INVALID_DATE_EXCEPTION_CODE, locale);

		return new ErrorsDto(errorMessage);
	}

	@ExceptionHandler(InvalidAgeException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorsDto handleInvalidAgeException(InvalidAgeException exception, Locale locale) {
		final String errorMessage = messageSource.getMessage(INVALID_AGE_EXCEPTION_CODE, null,
				INVALID_AGE_EXCEPTION_CODE, locale);

		return new ErrorsDto(errorMessage);
	}

	@PostMapping("/signUp")
	public ResponseEntity<UserAuthenticatedDto> signUp(@Validated @RequestBody RegisterParamsDto params)
			throws DuplicateInstanceException, InvalidDateException {

		final UserImpl user = (UserImpl) UserConversor.fromRegisterDto(params);
		userService.signUp(user);

		final UserAuthenticatedDto userAuthenticated = new UserAuthenticatedDto(user.getUserName(),
				generateServiceToken(user));

		final URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId())
				.toUri();

		return ResponseEntity.created(location).body(userAuthenticated);
	}

	@PutMapping("/searchCriteria")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void setSearchCriteria(@RequestAttribute Long userId, @Validated @RequestBody SearchCriteriaDto criteria)
			throws InstanceNotFoundException, InvalidAgeException, InvalidRateException, NotRatedException {
		final SearchCriteria searchCriteria = new SearchCriteria(criteria.getSex(), criteria.getMinAge(),
				criteria.getMaxAge(), criteria.getCity(), criteria.getMinRate());
		userService.setSearchCriteria(userId, searchCriteria);
	}

	@GetMapping("/searchCriteria")
	public SearchCriteriaDto getSearchCriteria(@RequestAttribute Long userId)
			throws InstanceNotFoundException {
		final SearchCriteria criteria = userService.getSearchCriteria(userId);

		return SearchCriteriaConversor.toSearchCriteriaDto(criteria);
	}

	@PostMapping("/login")
	public UserAuthenticatedDto login(@Validated @RequestBody LoginParamsDto params) throws IncorrectLoginException {

		final UserImpl user = userService.login(params);

		return new UserAuthenticatedDto(params.getUserName(), generateServiceToken(user));
	}

	@GetMapping("/data")
	public UserDataDto getUserData(@RequestAttribute Long userId) throws InstanceNotFoundException {
		final UserImpl user = userService.loginFromUserId(userId);
		final LocalDateTime today = LocalDateTime.now();
		final Period period = Period.between(user.getDate().toLocalDate(), today.toLocalDate());

		return new UserDataDto(user.getDate(), period.getYears(), user.getSex(), user.getCity(), user.getDescription(),
				user.getRating());
	}

	@PutMapping("/updateProfile")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void updateProfile(@RequestAttribute Long userId,
			@Validated @RequestBody UpdateProfileInDto updateProfileInDto)
					throws InstanceNotFoundException, InvalidDateException {
		userService.updateProfile(userId, UserConversor.toUserImpl(updateProfileInDto));
	}

	@PostMapping("/rate")
	public double rate(@RequestAttribute Long userId, @Validated @RequestBody RateDto rateDto)
			throws InstanceNotFoundException, InvalidRateException, ItsNotYourFriendException {

		return userService.rateUser(rateDto.getRate(), userId, rateDto.getUserObject());

	}

	@PutMapping("/premium")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void updatePremium(@RequestAttribute Long userId, @NotNull boolean premium)
			throws InstanceNotFoundException {

		userService.updatePremium(userId, premium);

	}

}
