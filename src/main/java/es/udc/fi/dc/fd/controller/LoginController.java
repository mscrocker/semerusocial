package es.udc.fi.dc.fd.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

	@GetMapping(path = "/login")
	public final String displayLogin() {
		return UserViewConstants.LOGIN_FORM;
	}

	@GetMapping(path = "/signup")
	public final String displaySignUpForm() {
		return UserViewConstants.SIGN_UP_FORM;
	}

}
