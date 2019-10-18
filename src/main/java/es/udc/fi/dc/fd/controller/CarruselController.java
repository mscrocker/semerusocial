package es.udc.fi.dc.fd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import es.udc.fi.dc.fd.controller.exception.InstanceNotFoundException;
import es.udc.fi.dc.fd.dtos.BlockDto;
import es.udc.fi.dc.fd.dtos.ImageConversor;
import es.udc.fi.dc.fd.dtos.ReturnedImageDto;
import es.udc.fi.dc.fd.model.persistence.ImageImpl;
import es.udc.fi.dc.fd.service.Block;
import es.udc.fi.dc.fd.service.ImageService;

@Controller
public class CarruselController {
	@Autowired
	ImageService imageService;

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
	public final ModelAndView displayFr0iendFinder() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName(UserViewConstants.Friend_Finder);
		Block<ImageImpl> images;
		try {
			images = imageService.getImagesByUserId(1L, 0);
			BlockDto<ReturnedImageDto> blockDto = ImageConversor.toReturnedImageDto(images);

			modelAndView.addObject("block", blockDto);

		} catch (InstanceNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return modelAndView;
	}
}
