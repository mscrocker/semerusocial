package es.udc.fi.dc.fd.dtos;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

public class RegisterParamsDto {

  @JsonUnwrapped
  private LoginParamsDto loginParams;

  @JsonUnwrapped
  private DateUserProfileDto profileData;


  public RegisterParamsDto() {
    super();
  }

  /**
   * Default constructor for the RegisterParamsDto.
   *
   * @param loginParams The login parameters
   * @param profileData The user profile data
   */
  public RegisterParamsDto(LoginParamsDto loginParams, DateUserProfileDto profileData) {
    super();
    this.setLoginParams(loginParams);
    this.setProfileData(profileData);
  }

  public LoginParamsDto getLoginParams() {
    return loginParams;
  }

  public void setLoginParams(LoginParamsDto loginParams) {
    this.loginParams = loginParams;
  }

  public DateUserProfileDto getProfileData() {
    return profileData;
  }

  public void setProfileData(DateUserProfileDto profileData) {
    this.profileData = profileData;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((getLoginParams() == null) ? 0 : getLoginParams().hashCode());
    result = prime * result + ((getProfileData() == null) ? 0 : getProfileData().hashCode());
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
    RegisterParamsDto other = (RegisterParamsDto) obj;
    if (getLoginParams() == null) {
      if (other.getLoginParams() != null) {
        return false;
      }
    } else if (!getLoginParams().equals(other.getLoginParams())) {
      return false;
    }
    if (getProfileData() == null) {
      return other.getProfileData() == null;
    } else {
      return getProfileData().equals(other.getProfileData());
    }
  }

  @Override
  public String toString() {
    return "RegisterParamsDto [loginParams=" + getLoginParams() + ", profileData="
        + getProfileData() + "]";
  }

}
