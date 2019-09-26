package es.udc.fi.dc.fd.dtos;

import es.udc.fi.dc.fd.model.persistence.ImageImpl;

public class ImageConversor {

	private ImageConversor() {
	}

	public final static ImageCreationDto toImageCreationDto(ImageImpl image) {

		return new ImageCreationDto(image.getData(), image.getDescription());

	}
	
	public final static ImageImpl toImageImpl(ImageCreationDto image) {

		return new ImageImpl(image.getData(), image.getDescription());

	}
	/*
	public final static BlockDto<ImageCreationDto> toImageCreationDto(Block<ImageImpl> images){
		List<ImageImpl> imagesIn = images.getItems();
		
		List<ImageCreationDto> imagesOut = imagesIn.stream().map(
				e -> toImageCreationDto(e)
		).collect(Collectors.toList());
		
		return new BlockDto<ImageCreationDto>(
			imagesOut,
			images.getExistMoreItems()
		);	
	}
	*/
}
