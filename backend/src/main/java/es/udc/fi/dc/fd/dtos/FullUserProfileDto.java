package es.udc.fi.dc.fd.dtos;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

public class FullUserProfileDto {

	private double rating;

	private boolean premium;

	@JsonUnwrapped
	private DateUserProfileDto baseProfileData;


	public FullUserProfileDto(double rating, boolean premium, DateUserProfileDto baseProfileData) {
		super();
		this.rating = rating;
		this.premium = premium;
		this.baseProfileData = baseProfileData;
	}

	public FullUserProfileDto() {
		super();
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public boolean isPremium() {
		return premium;
	}

	public void setPremium(boolean premium) {
		this.premium = premium;
	}

	public DateUserProfileDto getBaseProfileData() {
		return baseProfileData;
	}

	public void setBaseProfileData(DateUserProfileDto baseProfileData) {
		this.baseProfileData = baseProfileData;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((baseProfileData == null) ? 0 : baseProfileData.hashCode());
		result = prime * result + (premium ? 1231 : 1237);
		result = prime * result + (int) rating;
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
		FullUserProfileDto other = (FullUserProfileDto) obj;
		if (baseProfileData == null) {
			if (other.baseProfileData != null) {
				return false;
			}
		} else if (!baseProfileData.equals(other.baseProfileData)) {
			return false;
		}
		if (premium != other.premium) {
			return false;
		}
		if (rating != other.rating) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "FullUserProfileDto [rating=" + rating + ", premium=" + premium + ", baseProfileData=" + baseProfileData
			+ "]";
	}


}
