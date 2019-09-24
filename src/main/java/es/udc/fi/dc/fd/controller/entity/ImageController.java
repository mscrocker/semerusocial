package es.udc.fi.dc.fd.controller.entity;

//@RestController
//@RequestMapping("/images")
//public class ImageController {
//
//	private final ImageService imageService;
//
//	@Autowired
//	public ImageController(final ImageService imageService){
//		super();
//
//        this.imageService = checkNotNull(imageService,
//                "Received a null pointer as service imageService");
//
//	}
//
//	@PostMapping("/add/{userId}")
//	public ImageCreationDto addImage(@PathVariable Long userId, @RequestBody ImageCreationDto image) throws InstanceNotFoundException
//		{
//		ImageImpl imageResult = imageService.addImage(ImageConversor.toImageImpl( image),userId);
//		return ImageConversor.toImageCreationDto(imageResult);
//	}
//}
