package es.udc.fi.dc.fd.controller.entity;


import static com.google.common.base.Preconditions.checkNotNull;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import es.udc.fi.dc.fd.controller.exception.DuplicateInstanceException;
import es.udc.fi.dc.fd.jwt.JwtGenerator;
import es.udc.fi.dc.fd.jwt.JwtGeneratorImpl;
import es.udc.fi.dc.fd.jwt.JwtInfo;
import es.udc.fi.dc.fd.model.persistence.UserImpl;
import es.udc.fi.dc.fd.service.UserService;
import es.udc.fi.dc.fd.service.UserServiceImpl;


@Controller
@RequestMapping("/users")
public class UserController {
	
	@Bean
	JwtGenerator JwtGenerator() {
		return new JwtGeneratorImpl();
	}
	
	private final JwtGenerator jwtGenerator = JwtGenerator();
	private final UserService userService;
	
	@Autowired
	public UserController(final UserService user){
		super();
		
        userService = checkNotNull(user,
                "Received a null pointer as service");
		
	}
	@PostMapping("/signUp")
	public ResponseEntity<String> signUp(
		@RequestBody UserImpl user) throws DuplicateInstanceException {
		System.out.println(user.getUserName());
		
		userService.signUp(user);
		System.out.println(user.getUserName());
		URI location = ServletUriComponentsBuilder
			.fromCurrentRequest().path("/{id}")
			.buildAndExpand(user.getUserId()).toUri();
		System.out.println(user.getUserName());
		return ResponseEntity.created(location).body(generateServiceToken(user));

	}
	
	private String generateServiceToken(UserImpl user) {
		
		JwtInfo jwtInfo = new JwtInfo(user.getUserId(), user.getUserName());
		
		return jwtGenerator.generate(jwtInfo);
		
	}
	
}
