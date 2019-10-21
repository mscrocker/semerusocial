package es.udc.fi.dc.fd.dtos;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

public class RegisterParamsDto {

	@NotEmpty
	private String userName;

	@NotEmpty
	private String password;

	@NotNull
	@Min(value = 1)
	@Max(value = 31)
	private int day;

	@NotNull
	@Min(value = 1)
	@Max(value = 12)
	private int month;

	@NotNull
	private int year;

	@NotEmpty
	private String sex;

	@NotEmpty
	private String city;

	@NotEmpty
	private String description;

	public RegisterParamsDto() {
		super();
	}

	public RegisterParamsDto(String userName, String password, int day, int month, int year, String sex, String city,
			String description) {
		super();
		this.userName = userName;
		this.password = password;
		this.day = day;
		this.month = month;
		this.year = year;
		this.sex = sex;
		this.city = city;
		this.description = description;
	}

	public String getCity() {
		return city;
	}

	public int getDay() {
		return day;
	}

	public int getMonth() {
		return month;
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

	public int getYear() {
		return year;
	}

	public String getDescription() {
		return description;
	}

	public void setCity(String city) {
		this.city = checkNotNull(city, "Received a null pointer as city in UserImpl");
	}

	public void setDay(int day) {
		this.day = day;
	}

	public void setMonth(int month) {
		this.month = month;
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

	public void setYear(int year) {
		this.year = year;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (city == null ? 0 : city.hashCode());
		result = prime * result + day;
		result = prime * result + (description == null ? 0 : description.hashCode());
		result = prime * result + month;
		result = prime * result + (password == null ? 0 : password.hashCode());
		result = prime * result + (sex == null ? 0 : sex.hashCode());
		result = prime * result + (userName == null ? 0 : userName.hashCode());
		result = prime * result + year;
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
		final RegisterParamsDto other = (RegisterParamsDto) obj;
		if (city == null) {
			if (other.city != null) {
				return false;
			}
		} else if (!city.equals(other.city)) {
			return false;
		}
		if (day != other.day) {
			return false;
		}
		if (description == null) {
			if (other.description != null) {
				return false;
			}
		} else if (!description.equals(other.description)) {
			return false;
		}
		if (month != other.month) {
			return false;
		}
		if (password == null) {
			if (other.password != null) {
				return false;
			}
		} else if (!password.equals(other.password)) {
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
		if (year != other.year) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "RegisterParamsDto [userName=" + userName + ", password=" + password + ", day=" + day + ", month="
				+ month + ", year=" + year + ", sex=" + sex + ", city=" + city + ", description=" + description + "]";
	}

}
