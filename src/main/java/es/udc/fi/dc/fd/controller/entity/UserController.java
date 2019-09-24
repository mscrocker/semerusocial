package es.udc.fi.dc.fd.controller.entity;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import es.udc.fi.dc.fd.controller.exception.DuplicateInstanceException;
import es.udc.fi.dc.fd.controller.exception.InstanceNotFoundException;
import es.udc.fi.dc.fd.model.User;
import es.udc.fi.dc.fd.model.persistence.UserImpl;
import es.udc.fi.dc.fd.service.UserService;

@RestController
public class UserController {

	private final static String INCORRECT_LOGIN_EXCEPTION_CODE = "project.exceptions.IncorrectLoginException";
	private final static String DUPLICATE_INSTANCE_EXCEPTION_CODE = "project.exceptions.DuplicateInstanceException";

	private MessageSource messageSource;

	@Autowired
	private final UserService userService;

	public UserController(final UserService userService, final MessageSource messageSource) {
		super();

		this.userService = checkNotNull(userService, "Received a null pointer as service userService");

		this.messageSource = checkNotNull(messageSource, "Received a null pointer as messageSource");

	}

	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	public ModelAndView createNewUser(@Valid UserImpl user, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView();
		try {
			userService.signUp(user);
		} catch (DuplicateInstanceException ex) {
			bindingResult.rejectValue("username", "error.user",
					"There is already a user registered with the username provided");
		}
		if (bindingResult.hasErrors())
			modelAndView.setViewName("registration");
		else {
			modelAndView.addObject("successMessage", "User has been registered successfully");
			modelAndView.addObject("user", new UserImpl());
			modelAndView.setViewName("welcome");
		}

		return modelAndView;
	}

	@RequestMapping(value = "/admin/home", method = RequestMethod.GET)
	public ModelAndView home() {
		ModelAndView modelAndView = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user;
		try {
			user = userService.findByUserName(auth.getName());
		} catch (InstanceNotFoundException e) {
			// Unreachable statement?
			e.printStackTrace();
			return modelAndView;
		}
		modelAndView.addObject("userName", "Welcome " + user.getUserName() + " " + user.getPassword() + " (" + ")");
		modelAndView.addObject("adminMessage", "Content Available Only for Users with Admin Role");
		modelAndView.setViewName("admin/home");
		return modelAndView;
	}

	@RequestMapping(value = { "/login" }, method = RequestMethod.GET)
	public ModelAndView login() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("login");
		return modelAndView;
	}

	@RequestMapping(value = "/registration", method = RequestMethod.GET)
	public ModelAndView registration() {
		ModelAndView modelAndView = new ModelAndView();
		User user = new UserImpl();
		modelAndView.addObject("user", user);
		modelAndView.setViewName("signUp");
		return modelAndView;
	}
}
