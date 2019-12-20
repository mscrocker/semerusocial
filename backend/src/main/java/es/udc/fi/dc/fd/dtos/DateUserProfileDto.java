package es.udc.fi.dc.fd.dtos;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonUnwrapped;


public class DateUserProfileDto {

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

	@JsonUnwrapped
	AgelessUserProfileDto agelessFields;

	public DateUserProfileDto() {
		super();
	}

	public DateUserProfileDto(int day, int month, int year, AgelessUserProfileDto agelessFields) {
		super();
		this.day = day;
		this.month = month;
		this.year = year;
		this.agelessFields = agelessFields;
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

	public AgelessUserProfileDto getAgelessFields() {
		return agelessFields;
	}

	public void setAgelessFields(AgelessUserProfileDto agelessFields) {
		this.agelessFields = agelessFields;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((agelessFields == null) ? 0 : agelessFields.hashCode());
		result = prime * result + day;
		result = prime * result + month;
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
		DateUserProfileDto other = (DateUserProfileDto) obj;
		if (agelessFields == null) {
			if (other.agelessFields != null)
				return false;
		} else if (!agelessFields.equals(other.agelessFields))
			return false;
		if (day != other.day)
			return false;
		if (month != other.month)
			return false;
		if (year != other.year)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DateUserProfileDto [day=" + day + ", month=" + month + ", year=" + year + ", agelessFields="
				+ agelessFields + "]";
	}

	

	
}
