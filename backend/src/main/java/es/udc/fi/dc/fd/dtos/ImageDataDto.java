package es.udc.fi.dc.fd.dtos;

import java.io.Serializable;
import java.util.Objects;
import org.hibernate.validator.constraints.NotEmpty;

public final class ImageDataDto implements Serializable {

  private static final long serialVersionUID = 1328776989450853492L;

  @NotEmpty
  private String data;

  private String type;

  public ImageDataDto() {
    super();
  }

  /**
   * Default constructor for the image data DTO, without its ID.
   *
   * @param data The data of the image
   * @param type The format of the image
   */
  public ImageDataDto(String data, String type) {
    super();
    setData(data);
    setType(type);
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

  @Override
  public String toString() {
    return "ImageCreationDto [type= " + type + ", data=" + data + "]";
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  @Override
  public int hashCode() {
    return Objects.hash(data, type);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final ImageDataDto other = (ImageDataDto) obj;
    return Objects.equals(data, other.data) && Objects.equals(type, other.type);
  }

}
