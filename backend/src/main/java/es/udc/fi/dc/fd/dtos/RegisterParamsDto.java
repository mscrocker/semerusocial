package es.udc.fi.dc.fd.dtos;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

public class RegisterParamsDto {

	@JsonUnwrapped
	LoginParamsDto loginParams;

	@JsonUnwrapped
	DateUserProfileDto profileData;


	public RegisterParamsDto() {
		super();
	}

	public RegisterParamsDto(LoginParamsDto loginParams, DateUserProfileDto profileData) {
		super();
		this.loginParams = loginParams;
		this.profileData = profileData;
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
		result = prime * result + ((loginParams == null) ? 0 : loginParams.hashCode());
		result = prime * result + ((profileData == null) ? 0 : profileData.hashCode());
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
		if (loginParams == null) {
			if (other.loginParams != null) {
				return false;
			}
		} else if (!loginParams.equals(other.loginParams)) {
			return false;
		}
		if (profileData == null) {
			if (other.profileData != null) {
				return false;
			}
		} else if (!profileData.equals(other.profileData)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "RegisterParamsDto [loginParams=" + loginParams + ", profileData=" + profileData + "]";
	}

}
