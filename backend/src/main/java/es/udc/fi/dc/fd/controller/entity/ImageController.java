package es.udc.fi.dc.fd.controller.entity;

import static com.google.common.base.Preconditions.checkNotNull;

import java.net.URI;
import java.util.Locale;

import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import es.udc.fi.dc.fd.controller.exception.InstanceNotFoundException;
import es.udc.fi.dc.fd.controller.exception.InvalidImageFormatException;
import es.udc.fi.dc.fd.controller.exception.ItsNotYourImageException;
import es.udc.fi.dc.fd.dtos.BlockDto;
import es.udc.fi.dc.fd.dtos.ErrorsDto;
import es.udc.fi.dc.fd.dtos.IdDto;
import es.udc.fi.dc.fd.dtos.ImageConversor;
import es.udc.fi.dc.fd.dtos.ImageDataDto;
import es.udc.fi.dc.fd.dtos.ImageDto;
import es.udc.fi.dc.fd.model.persistence.ImageImpl;
import es.udc.fi.dc.fd.service.Block;
import es.udc.fi.dc.fd.service.ImageService;

@RestController
@RequestMapping("/images")
public class ImageController {

	private final static String ITS_NOT_YOUR_IMAGE_EXCEPTION_CODE = "project.exceptions.ItsNotYourImageException";
	private final static String INSTANCE_NOT_FOUND_EXCEPTION_CODE = "project.exceptions.InstanceNotFoundException";
	private static final String INVALID_IMAGE_FORMAT_EXCEPTION_CODE = "project.exceptions.InvalidImageFormatException";

	private final MessageSource messageSource;

	private final ImageService imageService;

	@Autowired
	public ImageController(final ImageService imageService, final MessageSource messageSource) {
		super();

		this.imageService = checkNotNull(imageService, "Received a null pointer as imageService in ImageController");

		this.messageSource = checkNotNull(messageSource, "Received a null pointer as messageSource in ImageController");
	}

	@ExceptionHandler(InstanceNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public ErrorsDto handleInstanceNotFoundException(InstanceNotFoundException exception, Locale locale) {

		final String nameMessage = messageSource.getMessage(exception.getName(), null, exception.getName(), locale);
		final String errorMessage = messageSource.getMessage(INSTANCE_NOT_FOUND_EXCEPTION_CODE,
				new Object[] { nameMessage, exception.getKey().toString() }, INSTANCE_NOT_FOUND_EXCEPTION_CODE, locale);

		return new ErrorsDto(errorMessage);
	}

	@ExceptionHandler(ItsNotYourImageException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ResponseBody
	public ErrorsDto handleItsNotYourImageException(ItsNotYourImageException exception, Locale locale) {
		final String errorMessage = messageSource.getMessage(ITS_NOT_YOUR_IMAGE_EXCEPTION_CODE, null,
				ITS_NOT_YOUR_IMAGE_EXCEPTION_CODE, locale);

		return new ErrorsDto(errorMessage);
	}

	@ExceptionHandler(InvalidImageFormatException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorsDto handleInvalidFormatException(InvalidImageFormatException exception, Locale locale) {
		final String errorMessage = messageSource.getMessage(INVALID_IMAGE_FORMAT_EXCEPTION_CODE, null,
				INVALID_IMAGE_FORMAT_EXCEPTION_CODE, locale);

		return new ErrorsDto(errorMessage);
	}

	@PostMapping("/add")
	public ResponseEntity<IdDto> addImage(@Validated @RequestBody ImageDataDto image,
			@RequestAttribute Long userId) throws InstanceNotFoundException, InvalidImageFormatException {
		final ImageImpl imageResult = imageService.addImage(ImageConversor.toImageImpl(image), userId);

		final IdDto imageResultDto = new IdDto(imageResult.getImageId());

		final URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{imageId}")
				.buildAndExpand(imageResult.getImageId()).toUri();

		return ResponseEntity.created(location).body(imageResultDto);
	}

	@DeleteMapping("/remove/{imageId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void removeImage(@PathVariable Long imageId, @RequestAttribute Long userId)
			throws InstanceNotFoundException, ItsNotYourImageException {
		imageService.removeImage(imageId, userId);
	}

	@GetMapping("/carrusel")
	public BlockDto<ImageDto> getImagesById(@RequestAttribute Long userId, @RequestParam int page)
			throws InstanceNotFoundException {
		final Block<ImageImpl> image = imageService.getImagesByUserId(userId, page);

		return ImageConversor.toReturnedImagesDto(image);
	}

	@GetMapping("/carrusel/user/{userId}")
	public BlockDto<ImageDataDto> getCarruselOfUser(@PathVariable Long userId, @RequestParam int page)
			throws InstanceNotFoundException {
		final Block<ImageImpl> images = imageService.getImagesByUserId(userId, page);
		final BlockDto<ImageDataDto> blockDto = ImageConversor.toImageDataDtos(images);

		return blockDto;
	}

	@GetMapping("/anonymousCarrusel")
	public BlockDto<ImageDataDto> getAnonymousCarrusel(@RequestParam @NotEmpty String city,
			@RequestParam @Min(0) int page)
					throws InstanceNotFoundException {
		final Block<ImageImpl> images = imageService.getAnonymousCarrusel(city, page);

		return ImageConversor.toImageDataDtos(images);
	}

}