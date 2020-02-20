package es.udc.fi.dc.fd.dtos;

import java.nio.charset.Charset;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import es.udc.fi.dc.fd.controller.exception.InvalidImageFormatException;
import es.udc.fi.dc.fd.model.persistence.ImageImpl;
import es.udc.fi.dc.fd.service.Block;

public class ImageConversor {

	private ImageConversor() {
	}

	public final static ImageDataDto toImageDataDto(ImageImpl image) {
		final String encoded = new String(Base64.getMimeEncoder().encode(image.getData()), Charset.forName("utf8"));
		return new ImageDataDto(encoded, image.getType());
	}
	
	public final static BlockDto<ImageDataDto> toImageDataDtos(Block<ImageImpl> images) {
		final List<ImageImpl> imagesIn = images.getElements();

		final List<ImageDataDto> imagesOut = imagesIn.stream().map(e -> toImageDataDto(e))
				.collect(Collectors.toList());

		return new BlockDto<>(imagesOut, images.isExistMoreElements());
	}

	public final static ImageImpl toImageImpl(ImageDataDto image) throws InvalidImageFormatException {
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

	public final static BlockDto<ImageDto> toReturnedImagesDto(Block<ImageImpl> images) {
		final List<ImageImpl> imagesIn = images.getElements();

		final List<ImageDto> imagesOut = imagesIn.stream().map(e -> toReturnedImagesDto(e))
				.collect(Collectors.toList());

		return new BlockDto<>(imagesOut, images.isExistMoreElements());
	}

	public final static ImageDto toReturnedImagesDto(ImageImpl image) {
		final String encoded = new String(Base64.getMimeEncoder().encode(image.getData()), Charset.forName("utf8"));

		return new ImageDto(image.getImageId(), new ImageDataDto(encoded, image.getType()));
	}

}