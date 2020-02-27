package es.udc.fi.dc.fd.dtos;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

public class AgeUserProfileDto {

	@NotNull
	@Min(value = 18)
	private int age;

	@JsonUnwrapped
	private AgelessUserProfileDto agelessFields;

	public AgeUserProfileDto() {
		super();
	}

	public AgeUserProfileDto(int age, AgelessUserProfileDto agelessFields) {
		super();
		this.age = age;
		this.setAgelessFields(agelessFields);
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
		result = prime * result + ((getAgelessFields() == null) ? 0 : getAgelessFields().hashCode());
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
		if (getAgelessFields() == null) {
			if (other.getAgelessFields() != null) {
				return false;
			}
		} else if (!getAgelessFields().equals(other.getAgelessFields())) {
			return false;
		}
		if (age != other.age) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "DateUserProfileDto [age=" + age + ", agelessFields=" + getAgelessFields() + "]";
	}

}
