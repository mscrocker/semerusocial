package es.udc.fi.dc.fd.dtos;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotEmpty;

public class ReturnedImagesDto implements Serializable {
	private static final long serialVersionUID = 1328776989450853492L;

	@NotEmpty
	private String data;


	public ReturnedImagesDto(String data) {
		super();
		setData(data);
	}
	
	public ReturnedImagesDto() {
		super();
	}


	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
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
		ReturnedImagesDto other = (ReturnedImagesDto) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "ReturnedImageDto [data=" + data + "]";
	}



}
