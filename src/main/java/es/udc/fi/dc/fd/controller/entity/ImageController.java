package es.udc.fi.dc.fd.controller.entity;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.udc.fi.dc.fd.controller.exception.InstanceNotFoundException;
import es.udc.fi.dc.fd.dtos.BlockDto;
import es.udc.fi.dc.fd.dtos.ImageConversor;
import es.udc.fi.dc.fd.dtos.ImageCreationDto;
import es.udc.fi.dc.fd.model.persistence.ImageImpl;
import es.udc.fi.dc.fd.service.Block;
import es.udc.fi.dc.fd.service.ImageService;

@RestController
@RequestMapping("/images")
public class ImageController {
	
	private final ImageService imageService;
	
	@Autowired
	public ImageController(final ImageService imageService){
		super();
		
        this.imageService = checkNotNull(imageService,
                "Received a null pointer as imageService in ImageController");
	}
	
	@PostMapping("/add")
	public ImageCreationDto addImage(@RequestAttribute Long userId, @RequestBody ImageCreationDto image) throws InstanceNotFoundException {
		ImageImpl imageResult = imageService.addImage(ImageConversor.toImageImpl(image), userId);
		
		return ImageConversor.toImageCreationDto(imageResult);
	}
	
	@GetMapping("/carrusel")
	public BlockDto<ImageCreationDto> getImagesById(@RequestAttribute Long userId, @RequestParam int page) throws InstanceNotFoundException{
		Block<ImageImpl> im = imageService.getImagesByUserId(userId, page);
				
		return ImageConversor.toImageCreationDto(im);
	}
	
}
