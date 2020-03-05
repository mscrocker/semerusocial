package es.udc.fi.dc.fd.controller.entity;

import static com.google.common.base.Preconditions.checkNotNull;

import es.udc.fi.dc.fd.controller.exception.DuplicateInstanceException;
import es.udc.fi.dc.fd.controller.exception.IncorrectLoginException;
import es.udc.fi.dc.fd.controller.exception.InstanceNotFoundException;
import es.udc.fi.dc.fd.controller.exception.InvalidAgeException;
import es.udc.fi.dc.fd.controller.exception.InvalidDateException;
import es.udc.fi.dc.fd.controller.exception.InvalidRateException;
import es.udc.fi.dc.fd.controller.exception.ItsNotYourFriendException;
import es.udc.fi.dc.fd.controller.exception.NotRatedException;
import es.udc.fi.dc.fd.dtos.AgelessUserProfileDto;
import es.udc.fi.dc.fd.dtos.BlockDto;
import es.udc.fi.dc.fd.dtos.DateUserProfileDto;
import es.udc.fi.dc.fd.dtos.ErrorsDto;
import es.udc.fi.dc.fd.dtos.FieldErrorDto;
import es.udc.fi.dc.fd.dtos.FullUserProfileDto;
import es.udc.fi.dc.fd.dtos.LoginParamsDto;
import es.udc.fi.dc.fd.dtos.PremiumFormDto;
import es.udc.fi.dc.fd.dtos.RateDto;
import es.udc.fi.dc.fd.dtos.RegisterParamsDto;
import es.udc.fi.dc.fd.dtos.SearchCriteriaConversor;
import es.udc.fi.dc.fd.dtos.SearchCriteriaDto;
import es.udc.fi.dc.fd.dtos.UserAuthenticatedDto;
import es.udc.fi.dc.fd.dtos.UserConversor;
import es.udc.fi.dc.fd.jwt.JwtGenerator;
import es.udc.fi.dc.fd.jwt.JwtGeneratorImpl;
import es.udc.fi.dc.fd.jwt.JwtInfo;
import es.udc.fi.dc.fd.model.persistence.SearchCriteria;
import es.udc.fi.dc.fd.model.persistence.UserImpl;
import es.udc.fi.dc.fd.service.Block;
import es.udc.fi.dc.fd.service.UserService;
import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import javax.validation.constraints.Min;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/users")
public class UserController {

  private static final String DUPLICATE_INSTANCE_EXCEPTION_CODE =
      "project.exceptions.DuplicateInstanceException";

  private static final String INCORRECT_LOGIN_EXCEPTION_CODE =
      "project.exceptions.IncorrectLoginException";

  private static final String INSTANCE_NOT_FOUND_EXCEPTION_CODE =
      "project.exceptions.InstanceNotFoundException";

  private static final String INVALID_DATE_EXCEPTION_CODE =
      "project.exceptions.InvalidDateException";

  private static final String INVALID_AGE_EXCEPTION_CODE =
      "project.exceptions.InvalidAgeException";

  private static final String INVALID_RATE_EXCEPTION_CODE =
      "project.exceptions.InvalidRateException";

  private static final String INVALID_ITS_NOT_YOUR_FRIEND_EXCEPTION_CODE =
      "project.exceptions.ItsNotYourFriendException";

  private final JwtGenerator jwtGenerator = jwtGenerator();

  private final MessageSource messageSource;

  private final UserService userService;

  @Bean
  JwtGenerator jwtGenerator() {
    return new JwtGeneratorImpl();
  }

  /**
   * Default constructor for the REST controller of the user service.
   * @param userService The user service reference
   * @param messageSource The message source that will be used for localization purposes
   */
  @Autowired
  public UserController(final UserService userService, final MessageSource messageSource) {
    super();

    this.userService = checkNotNull(userService,
        "Received a null pointer as userService in UserController");

    this.messageSource = checkNotNull(messageSource,
        "Received a null pointer as messageSource in UserController");
  }

  /**
   * Generates a new token for a given user.
   * @param user The user
   * @return The token
   */
  private String generateServiceToken(UserImpl user) {
    final JwtInfo jwtInfo = new JwtInfo(user.getId(), user.getUserName());

    return jwtGenerator.generate(jwtInfo);
  }

