package es.udc.fi.dc.fd.dtos;

import java.io.Serializable;

public final class ImageCreatedDto implements Serializable {

	private static final long serialVersionUID = 1328776989450853492L;

	private Long imageId;

   	public ImageCreatedDto() {
   		super();
   	}
   	
   	public ImageCreatedDto(Long imageId) {
		super();
		setImageId(imageId);
	}

	public Long getImageId() {
		return imageId;
	}

	public void setImageId(Long imageId) {
		this.imageId = imageId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((imageId == null) ? 0 : imageId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ImageCreatedDto other = (ImageCreatedDto) obj;
		if (imageId == null) {
			if (other.imageId != null)
				return false;
		} else if (!imageId.equals(other.imageId))
			return false;
		return true;
	}
   	
	@Override
	public String toString() {
		return "ImageCreatedDto [imageId=" + imageId + "]";
	}
	
}

