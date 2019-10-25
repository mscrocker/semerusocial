package es.udc.fi.dc.fd.dtos;

import java.io.Serializable;
import java.util.Objects;

public final class FriendDto implements Serializable {

	private static final long serialVersionUID = 1328776989450853492L;

	private Long id;

	private String userName;

	private int age;

	private String sex;

	private String city;

	private String description;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "FriendDto [id=" + id + ", userName=" + userName + ", age=" + age + ", sex=" + sex + ", city=" + city
				+ ", description=" + description + "]";
	}

	public FriendDto(Long id, String userName, int age, String sex, String city, String description) {
		super();
		this.id = id;
		this.userName = userName;
		this.age = age;
		this.sex = sex;
		this.city = city;
		this.description = description;
	}

	@Override
	public int hashCode() {
		return Objects.hash(age, city, description, id, sex, userName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FriendDto other = (FriendDto) obj;
		return age == other.age && Objects.equals(city, other.city) && Objects.equals(description, other.description)
				&& Objects.equals(id, other.id) && Objects.equals(sex, other.sex)
				&& Objects.equals(userName, other.userName);
	}

	public FriendDto() {
		super();
	}

}