  /**
   * Handler for the DuplicateInstanceException.
   * @param exception The instance of the exception to handle
   * @param locale The local to use for the error messages
   * @return The ErrorsDto with the error messages
   */
  @ExceptionHandler(DuplicateInstanceException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ErrorsDto handleDuplicateInstanceException(DuplicateInstanceException exception,
                                                    Locale locale) {
    final String nameMessage = messageSource.getMessage(exception.getName(), null,
        exception.getName(), locale);
    final String errorMessage = messageSource.getMessage(DUPLICATE_INSTANCE_EXCEPTION_CODE,
        new Object[] {nameMessage, exception.getKey().toString()},
        DUPLICATE_INSTANCE_EXCEPTION_CODE, locale);

    return new ErrorsDto(errorMessage);
  }

  /**
   * Handler for the IncorrectLoginException.
   * @param exception The instance of the exception to handle
   * @param locale The local to use for the error messages
   * @return The ErrorsDto with the error messages
   */
  @ExceptionHandler(IncorrectLoginException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ResponseBody
  public ErrorsDto handleIncorrectLoginException(IncorrectLoginException exception, Locale locale) {
    final String errorMessage = messageSource.getMessage(INCORRECT_LOGIN_EXCEPTION_CODE, null,
        INCORRECT_LOGIN_EXCEPTION_CODE, locale);

    return new ErrorsDto(errorMessage);
  }

  /**
   * Handler for the InstanceNotFoundException.
   * @param exception The instance of the exception to handle
   * @param locale The local to use for the error messages
   * @return The ErrorsDto with the error messages
   */
  @ExceptionHandler(InstanceNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ResponseBody
  public ErrorsDto handleInstanceNotFoundException(InstanceNotFoundException exception,
                                                   Locale locale) {

    final String nameMessage = messageSource.getMessage(exception.getName(), null,
        exception.getName(), locale);
    final String errorMessage = messageSource.getMessage(INSTANCE_NOT_FOUND_EXCEPTION_CODE,
        new Object[] {nameMessage, exception.getKey().toString()},
        INSTANCE_NOT_FOUND_EXCEPTION_CODE, locale);

    return new ErrorsDto(errorMessage);

  }

  /**
   * Handler for the MethodArgumentNotValidException.
   * @param exception The instance of the exception to handle
   * @return The ErrorsDto with the error messages
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ErrorsDto handleMethodArgumentNotValidException(
      MethodArgumentNotValidException exception) {

    final List<FieldErrorDto> fieldErrors = exception.getBindingResult().getFieldErrors().stream()
        .map(error -> new FieldErrorDto(error.getField(), error.getDefaultMessage()))
        .collect(Collectors.toList());

    return new ErrorsDto(fieldErrors);

  }

  /**
   * Handler for the InvalidRateException.
   * @param exception The instance of the exception to handle
   * @param locale The local to use for the error messages
   * @return The ErrorsDto with the error messages
   */
  @ExceptionHandler(InvalidRateException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ErrorsDto handleInvalidRateException(InvalidRateException exception, Locale locale) {
    final String errorMessage = messageSource.getMessage(INVALID_RATE_EXCEPTION_CODE, null,
        INVALID_RATE_EXCEPTION_CODE, locale);

    return new ErrorsDto(errorMessage);
  }

  /**
   * Handler for the ItsNotYourFriendException.
   * @param exception The instance of the exception to handle
   * @param locale The local to use for the error messages
   * @return The ErrorsDto with the error messages
   */
  @ExceptionHandler(ItsNotYourFriendException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ErrorsDto handleItsNotYourFriendException(ItsNotYourFriendException exception,
                                                   Locale locale) {
    final String errorMessage = messageSource.getMessage(INVALID_ITS_NOT_YOUR_FRIEND_EXCEPTION_CODE,
        null, INVALID_ITS_NOT_YOUR_FRIEND_EXCEPTION_CODE, locale);

    return new ErrorsDto(errorMessage);
  }

  /**
   * Handler for the InvalidDateException.
   * @param exception The instance of the exception to handle
   * @param locale The local to use for the error messages
   * @return The ErrorsDto with the error messages
   */
  @ExceptionHandler(InvalidDateException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ErrorsDto handleInvalidDateException(InvalidDateException exception, Locale locale) {
    final String errorMessage = messageSource.getMessage(INVALID_DATE_EXCEPTION_CODE, null,
        INVALID_DATE_EXCEPTION_CODE, locale);

    return new ErrorsDto(errorMessage);
  }

  /**
   * Handler for the InvalidAgeException.
   * @param exception The instance of the exception to handle
   * @param locale The local to use for the error messages
   * @return The ErrorsDto with the error messages
   */
  @ExceptionHandler(InvalidAgeException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ErrorsDto handleInvalidAgeException(InvalidAgeException exception, Locale locale) {
    final String errorMessage = messageSource.getMessage(INVALID_AGE_EXCEPTION_CODE, null,
        INVALID_AGE_EXCEPTION_CODE, locale);

    return new ErrorsDto(errorMessage);
  }

  /**
   * REST controller for the signUp method of the user service.
   * @param params The register parameters
   * @return The user authenticated data
   * @throws DuplicateInstanceException If the user was already registered
   * @throws InvalidDateException If an invalid date was introduced while at the registration
   *      process
   */
  @PostMapping("/signUp")
  public ResponseEntity<UserAuthenticatedDto> signUp(@Validated @RequestBody
                                                           RegisterParamsDto params)
      throws DuplicateInstanceException, InvalidDateException {

    final UserImpl user = (UserImpl) UserConversor.fromRegisterDto(params);
    userService.signUp(user);

    final UserAuthenticatedDto userAuthenticated = new UserAuthenticatedDto(user.getUserName(),
        generateServiceToken(user));

    final URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
        .buildAndExpand(user.getId()).toUri();

    return ResponseEntity.created(location).body(userAuthenticated);
  }

  /**
   * REST Controller for the setSearchCriteria method of the user service.
   * @param userId The id of the user whose search criteria is going to be updated
   * @param criteria The new search criteria
   * @throws InstanceNotFoundException If the user was not found
   * @throws InvalidAgeException If the age specified was not valid
   * @throws InvalidRateException If the rate specified was not valid
   * @throws NotRatedException If the user was not rated yet
   */
  @PutMapping("/searchCriteria")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void setSearchCriteria(@RequestAttribute Long userId, @Validated @RequestBody
      SearchCriteriaDto criteria) throws InstanceNotFoundException, InvalidAgeException,
      InvalidRateException, NotRatedException {
    final SearchCriteria searchCriteria = new SearchCriteria(criteria.getSex(),
        criteria.getMinAge(), criteria.getMaxAge(), criteria.getCity(), criteria.getMinRate());
    userService.setSearchCriteria(userId, searchCriteria);
  }

  /**
   * REST Controller for the getSearchCriteria method of the user service.
   * @param userId The user id whose criteria is being queried
   * @return The criteria of the user
   * @throws InstanceNotFoundException If the user did not exist
   */
  @GetMapping("/searchCriteria")
  public SearchCriteriaDto getSearchCriteria(@RequestAttribute Long userId)
      throws InstanceNotFoundException {
    final SearchCriteria criteria = userService.getSearchCriteria(userId);

    return SearchCriteriaConversor.toSearchCriteriaDto(criteria);
  }

  /**
   * REST controller for the login method of the user service.
   * @param params The login parameters
   * @return The authenticated user data
   * @throws IncorrectLoginException If the login params were invalid
   */
  @PostMapping("/login")
  public UserAuthenticatedDto login(@Validated @RequestBody LoginParamsDto params)
      throws IncorrectLoginException {

    final UserImpl user = userService.login(params);

    return new UserAuthenticatedDto(params.getUserName(), generateServiceToken(user));
  }

  /**
   * Gets the profile data of the user.
   * @param userId The user whose profile data is being queried
   * @return The profile data
   * @throws InstanceNotFoundException If the user did not exist
   */
  @GetMapping("/data")
  public FullUserProfileDto getUserData(@RequestAttribute Long userId)
      throws InstanceNotFoundException {
    final UserImpl user = userService.loginFromUserId(userId);

    return new FullUserProfileDto(
        user.getRating(), user.isPremium(),
        new DateUserProfileDto(
            user.getDate().getDayOfMonth(),
            user.getDate().getMonthValue(),
            user.getDate().getYear(),
            new AgelessUserProfileDto(
                user.getSex(),
                user.getCity(),
                user.getDescription()
            )
        )
    );
  }

  @PutMapping("/updateProfile")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void updateProfile(@RequestAttribute Long userId,
                            @Validated @RequestBody DateUserProfileDto updateProfileInDto)
      throws InstanceNotFoundException, InvalidDateException {
    userService.updateProfile(userId, UserConversor.toUserImpl(updateProfileInDto));
  }

  /**
   * REST Controller for the rateUser method of the user service.
   * @param userId The user rating
   * @param rateDto The rating to be done
   * @return The new rating
   * @throws InstanceNotFoundException If the user rating did not exist
   * @throws InvalidRateException If the rate was invalid
   * @throws ItsNotYourFriendException If the user being rated was not a friend of the user rating
   */
  @PostMapping("/rate")
  public double rate(@RequestAttribute Long userId, @Validated @RequestBody RateDto rateDto)
      throws InstanceNotFoundException, InvalidRateException, ItsNotYourFriendException {

    return userService.rateUser(rateDto.getRate(), userId, rateDto.getUserObject());

  }

  /**
   * REST Controller for the updatePremium method of the user service.
   * @param userId The id of the user that is updating its premium status
   * @param premiumDto The premium status data
   * @throws InstanceNotFoundException If the user did not exist
   */
  @PutMapping("/premium")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void updatePremium(@RequestAttribute Long userId,
                            @Validated @RequestBody PremiumFormDto premiumDto)
      throws InstanceNotFoundException {

    userService.updatePremium(userId, premiumDto.isPremium());

  }

  @GetMapping("/topUsers")
  @ResponseStatus(value = HttpStatus.OK)
  public BlockDto<FullUserProfileDto> getTopUsers(@RequestParam String city,
                                                  @RequestParam(defaultValue = "0")
                                                  @Min(0) int page,
                                                  @RequestParam(defaultValue = "10")
                                                    @Min(1) int size) {
    final Block<UserImpl> users = userService.getTopUsers(city, page, size);
    return UserConversor.toReturnedUserBlockDto(users);
  }

}
