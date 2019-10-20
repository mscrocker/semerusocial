package es.udc.fi.dc.fd.dtos;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

public class UpdateProfileInDto {

	@NotNull
	@Min(value = 1)
	@Max(value = 31)
	private int day;

	@NotNull
	@Min(value = 1)
	@Max(value = 12)
	private int month;

	@NotNull
	private int year;

	@NotEmpty
	private String sex;

	@NotEmpty
	private String city;

	@NotEmpty
	private String description;

	public UpdateProfileInDto() {
		super();
	}

	public UpdateProfileInDto(int day, int month, int year, String sex, String city, String description) {
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

}
