package es.udc.fi.dc.fd.controller.entity;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.udc.fi.dc.fd.controller.exception.InstanceNotFoundException;
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
                "Received a null pointer as service imageService");
		
	}
	
	@PostMapping("/add")	//TODO: password no debería ser devuelta
	public ImageCreationDto addImage(@RequestAttribute Long userId, @RequestBody ImageCreationDto image) throws InstanceNotFoundException 
		{
		ImageImpl imageResult = imageService.addImage(ImageConversor.toImageImpl( image),userId);
		return ImageConversor.toImageCreationDto(imageResult);
	}
	
	@PostMapping("/pageable")
	public ImageImpl pag (@RequestBody ImageCreationDto image) throws InstanceNotFoundException{
		long userId = 1L;
		
		Block<ImageImpl> im = imageService.getImagesByUserId(userId, 1, 3);
		
		System.out.println(im.getItems());
		
		return null;
		
	}
}
