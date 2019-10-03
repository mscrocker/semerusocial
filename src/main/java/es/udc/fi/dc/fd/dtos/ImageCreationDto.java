package es.udc.fi.dc.fd.dtos;

import java.io.Serializable;
import java.util.Objects;

import org.hibernate.validator.constraints.NotEmpty;

public final class ImageCreationDto implements Serializable {

	private static final long serialVersionUID = 1328776989450853492L;

	@NotEmpty
	private String data;

	@NotEmpty
	private String description;

	private String type;

	public ImageCreationDto() {
		super();
	}

	public ImageCreationDto(String data, String description) {
		super();
		setData(data);
		setDescription(description);
	}

	public String getData() {
		return data;
	}

	public String getDescription() {
		return description;
	}

	public void setData(String data) {
		this.data = data;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "ImageCreationDto [data=" + data + ", description=" + description + "]";
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public int hashCode() {
		return Objects.hash(data, description, type);
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
		return Objects.equals(data, other.data) && Objects.equals(description, other.description)
				&& Objects.equals(type, other.type);
	}

}
