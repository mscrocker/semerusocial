package es.udc.fi.dc.fd.dtos;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotEmpty;

public final class ImageEditionDto implements Serializable {

	private static final long serialVersionUID = 1328776989450853492L;

	@NotEmpty
	private String description;

   	public ImageEditionDto() {
   		super();
   	}
   	
   	public ImageEditionDto(String description) {
		super();
		setDescription(description);
	}
   	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
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
		ImageEditionDto other = (ImageEditionDto) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ImageCreationDto [description=" + description + "]";
	}
	
}

