package es.udc.fi.dc.fd.dtos;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

public class RegisterParamsDto {

	@NotEmpty
	private String userName;

	@NotEmpty
	private String password;

	@NotNull
	private Long age;

	@NotEmpty
	private String sex;

	@NotEmpty
	private String city;

	public RegisterParamsDto() {
		super();
	}

	public RegisterParamsDto(String userName, String password, Long age, String sex, String city) {
		setUserName(userName);
		setPassword(password);
		setAge(age);
		setSex(sex);
		setCity(city);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RegisterParamsDto other = (RegisterParamsDto) obj;
		if (age == null) {
			if (other.age != null)
				return false;
		} else if (!age.equals(other.age))
			return false;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (sex == null) {
			if (other.sex != null)
				return false;
		} else if (!sex.equals(other.sex))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}

	public Long getAge() {
		return age;
	}

	public String getCity() {
		return city;
	}

	public String getPassword() {
		return password;
	}

	public String getSex() {
		return sex;
	}

	public String getUserName() {
		return userName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((age == null) ? 0 : age.hashCode());
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((sex == null) ? 0 : sex.hashCode());
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
		return result;
	}

	public void setAge(Long age) {
		this.age = checkNotNull(age, "Received a null pointer as age in UserImpl");
	}

	public void setCity(String city) {
		this.city = checkNotNull(city, "Received a null pointer as city in UserImpl");
	}

	public void setPassword(String password) {
		this.password = checkNotNull(password, "Received a null pointer as password in UserImpl");
	}

	public void setSex(String sex) {
		this.sex = checkNotNull(sex, "Received a null pointer as sex in UserImpl");
	}

	public void setUserName(String userName) {
		this.userName = checkNotNull(userName, "Received a null pointer as userName in UserImpl");
	}

	@Override
	public String toString() {
		return "UserImpl [userName=" + userName + ", password=" + password + ", age=" + age + ", sex=" + sex + ", city="
				+ city + "]";
	}

}
