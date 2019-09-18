package es.udc.fi.dc.fd.controller.entity;


import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import es.udc.fi.dc.fd.controller.exception.DuplicateInstanceException;
import es.udc.fi.dc.fd.jwt.JwtGenerator;
import es.udc.fi.dc.fd.jwt.JwtInfo;
import es.udc.fi.dc.fd.model.persistence.UserImpl;
import es.udc.fi.dc.fd.service.UserService;


@Controller
@RequestMapping("/users")
public class UserController {
	
	private JwtGenerator jwtGenerator;
	
	private UserService userService;
	
	@PostMapping("/signUp")
	public ResponseEntity<String> signUp(
		@RequestBody UserImpl user) throws DuplicateInstanceException {
		
		
		userService.signUp(user);
		
		URI location = ServletUriComponentsBuilder
			.fromCurrentRequest().path("/{id}")
			.buildAndExpand(user.getUserId()).toUri();
	
		return ResponseEntity.created(location).body(generateServiceToken(user));

	}
	
	private String generateServiceToken(UserImpl user) {
		
		JwtInfo jwtInfo = new JwtInfo(user.getUserId(), user.getUserName());
		
		return jwtGenerator.generate(jwtInfo);
		
	}
	
}
