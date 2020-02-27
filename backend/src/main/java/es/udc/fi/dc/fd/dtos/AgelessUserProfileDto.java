package es.udc.fi.dc.fd.dtos;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;

public class AgelessUserProfileDto implements Serializable {
	@NotEmpty
	private String sex;

	@NotEmpty
	@Length(max = 30)
	private String city;

	@NotEmpty
	@Length(max = 60)
	private String description;

	public AgelessUserProfileDto(String sex, String city, String description) {
		super();
		this.sex = sex;
		this.city = city;
		this.description = description;
	}

	public AgelessUserProfileDto() {
		super();
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
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
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((sex == null) ? 0 : sex.hashCode());
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
		AgelessUserProfileDto other = (AgelessUserProfileDto) obj;
		if (city == null) {
			if (other.city != null) {
				return false;
			}
		} else if (!city.equals(other.city)) {
			return false;
		}
		if (description == null) {
			if (other.description != null) {
				return false;
			}
		} else if (!description.equals(other.description)) {
			return false;
		}
		if (sex == null) {
			return other.sex == null;
		} else {
			return sex.equals(other.sex);
		}
	}

	@Override
	public String toString() {
		return "AgelessUserProfileDto [sex=" + sex + ", city=" + city + ", description=" + description + "]";
	}

}
