package es.udc.fi.dc.fd.dtos;

public class GetFriendListOutDto {

	private Long id;

	private String userName;

	private int age;

	private String sex;

	private String city;

	private int myRating;

	public GetFriendListOutDto() {
		super();
	}

	public GetFriendListOutDto(Long id, String userName, int age, String sex, String city, int myRating) {
		super();
		this.id = id;
		this.userName = userName;
		this.age = age;
		this.sex = sex;
		this.city = city;
		this.myRating = myRating;
	}

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

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
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
		result = prime * result + age;
		result = prime * result + (city == null ? 0 : city.hashCode());
		result = prime * result + (id == null ? 0 : id.hashCode());
		result = prime * result + myRating;
		result = prime * result + (sex == null ? 0 : sex.hashCode());
		result = prime * result + (userName == null ? 0 : userName.hashCode());
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
		final GetFriendListOutDto other = (GetFriendListOutDto) obj;
		if (age != other.age) {
			return false;
		}
		if (city == null) {
			if (other.city != null) {
				return false;
			}
		} else if (!city.equals(other.city)) {
			return false;
		}
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (myRating != other.myRating) {
			return false;
		}
		if (sex == null) {
			if (other.sex != null) {
				return false;
			}
		} else if (!sex.equals(other.sex)) {
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
		return "GetFriendListOutDto [id=" + id + ", userName=" + userName + ", age=" + age + ", sex=" + sex + ", city="
				+ city + ", myRating=" + myRating + "]";
	}

}
