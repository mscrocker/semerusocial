package es.udc.fi.dc.fd.dtos;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import es.udc.fi.dc.fd.model.persistence.ImageImpl;
import es.udc.fi.dc.fd.service.Block;
import es.udc.fi.dc.fd.service.BlockImageByUserId;

public class ImageConversor {

	private ImageConversor() {}

	public final static ImageCreationDto toImageCreationDto(ImageImpl image) {
		return new ImageCreationDto(image.getData(), image.getDescription());
	}
	
	public final static ImageCreatedDto toImageCreatedDto(ImageImpl image) {
		return new ImageCreatedDto(image.getImageId());
	}
	
	public final static ImageImpl toImageImpl(ImageCreationDto image) {
		return new ImageImpl(image.getData(), image.getDescription());
	}
	
	public final static BlockDto<ReturnedImageDto> toReturnedImageDto(Block<ImageImpl> images){
		List<ImageImpl> imagesIn = images.getItems();
		
		List<ReturnedImageDto> imagesOut = imagesIn.stream().map(
				e -> toReturnedImageDto(e)
		).collect(Collectors.toList());
		
		return new BlockDto<ReturnedImageDto>(
			imagesOut,
			images.getExistMoreItems()
		);	
	}
	
	public final static BlockImageByUserIdDto<ReturnedImageDto> toReturnedImageDto(BlockImageByUserId<ImageImpl> image){
		ImageImpl imageIn = image.getImage();
		
		ReturnedImageDto imageOut = toReturnedImageDto(imageIn);

		return new BlockImageByUserIdDto<ReturnedImageDto>(
			imageOut,
			image.getPrevId(),
			image.getNextId()
		);	
	}
	
	public final static ReturnedImageDto toReturnedImageDto(ImageImpl image) {
		
		return new ReturnedImageDto(Arrays.toString(image.getData()).trim(), image.getDescription());
	
	}
	
}
