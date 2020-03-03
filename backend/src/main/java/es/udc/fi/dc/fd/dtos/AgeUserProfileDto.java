package es.udc.fi.dc.fd.dtos;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import java.io.Serializable;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class AgeUserProfileDto implements Serializable {

  @NotNull
  @Min(value = 18)
  private int age;

  @JsonUnwrapped
  private AgelessUserProfileDto agelessFields;

  public AgeUserProfileDto() {
    super();
  }

  /**
   * Default constructor of an age-based user profile dto.
   *
   * @param age           The age of the user
   * @param agelessFields The fields of the user that did not depend on the age
   */
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
    return age == other.age;
  }

  @Override
  public String toString() {
    return "DateUserProfileDto [age=" + age + ", agelessFields=" + getAgelessFields() + "]";
  }

}
