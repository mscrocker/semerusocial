package es.udc.fi.dc.fd.controller;

import java.time.LocalDateTime;
import java.time.Period;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import es.udc.fi.dc.fd.controller.exception.InstanceNotFoundException;
import es.udc.fi.dc.fd.dtos.BlockDto;
import es.udc.fi.dc.fd.dtos.ImageConversor;
import es.udc.fi.dc.fd.dtos.ReturnedImageDto;
import es.udc.fi.dc.fd.model.persistence.ImageImpl;
import es.udc.fi.dc.fd.model.persistence.UserImpl;
import es.udc.fi.dc.fd.service.Block;
import es.udc.fi.dc.fd.service.ImageService;
import es.udc.fi.dc.fd.service.PermissionChecker;

@Controller
public class CarruselController {
	@Autowired
	ImageService imageService;
	@Autowired
	PermissionChecker permissionChecker;

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
	public final ModelAndView displayFriendFinder() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName(UserViewConstants.Friend_Finder);
		Block<ImageImpl> images;
		try {
			images = imageService.getImagesByUserId(1L, 0);
			// Add user Data (Description, Age, Id (important), name ...)
			UserImpl user = permissionChecker.checkUserByUserId(1L);
			Integer age = Period.between(user.getDate().toLocalDate(), LocalDateTime.now().toLocalDate()).getYears();
			BlockDto<ReturnedImageDto> blockDto = ImageConversor.toReturnedImageDto(images);

			modelAndView.addObject("block", blockDto);
			modelAndView.addObject("user", user);
			modelAndView.addObject("age", age);

		} catch (InstanceNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return modelAndView;
	}
}
