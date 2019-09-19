package es.udc.fi.dc.fd.controller.entity;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	/*
	//3. Inserción de anuncios
	@PostMapping("/auctions")
	public ImageCreationDto addImage(@Validated @RequestBody ImageCreationDto params, @RequestAttribute Long userId) throws InstanceNotFoundException 
		{
		ImageImpl auction = new ImageImpl(params.getName(), params.getDescription(),
				params.getDuration(), params.getSendInformation(), params.getStartingPrice(), null, null);
		return AuctionConversor.toAuctionCreationDto(imageService.addImage(auction,userId,params.getCategoryId()));
	}
	*/
}
