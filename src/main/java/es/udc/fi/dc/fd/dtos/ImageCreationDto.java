package es.udc.fi.dc.fd.dtos;

import java.io.Serializable;
import java.util.Arrays;

import org.hibernate.validator.constraints.NotEmpty;

public final class ImageCreationDto implements Serializable {

	private static final long serialVersionUID = 1328776989450853492L;

	@NotEmpty
	private byte[] data;

	@NotEmpty
	private String description;

   	public ImageCreationDto() {
   		super();
   	}
   	
   	public ImageCreationDto(byte[] data, String description) {
		super();
		setData(data);
		setDescription(description);
	}
   	
	public byte[] getData() {
		return data;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setData(byte[] data) {
		this.data = data;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + Arrays.hashCode(data);
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
		ImageCreationDto other = (ImageCreationDto) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (!Arrays.equals(data, other.data))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ImageCreationDto [data=" + Arrays.toString(data) + ", description=" + description
				+ "]";
	}
	
}

