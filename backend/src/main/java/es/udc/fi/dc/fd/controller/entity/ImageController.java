package es.udc.fi.dc.fd.controller.entity;

import static com.google.common.base.Preconditions.checkNotNull;

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

@RestController
@RequestMapping("/images")
public class ImageController {

  private static final String ITS_NOT_YOUR_IMAGE_EXCEPTION_CODE =
      "project.exceptions.ItsNotYourImageException";

  private static final String INSTANCE_NOT_FOUND_EXCEPTION_CODE =
      "project.exceptions.InstanceNotFoundException";

  private static final String INVALID_IMAGE_FORMAT_EXCEPTION_CODE =
      "project.exceptions.InvalidImageFormatException";

  private final MessageSource messageSource;

  private final ImageService imageService;

  /**
   * REST Controller for the image service.
   * @param imageService The instance of the service
   * @param messageSource The message source used for localization purposes
   */
  @Autowired
  public ImageController(final ImageService imageService, final MessageSource messageSource) {
    super();

    this.imageService = checkNotNull(imageService,
        "Received a null pointer as imageService in ImageController");

    this.messageSource = checkNotNull(messageSource,
        "Received a null pointer as messageSource in ImageController");
  }

  /**
   * Handler for the InstanceNotFoundException.
   * @param exception The instance of the exception to handle
   * @param locale The locale to use for the error message
   * @return The ErrorsDto containing the errors message
   */
  @ExceptionHandler(InstanceNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ResponseBody
  public ErrorsDto handleInstanceNotFoundException(InstanceNotFoundException exception,
                                                   Locale locale) {

    final String nameMessage = messageSource.getMessage(exception.getName(), null,
        exception.getName(), locale);
    final String errorMessage = messageSource.getMessage(INSTANCE_NOT_FOUND_EXCEPTION_CODE,
        new Object[] {nameMessage, exception.getKey().toString()},
        INSTANCE_NOT_FOUND_EXCEPTION_CODE, locale);

    return new ErrorsDto(errorMessage);
  }

  /**
   * Handler for the ItsNotYourFriendException.
   * @param exception The instance of the exception to handle
   * @param locale The locale to use for the error message
   * @return The ErrorsDto containing the errors message
   */
  @ExceptionHandler(ItsNotYourImageException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  @ResponseBody
  public ErrorsDto handleItsNotYourImageException(ItsNotYourImageException exception,
                                                  Locale locale) {
    final String errorMessage = messageSource.getMessage(ITS_NOT_YOUR_IMAGE_EXCEPTION_CODE,
        null, ITS_NOT_YOUR_IMAGE_EXCEPTION_CODE, locale);

    return new ErrorsDto(errorMessage);
  }

  /**
   * Handler for the InvalidImageFormatException.
   * @param exception The instance of the exception to handle
   * @param locale The locale to use for the error message
   * @return The ErrorsDto containing the errors message
   */
  @ExceptionHandler(InvalidImageFormatException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ErrorsDto handleInvalidFormatException(InvalidImageFormatException exception,
                                                Locale locale) {
    final String errorMessage = messageSource.getMessage(INVALID_IMAGE_FORMAT_EXCEPTION_CODE,
        null, INVALID_IMAGE_FORMAT_EXCEPTION_CODE, locale);

    return new ErrorsDto(errorMessage);
  }

  /**
   * Controller for the add image method of the image service.
   * @param image The image to add
   * @param userId The id of the user uploading the image
   * @return The id of the newly created image
   * @throws InstanceNotFoundException If the user did not exist
   * @throws InvalidImageFormatException If the image was invalid
   */
  @PostMapping("/add")
  public ResponseEntity<IdDto> addImage(@Validated @RequestBody ImageDataDto image,
                                        @RequestAttribute Long userId)
      throws InstanceNotFoundException, InvalidImageFormatException {
    final ImageImpl imageResult = imageService.addImage(ImageConversor.toImageImpl(image), userId);

    final IdDto imageResultDto = new IdDto(imageResult.getImageId());

    final URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{imageId}")
        .buildAndExpand(imageResult.getImageId()).toUri();

    return ResponseEntity.created(location).body(imageResultDto);
  }

  /**
   * Controller for the remove image method of the image service.
   * @param imageId The id of the image to remove
   * @param userId The id of the user removing that image
   * @throws InstanceNotFoundException If either the user or the image were not found
   * @throws ItsNotYourImageException If you were not the owner of the image
   */
  @DeleteMapping("/remove/{imageId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void removeImage(@PathVariable Long imageId, @RequestAttribute Long userId)
      throws InstanceNotFoundException, ItsNotYourImageException {
    imageService.removeImage(imageId, userId);
  }

  /**
   * Controller for the getImagesByUserId method of the image service.
   * @param userId The id of the user asking for the images
   * @param page The page being queried
   * @return The block with the images queried, and their IDs
   * @throws InstanceNotFoundException If the user was not found
   */
  @GetMapping("/carrusel")
  public BlockDto<ImageDto> getImagesById(@RequestAttribute Long userId, @RequestParam int page)
      throws InstanceNotFoundException {
    final Block<ImageImpl> image = imageService.getImagesByUserId(userId, page);

    return ImageConversor.toReturnedImagesDto(image);
  }

  /**
   * Controller for the getImagesByUserId method of the image service.
   * @param userId The id of the user asking for the images
   * @param page The page being queried
   * @return The block with the images queried, without their IDs
   * @throws InstanceNotFoundException If the user was not found
   */
  @GetMapping("/carrusel/user/{userId}")
  public BlockDto<ImageDataDto> getCarruselOfUser(@PathVariable Long userId, @RequestParam int page)
      throws InstanceNotFoundException {
    final Block<ImageImpl> images = imageService.getImagesByUserId(userId, page);
    final BlockDto<ImageDataDto> blockDto = ImageConversor.toImageDataDtos(images);

    return blockDto;
  }

  /**
   * Controller for the getAnonymousCarrusel method of the imageService.
   * @param city The city whose public carrusel is being queried
   * @param page The page being queried
   * @return The block with the images of the anonymous carrusel
   */
  @GetMapping("/anonymousCarrusel")
  public BlockDto<ImageDataDto> getAnonymousCarrusel(@RequestParam @NotEmpty String city,
                                                     @RequestParam @Min(0) int page) {
    final Block<ImageImpl> images = imageService.getAnonymousCarrusel(city, page);

    return ImageConversor.toImageDataDtos(images);
  }

}
