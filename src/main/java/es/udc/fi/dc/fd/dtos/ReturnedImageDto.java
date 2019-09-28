package es.udc.fi.dc.fd.dtos;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotEmpty;

public class ReturnedImageDto implements Serializable {
	private static final long serialVersionUID = 1328776989450853492L;

	@NotEmpty
	private String data;

	@NotEmpty
	private String description;


	public ReturnedImageDto(String data, String description) {
		super();
		setData(data);
		setDescription(description);
	}
	
	public ReturnedImageDto() {
		super();
	}


	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
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
		result = prime * result + ((data == null) ? 0 : data.hashCode());
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
		ReturnedImageDto other = (ReturnedImageDto) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "ReturnedImageDto [data=" + data + ", description=" + description + "]";
	}



}
