package es.udc.fi.dc.fd.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CarruselController {

	@GetMapping(path = "/carrusel/**")
	public final String displayLogin() {
		return UserViewConstants.CARRUSEL_FORM;
	}

	@GetMapping(path = "/addImage")
	public final String displayAddImage() {
		return UserViewConstants.ADD_IMAGE_FORM;
	}

	@GetMapping(path = "/users/profile")
	public final String displayGetUserData() {
		return UserViewConstants.GET_PROFILE;
	}

	@GetMapping(path = "/findFriend")
	public final String displayFriendFinder() {

		return UserViewConstants.Friend_Finder;
	}

	@GetMapping(path = "/chat")
	public final String displayChat() {

		return "chat";
	}
}
