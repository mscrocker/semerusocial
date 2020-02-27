package es.udc.fi.dc.fd.dtos;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

public class ImageDto implements Serializable {
	private static final long serialVersionUID = 1328776989450853493L;

	@NotEmpty
	private Long imageId;

	@JsonUnwrapped
	private ImageDataDto imageData;

	public ImageDto(Long imageId, ImageDataDto imageData) {
		super();
		this.imageId = imageId;
		this.imageData = imageData;
	}

	public ImageDto() {
		super();
	}

	public Long getImageId() {
		return imageId;
	}

	public void setImageId(Long imageId) {
		this.imageId = imageId;
	}

	public ImageDataDto getImageData() {
		return imageData;
	}

	public void setImageData(ImageDataDto imageData) {
		this.imageData = imageData;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((imageData == null) ? 0 : imageData.hashCode());
		result = prime * result + ((imageId == null) ? 0 : imageId.hashCode());
		return result;
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
		ImageDto other = (ImageDto) obj;
		if (imageData == null) {
			if (other.imageData != null) {
				return false;
			}
		} else if (!imageData.equals(other.imageData)) {
			return false;
		}
		if (imageId == null) {
			if (other.imageId != null) {
				return false;
			}
		} else if (!imageId.equals(other.imageId)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "ImageDto [imageId=" + imageId + ", imageData=" + imageData + "]";
	}


}
