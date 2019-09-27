package es.udc.fi.dc.fd.dtos;

public class UserDataDto {
	
	private int age;
	
	private String sex;
	
	private String city;
	
	public UserDataDto() {
		super();
	}

	public UserDataDto(int age, String sex, String city) {
		super();
		setAge(age);
		setSex(sex);
		setCity(city);
	}

	public int getAge() {
		return age;
	}
	
	public String getSex() {
		return sex;
	}
	
	public String getCity() {
		return city;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + age;
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((sex == null) ? 0 : sex.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserDataDto other = (UserDataDto) obj;
		if (age != other.age)
			return false;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (sex == null) {
			if (other.sex != null)
				return false;
		} else if (!sex.equals(other.sex))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UserDataDto [age=" + age + ", sex=" + sex + ", city=" + city + "]";
	}
	
}
