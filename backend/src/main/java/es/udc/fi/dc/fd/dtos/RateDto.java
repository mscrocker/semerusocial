package es.udc.fi.dc.fd.dtos;

import java.io.Serializable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public final class RateDto implements Serializable {

  private static final long serialVersionUID = 1328776989450853492L;

  @NotNull
  @Min(value = 1)
  @Max(value = 5)
  private int rate;

  @NotNull
  private Long userObject;

  public RateDto() {
    super();
  }

  /**
   * Default constructor for the DTO with the rating of an user.
   *
   * @param rate       The rating of the user
   * @param userObject The user being rated
   */
  public RateDto(int rate, Long userObject) {
    super();
    this.rate = rate;
    this.userObject = userObject;
  }

  public int getRate() {
    return rate;
  }

  public void setRate(int rate) {
    this.rate = rate;
  }

  public Long getUserObject() {
    return userObject;
  }

  public void setUserObject(Long userObject) {
    this.userObject = userObject;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + rate;
    result = prime * result + (userObject == null ? 0 : userObject.hashCode());
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
    final RateDto other = (RateDto) obj;
    if (rate != other.rate) {
      return false;
    }
    if (userObject == null) {
      if (other.userObject != null) {
        return false;
      }
    } else if (!userObject.equals(other.userObject)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "RateDto [rate=" + rate + ", userObject=" + userObject + "]";
  }


}
