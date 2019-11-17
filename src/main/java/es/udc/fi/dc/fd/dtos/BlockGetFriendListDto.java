package es.udc.fi.dc.fd.dtos;

import java.util.List;

public class BlockGetFriendListDto<T> {

	private List<T> friends;
	private boolean existMoreFriends;

	public BlockGetFriendListDto() {
		super();
	}
	
	public BlockGetFriendListDto(List<T> friends, boolean existMoreFriends) {
		super();
		this.friends = friends;
		this.existMoreFriends = existMoreFriends;
	}

	public List<T> getFriends() {
		return friends;
	}

	public void setFriends(List<T> friends) {
		this.friends = friends;
	}

	public boolean isExistMoreFriends() {
		return existMoreFriends;
	}

	public void setExistMoreFriends(boolean existMoreFriends) {
		this.existMoreFriends = existMoreFriends;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (existMoreFriends ? 1231 : 1237);
		result = prime * result + (friends == null ? 0 : friends.hashCode());
		return result;
	}

	@SuppressWarnings("rawtypes")
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
		final BlockGetFriendListDto other = (BlockGetFriendListDto) obj;
		if (existMoreFriends != other.existMoreFriends) {
			return false;
		}
		if (friends == null) {
			if (other.friends != null) {
				return false;
			}
		} else if (!friends.equals(other.friends)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "GetFriendListOutDto [friends=" + friends + ", existMoreFriends=" + existMoreFriends + "]";
	}

}
