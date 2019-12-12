package es.udc.fi.dc.fd.dtos;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class ProfileDto {

	@NotNull
	@Min(value = 1)
	@Max(value = 31)
	private int day;

	@NotNull
	@Min(value = 1)
	@Max(value = 12)
	private int month;

	@NotNull
	@Min(value = 1)
	private int year;

	@NotEmpty
	private String sex;

	@NotEmpty
	@Length(max = 30)
	private String city;

	@NotEmpty
	@Length(max = 60)
	private String description;

	public ProfileDto() {
		super();
	}

	public ProfileDto(int day, int month, int year, String sex, String city, String description) {
		super();
		this.day = day;
		this.month = month;
		this.year = year;
		this.sex = sex;
		this.city = city;
		this.description = description;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
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
	public String toString() {
		return "UpdateProfileInDto [day=" + day + ", month=" + month + ", year=" + year + ", sex=" + sex + ", city="
				+ city + ", description=" + description + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + day;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + month;
		result = prime * result + ((sex == null) ? 0 : sex.hashCode());
		result = prime * result + year;
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
		ProfileDto other = (ProfileDto) obj;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (day != other.day)
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (month != other.month)
			return false;
		if (sex == null) {
			if (other.sex != null)
				return false;
		} else if (!sex.equals(other.sex))
			return false;
		if (year != other.year)
			return false;
		return true;
	}

	
}
