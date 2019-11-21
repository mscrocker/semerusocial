package es.udc.fi.dc.fd.dtos;

import java.time.LocalDateTime;

public class UserDataDto {

	private LocalDateTime date;

	private int age;

	private String sex;

	private String city;

	private String description;

	private double rate;

	public UserDataDto() {
		super();
	}

	public UserDataDto(LocalDateTime date, int age, String sex, String city, String description, double rate) {
		super();
		this.date = date;
		this.age = age;
		this.sex = sex;
		this.city = city;
		this.description = description;
		this.rate = rate;
	}


	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
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

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + age;
		result = prime * result + (city == null ? 0 : city.hashCode());
		result = prime * result + (date == null ? 0 : date.hashCode());
		result = prime * result + (description == null ? 0 : description.hashCode());
		long temp;
		temp = Double.doubleToLongBits(rate);
		result = prime * result + (int) (temp ^ temp >>> 32);
		result = prime * result + (sex == null ? 0 : sex.hashCode());
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
		final UserDataDto other = (UserDataDto) obj;
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
		if (date == null) {
			if (other.date != null) {
				return false;
			}
		} else if (!date.equals(other.date)) {
			return false;
		}
		if (description == null) {
			if (other.description != null) {
				return false;
			}
		} else if (!description.equals(other.description)) {
			return false;
		}
		if (Double.doubleToLongBits(rate) != Double.doubleToLongBits(other.rate)) {
			return false;
		}
		if (sex == null) {
			if (other.sex != null) {
				return false;
			}
		} else if (!sex.equals(other.sex)) {
			return false;
		}
		return true;
	}


	@Override
	public String toString() {
		return "UserDataDto [date=" + date + ", age=" + age + ", sex=" + sex + ", city=" + city + ", description="
				+ description + ", rate=" + rate + "]";
	}


}
