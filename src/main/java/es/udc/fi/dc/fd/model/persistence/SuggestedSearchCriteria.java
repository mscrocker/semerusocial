package es.udc.fi.dc.fd.model.persistence;

public class SuggestedSearchCriteria {

	private int newMinAge;

	private int newMaxAge;

	private int newMinRate;

	private int usersYouWouldFind;

	public SuggestedSearchCriteria() {
		super();
	}

	public SuggestedSearchCriteria(int newMinAge, int newMaxAge, int newMinRate, int usersYouWouldFind) {
		super();
		this.newMinAge = newMinAge;
		this.newMaxAge = newMaxAge;
		this.newMinRate = newMinRate;
		this.usersYouWouldFind = usersYouWouldFind;
	}

	public int getNewMinAge() {
		return newMinAge;
	}

	public void setNewMinAge(int newMinAge) {
		this.newMinAge = newMinAge;
	}

	public int getNewMaxAge() {
		return newMaxAge;
	}

	public void setNewMaxAge(int newMaxAge) {
		this.newMaxAge = newMaxAge;
	}

	public int getNewMinRate() {
		return newMinRate;
	}

	public void setNewMinRate(int newMinRate) {
		this.newMinRate = newMinRate;
	}

	public int getUsersYouWouldFind() {
		return usersYouWouldFind;
	}

	public void setUsersYouWouldFind(int usersYouWouldFind) {
		this.usersYouWouldFind = usersYouWouldFind;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + usersYouWouldFind;
		result = prime * result + newMaxAge;
		result = prime * result + newMinAge;
		result = prime * result + newMinRate;
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
		final SuggestedSearchCriteria other = (SuggestedSearchCriteria) obj;
		if (usersYouWouldFind != other.usersYouWouldFind) {
			return false;
		}
		if (newMaxAge != other.newMaxAge) {
			return false;
		}
		if (newMinAge != other.newMinAge) {
			return false;
		}
		if (newMinRate != other.newMinRate) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "SuggestedSearchCriteria [newMinAge=" + newMinAge + ", newMaxAge=" + newMaxAge + ", newMinRate="
				+ newMinRate + ", usersYouWouldFind=" + usersYouWouldFind + "]";
	}

}