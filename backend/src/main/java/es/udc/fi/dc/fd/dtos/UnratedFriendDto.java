package es.udc.fi.dc.fd.dtos;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

public final class UnratedFriendDto implements Serializable {

	private static final long serialVersionUID = 1328776989450853493L;

	private Long id;

	private String userName;

	@JsonUnwrapped
	private AgeUserProfileDto userData;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public AgeUserProfileDto getUserData() {
		return userData;
	}

	public void setUserData(AgeUserProfileDto userData) {
		this.userData = userData;
	}

	public UnratedFriendDto() {
		super();
	}

	public UnratedFriendDto(Long id, String userName, AgeUserProfileDto userData) {
		super();
		this.id = id;
		this.userName = userName;
		this.userData = userData;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((userData == null) ? 0 : userData.hashCode());
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
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
		UnratedFriendDto other = (UnratedFriendDto) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (userData == null) {
			if (other.userData != null) {
				return false;
			}
		} else if (!userData.equals(other.userData)) {
			return false;
		}
		if (userName == null) {
			if (other.userName != null) {
				return false;
			}
		} else if (!userName.equals(other.userName)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "FriendDto [id=" + id + ", userName=" + userName + ", userData=" + userData + "]";
	}


}
