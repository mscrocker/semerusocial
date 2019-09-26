package es.udc.fi.dc.fd.controller.entity;


import static com.google.common.base.Preconditions.checkNotNull;

import java.net.URI;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import es.udc.fi.dc.fd.controller.exception.DuplicateInstanceException;
import es.udc.fi.dc.fd.controller.exception.IncorrectLoginException;
import es.udc.fi.dc.fd.dtos.ErrorsDto;
import es.udc.fi.dc.fd.dtos.LoginParamsDto;
import es.udc.fi.dc.fd.dtos.UserAuthenticatedDto;
import es.udc.fi.dc.fd.jwt.JwtGenerator;
import es.udc.fi.dc.fd.jwt.JwtGeneratorImpl;
import es.udc.fi.dc.fd.jwt.JwtInfo;
import es.udc.fi.dc.fd.model.persistence.UserImpl;
import es.udc.fi.dc.fd.service.UserService;


@RestController
@RequestMapping("/users")
public class UserController {
	
	private final static String INCORRECT_LOGIN_EXCEPTION_CODE = "project.exceptions.IncorrectLoginException";
	private final static String DUPLICATE_INSTANCE_EXCEPTION_CODE = "project.exceptions.DuplicateInstanceException";

	private MessageSource messageSource;
	
	@Bean
	JwtGenerator JwtGenerator() {
		return new JwtGeneratorImpl();
	}
	
	private final JwtGenerator jwtGenerator = JwtGenerator();
	private final UserService userService;
	
	@Autowired
	public UserController(final UserService userService, final MessageSource messageSource){
		super();
		
        this.userService = checkNotNull(userService,
                "Received a null pointer as service userService");
        
        this.messageSource = checkNotNull(messageSource,
                "Received a null pointer as messageSource");
		
	}
	
	@ExceptionHandler(DuplicateInstanceException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorsDto handleDuplicateInstanceException(DuplicateInstanceException exception, Locale locale) {
		
		String nameMessage = messageSource.getMessage(exception.getName(), null, exception.getName(), locale);
		String errorMessage = messageSource.getMessage(DUPLICATE_INSTANCE_EXCEPTION_CODE, 
				new Object[] {nameMessage, exception.getKey().toString()}, DUPLICATE_INSTANCE_EXCEPTION_CODE, locale);

		return new ErrorsDto(errorMessage);
		
	}
	
	@ExceptionHandler(IncorrectLoginException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public ErrorsDto handleIncorrectLoginException(IncorrectLoginException exception, Locale locale) {
		
		String errorMessage = messageSource.getMessage(INCORRECT_LOGIN_EXCEPTION_CODE, null,
				INCORRECT_LOGIN_EXCEPTION_CODE, locale);

		return new ErrorsDto(errorMessage);
		
	}
	
	@PostMapping("/signUp")
	public ResponseEntity<UserAuthenticatedDto> signUp(
		@RequestBody UserImpl user) throws DuplicateInstanceException {
		
		long id = userService.signUp(user);
		
		user.setUserId(id);

		UserAuthenticatedDto userAuthenticated = new UserAuthenticatedDto(user.getUserName(), user.getPassword(), generateServiceToken(user));
		

		URI location = ServletUriComponentsBuilder
			.fromCurrentRequest().path("/{id}")
			.buildAndExpand(user.getUserId()).toUri();

		return ResponseEntity.created(location).body(userAuthenticated);

	}
	
	@PostMapping("/login")
	public UserAuthenticatedDto login(@Validated @RequestBody LoginParamsDto params)
		throws IncorrectLoginException {
		
		UserImpl user = userService.login(params);
		
		return new UserAuthenticatedDto(params.getUserName(), params.getPassword(),generateServiceToken(user));
		
	}
	
	private String generateServiceToken(UserImpl user) {
		
		JwtInfo jwtInfo = new JwtInfo(user.getUserId(), user.getUserName());
		
		return jwtGenerator.generate(jwtInfo);
		
	}
	
	
	
	
}
