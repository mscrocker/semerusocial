package es.udc.fi.dc.fd.dtos;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

public class RatedFriendDto {

	@JsonUnwrapped
	private UnratedFriendDto friendData;

	private int myRating;

	public RatedFriendDto() {
		super();
	}

	public RatedFriendDto(UnratedFriendDto friendData, int myRating) {
		super();
		this.friendData = friendData;
		this.myRating = myRating;
	}

	public UnratedFriendDto getFriendData() {
		return friendData;
	}

	public void setFriendData(UnratedFriendDto friendData) {
		this.friendData = friendData;
	}

	public int getMyRating() {
		return myRating;
	}

	public void setMyRating(int myRating) {
		this.myRating = myRating;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((friendData == null) ? 0 : friendData.hashCode());
		result = prime * result + myRating;
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
		RatedFriendDto other = (RatedFriendDto) obj;
		if (friendData == null) {
			if (other.friendData != null) {
				return false;
			}
		} else if (!friendData.equals(other.friendData)) {
			return false;
		}
		if (myRating != other.myRating) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "RatedFriendDto [friendData=" + friendData + ", myRating=" + myRating + "]";
	}

}
