package es.udc.fi.dc.fd.service;

import java.util.List;

public class BlockFriendList<T> {

	private List<T> friends;
	private boolean existMoreFriends;

	public BlockFriendList() {
		super();
	}

	public BlockFriendList(List<T> friends, boolean existMoreFriends) {
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

	public boolean getExistMoreFriends() {
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
		final BlockFriendList other = (BlockFriendList) obj;
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
		return "BlockFriendList [friends=" + friends + ", existMoreFriends=" + existMoreFriends + "]";
	}

}
