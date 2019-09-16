package es.udc.fi.dc.fd.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {

	private static final String VIEW_LOGIN = "login";
	
	public LoginController() {
		super();
	}
	
	/**
     * Shows the login view.
     * 
     * @return the login view
     */
    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
	public final String showLoginForm() {
		return VIEW_LOGIN;
	}
}
