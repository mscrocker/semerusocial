package es.udc.fi.dc.fd.dtos;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

public class AgeUserProfileDto {

	@NotNull
	@Min(value = 18)
	private int age;

	@JsonUnwrapped
	AgelessUserProfileDto agelessFields;

	public AgeUserProfileDto() {
		super();
	}

	public AgeUserProfileDto(int age, AgelessUserProfileDto agelessFields) {
		super();
		this.age = age;
		this.agelessFields = agelessFields;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
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
		result = prime * result + age;
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
		AgeUserProfileDto other = (AgeUserProfileDto) obj;
		if (agelessFields == null) {
			if (other.agelessFields != null) {
				return false;
			}
		} else if (!agelessFields.equals(other.agelessFields)) {
			return false;
		}
		if (age != other.age) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "DateUserProfileDto [age=" + age + ", agelessFields=" + agelessFields + "]";
	}

}
