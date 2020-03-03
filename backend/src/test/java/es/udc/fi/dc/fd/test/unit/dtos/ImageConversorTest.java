package es.udc.fi.dc.fd.test.unit.dtos;


import static org.junit.jupiter.api.Assertions.assertEquals;

import es.udc.fi.dc.fd.dtos.BlockDto;
import es.udc.fi.dc.fd.dtos.ImageConversor;
import es.udc.fi.dc.fd.dtos.ImageDataDto;
import es.udc.fi.dc.fd.model.persistence.ImageImpl;
import es.udc.fi.dc.fd.model.persistence.UserImpl;
import es.udc.fi.dc.fd.service.Block;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Base64;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class ImageConversorTest {


  @Test
  void testToImageCreationDto() {
    UserImpl user = new UserImpl();
    ImageImpl image = new ImageImpl(user, new byte[] {10, 12, 13}, "png");
    image.setImageId(2L);
    ImageDataDto dto = ImageConversor.toImageDataDto(image);
    assertEquals(dto.getData(), new String(Base64.getMimeEncoder().encode(image.getData()), Charset.forName("utf8")));
    assertEquals(dto.getType(), image.getType());
  }

  @Test
  void testToReturnedImageDto() {
    Block<ImageImpl> images = new Block<>();
    UserImpl user = new UserImpl();
    images.setElements(Arrays.asList(new ImageImpl[] {
        new ImageImpl(user, new byte[] {10, 12, 13}, "png"),
        new ImageImpl(user, new byte[] {11, 12, 13}, "jpg"),
        new ImageImpl(user, new byte[] {12, 12, 13}, "png"),
    }));
    images.setExistMoreElements(false);

    BlockDto<ImageDataDto> dto = ImageConversor.toImageDataDtos(images);
    assertEquals(dto.isExistMoreElements(), images.isExistMoreElements());
    assertEquals(dto.getElements(), images.getElements().stream().map(
        (ImageImpl e) -> ImageConversor.toImageDataDto(e)
    ).collect(Collectors.toList()));

  }

}
