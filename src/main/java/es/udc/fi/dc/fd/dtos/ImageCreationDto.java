package es.udc.fi.dc.fd.dtos;

import java.io.Serializable;
import java.util.Objects;

import org.hibernate.validator.constraints.NotEmpty;

public final class ImageCreationDto implements Serializable {

	private static final long serialVersionUID = 1328776989450853492L;

	@NotEmpty
	private String data;

	private String type;

	public ImageCreationDto() {
		super();
	}

	public ImageCreationDto(String data) {
		super();
		setData(data);
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
		final ImageCreationDto other = (ImageCreationDto) obj;
		return Objects.equals(data, other.data) && Objects.equals(type, other.type);
	}

}
