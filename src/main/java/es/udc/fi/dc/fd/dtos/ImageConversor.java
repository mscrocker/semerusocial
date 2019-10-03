package es.udc.fi.dc.fd.dtos;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import es.udc.fi.dc.fd.controller.exception.InvalidImageFormatException;
import es.udc.fi.dc.fd.model.persistence.ImageImpl;
import es.udc.fi.dc.fd.service.Block;
import es.udc.fi.dc.fd.service.BlockImageByUserId;

public class ImageConversor {

	private ImageConversor() {
	}

	public final static ImageCreationDto toImageCreationDto(ImageImpl image) {
		String encoded = new String(Base64.getMimeEncoder().encode(image.getData()));
		return new ImageCreationDto(encoded, image.getDescription());
	}

	public final static ImageCreatedDto toImageCreatedDto(ImageImpl image) {
		return new ImageCreatedDto(image.getImageId());
	}

	public final static ImageImpl toImageImpl(ImageCreationDto image) throws InvalidImageFormatException {
		String dataString = image.getData();
		if (dataString.indexOf(',') == -1)
			throw new InvalidImageFormatException("Base64 lacking metadata");

		String[] splitted = dataString.split(",");
		String metadata = splitted[0];
		int index = metadata.indexOf(':');
		int index2 = metadata.indexOf(';');
		int index3 = metadata.indexOf('/');

		if (index == -1 || index2 == -1 || index3 == -1)
			throw new InvalidImageFormatException("Base64 lacking metadata");

		String type = metadata.substring(index, index2).split("/")[1];
		byte[] decodString = Base64.getMimeDecoder().decode(splitted[1]);

		return new ImageImpl(decodString, image.getDescription(), type);
	}

	public final static ImageImpl toImageImpl(ImageEditionDto image) {
		return new ImageImpl(image.getDescription());
	}

	public final static BlockDto<ReturnedImageDto> toReturnedImageDto(Block<ImageImpl> images) {
		List<ImageImpl> imagesIn = images.getImages();

		List<ReturnedImageDto> imagesOut = imagesIn.stream().map(e -> toReturnedImageDto(e))
				.collect(Collectors.toList());

		return new BlockDto<>(imagesOut, images.getExistMoreImages());
	}

	public final static BlockImageByUserIdDto<ReturnedImageDto> toReturnedImageDto(
			BlockImageByUserId<ImageImpl> image) {
		ImageImpl imageIn = image.getImage();

		ReturnedImageDto imageOut = toReturnedImageDto(imageIn);

		return new BlockImageByUserIdDto<>(imageOut, image.getPrevId(), image.getNextId());
	}

	public final static ReturnedImageDto toReturnedImageDto(ImageImpl image) {
		String encoded = new String(Base64.getMimeEncoder().encode(image.getData()));
		return new ReturnedImageDto(encoded, image.getDescription(), image.getType());
	}

	public final static BlockDto<ReturnedImagesDto> toReturnedImagesDto(Block<ImageImpl> images) {
		List<ImageImpl> imagesIn = images.getImages();

		List<ReturnedImagesDto> imagesOut = imagesIn.stream().map(e -> toReturnedImagesDto(e))
				.collect(Collectors.toList());

		return new BlockDto<>(imagesOut, images.getExistMoreImages());
	}

	public final static ReturnedImagesDto toReturnedImagesDto(ImageImpl image) {
		String encoded = new String(Base64.getMimeEncoder().encode(image.getData()));

		return new ReturnedImagesDto(image.getImageId(), encoded);
	}

}
