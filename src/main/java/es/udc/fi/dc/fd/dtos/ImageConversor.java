package es.udc.fi.dc.fd.dtos;

import java.nio.charset.Charset;
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
		final String encoded = new String(Base64.getMimeEncoder().encode(image.getData()), Charset.forName("utf8"));
		return new ImageCreationDto(encoded);
	}

	public final static ImageCreatedDto toImageCreatedDto(ImageImpl image) {
		return new ImageCreatedDto(image.getImageId());
	}

	public final static ImageImpl toImageImpl(ImageCreationDto image) throws InvalidImageFormatException {
		final String dataString = image.getData();
		if (dataString.indexOf(',') == -1) {
			throw new InvalidImageFormatException("Base64 lacking metadata");
		}

		final String[] splitted = dataString.split(",");
		final String metadata = splitted[0];
		final int index = metadata.indexOf(':');
		final int index2 = metadata.indexOf(';');
		final int index3 = metadata.indexOf('/');

		if (index == -1 || index2 == -1 || index3 == -1) {
			throw new InvalidImageFormatException("Base64 lacking metadata");
		}

		final String type = metadata.substring(index, index2).split("/")[1];
		final byte[] decodString = Base64.getMimeDecoder().decode(splitted[1]);

		return new ImageImpl(decodString, type);
	}

	public final static BlockDto<ReturnedImageDto> toReturnedImageDto(Block<ImageImpl> images) {
		final List<ImageImpl> imagesIn = images.getImages();

		final List<ReturnedImageDto> imagesOut = imagesIn.stream().map(e -> toReturnedImageDto(e))
				.collect(Collectors.toList());

		return new BlockDto<>(imagesOut, images.getExistMoreImages());
	}

	public final static BlockImageByUserIdDto<ReturnedImageDto> toReturnedImageDto(
			BlockImageByUserId<ImageImpl> image) {
		final ImageImpl imageIn = image.getImage();

		final ReturnedImageDto imageOut = toReturnedImageDto(imageIn);

		return new BlockImageByUserIdDto<>(imageOut, image.getPrevId(), image.getNextId());
	}

	public final static ReturnedImageDto toReturnedImageDto(ImageImpl image) {
		final String encoded = new String(Base64.getMimeEncoder().encode(image.getData()), Charset.forName("utf8"));
		return new ReturnedImageDto(encoded, image.getType());
	}

	public final static BlockDto<ReturnedImagesDto> toReturnedImagesDto(Block<ImageImpl> images) {
		final List<ImageImpl> imagesIn = images.getImages();

		final List<ReturnedImagesDto> imagesOut = imagesIn.stream().map(e -> toReturnedImagesDto(e))
				.collect(Collectors.toList());

		return new BlockDto<>(imagesOut, images.getExistMoreImages());
	}

	public final static ReturnedImagesDto toReturnedImagesDto(ImageImpl image) {
		final String encoded = new String(Base64.getMimeEncoder().encode(image.getData()), Charset.forName("utf8"));

		return new ReturnedImagesDto(image.getImageId(), encoded);
	}

}
