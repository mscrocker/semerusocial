package es.udc.fi.dc.fd.controller.entity;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import es.udc.fi.dc.fd.controller.exception.InstanceNotFoundException;
import es.udc.fi.dc.fd.controller.exception.ItsNotYourImageException;
import es.udc.fi.dc.fd.dtos.BlockDto;
import es.udc.fi.dc.fd.dtos.BlockImageByUserIdDto;
import es.udc.fi.dc.fd.dtos.ErrorsDto;
import es.udc.fi.dc.fd.dtos.ImageConversor;
import es.udc.fi.dc.fd.dtos.ImageCreatedDto;
import es.udc.fi.dc.fd.dtos.ImageCreationDto;
import es.udc.fi.dc.fd.dtos.ReturnedImageDto;
import es.udc.fi.dc.fd.model.persistence.ImageImpl;
import es.udc.fi.dc.fd.service.Block;
import es.udc.fi.dc.fd.service.BlockImageByUserId;
import es.udc.fi.dc.fd.service.ImageService;

@RestController
@RequestMapping("/images")
public class ImageController {
	
	private final static String ITS_NOT_YOUR_IMAGE_EXCEPTION_CODE = "project.exceptions.ItsNotYourImageException";
	private final static String INSTANCE_NOT_FOUND_EXCEPTION_CODE = "project.exceptions.InstanceNotFoundException";

	private MessageSource messageSource;
	
	private final ImageService imageService;
	
	@Autowired
	public ImageController(final ImageService imageService, final MessageSource messageSource){
		super();
		
        this.imageService = checkNotNull(imageService,
                "Received a null pointer as imageService in ImageController");
        
        this.messageSource = checkNotNull(messageSource,
                "Received a null pointer as messageSource in ImageController");
	}
	
	@ExceptionHandler(ItsNotYourImageException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ResponseBody
	public ErrorsDto handleItsNotYourImageException(ItsNotYourImageException exception, Locale locale) {
		String errorMessage = messageSource.getMessage(ITS_NOT_YOUR_IMAGE_EXCEPTION_CODE, null,
				ITS_NOT_YOUR_IMAGE_EXCEPTION_CODE, locale);

		return new ErrorsDto(errorMessage);
	}
	
	@ExceptionHandler(InstanceNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public ErrorsDto handleInstanceNotFoundException(InstanceNotFoundException exception, Locale locale) {
		
		String nameMessage = messageSource.getMessage(exception.getName(), null, exception.getName(), locale);
		String errorMessage = messageSource.getMessage(INSTANCE_NOT_FOUND_EXCEPTION_CODE, 
				new Object[] {nameMessage, exception.getKey().toString()}, INSTANCE_NOT_FOUND_EXCEPTION_CODE, locale);

		return new ErrorsDto(errorMessage);
	}
	
	@PostMapping("/add")
	public ImageCreatedDto addImage(@RequestAttribute Long userId, @RequestBody ImageCreationDto image) throws InstanceNotFoundException {
		ImageImpl imageResult = imageService.addImage(ImageConversor.toImageImpl(image), userId);

		return ImageConversor.toImageCreatedDto(imageResult);
	}
	
	@GetMapping("/carrusel")
	public BlockDto<ReturnedImageDto> getImagesById(@RequestAttribute Long userId, @RequestParam int page) throws InstanceNotFoundException{
		Block<ImageImpl> image = imageService.getImagesByUserId(userId, page);
				
		return ImageConversor.toReturnedImageDto(image);
	}
	
	@GetMapping("/carrusel/{imageId}")
	public BlockImageByUserIdDto<ReturnedImageDto> getImageById(@RequestAttribute Long userId, @PathVariable Long imageId) throws InstanceNotFoundException, ItsNotYourImageException{
		BlockImageByUserId<ImageImpl> image = imageService.getImageByUserId(userId, imageId);
				
		return ImageConversor.toReturnedImageDto(image);
	}
	
}
