package es.udc.fi.dc.fd.dtos;

import es.udc.fi.dc.fd.controller.exception.InvalidImageFormatException;
import es.udc.fi.dc.fd.model.persistence.ImageImpl;
import es.udc.fi.dc.fd.service.Block;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

public final class ImageConversor {

  private ImageConversor() {
  }

  /**
   * Converts an image entity to an image dto with its ID.
   * @param image The image entity
   * @return The image dto
   */
  public static final ImageDataDto toImageDataDto(ImageImpl image) {
    final String encoded = new String(Base64.getMimeEncoder().encode(image.getData()),
        Charset.forName("utf8"));
    return new ImageDataDto(encoded, image.getType());
  }

  /**
   * Converts a block with image entities to a block DTO with DTOs for each image with their
   * IDs.
   * @param images The block with the image entities
   * @return The block DTO with the DTOs for each image
   */
  public static final BlockDto<ImageDataDto> toImageDataDtos(Block<ImageImpl> images) {
    final List<ImageImpl> imagesIn = images.getElements();

    final List<ImageDataDto> imagesOut = imagesIn.stream().map(e -> toImageDataDto(e))
        .collect(Collectors.toList());

    return new BlockDto<>(imagesOut, images.isExistMoreElements());
  }

  /**
   * Converts an image dto back to an image entity.
   * @param image The image DTO
   * @return The image entity
   * @throws InvalidImageFormatException If the image format was not recognized
   */
  public static final ImageImpl toImageImpl(ImageDataDto image) throws InvalidImageFormatException {
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

  /**
   * Converts a block with image entities to a block DTO with DTOs for each image without their
   * IDs.
   * @param images The block with the image entities
   * @return The block DTO with the DTOs for each image
   */
  public static final BlockDto<ImageDto> toReturnedImagesDto(Block<ImageImpl> images) {
    final List<ImageImpl> imagesIn = images.getElements();

    final List<ImageDto> imagesOut = imagesIn.stream().map(e -> toReturnedImagesDto(e))
        .collect(Collectors.toList());

    return new BlockDto<>(imagesOut, images.isExistMoreElements());
  }

  /**
   * Converts an image entity to an image dto without its ID.
   * @param image The image entity
   * @return The image dto
   */
  public static final ImageDto toReturnedImagesDto(ImageImpl image) {
    final String encoded = new String(Base64.getMimeEncoder().encode(image.getData()),
        Charset.forName("utf8"));

    return new ImageDto(image.getImageId(), new ImageDataDto(encoded, image.getType()));
  }

}
