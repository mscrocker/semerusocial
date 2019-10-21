package es.udc.fi.dc.fd.dtos;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import es.udc.fi.dc.fd.model.SexCriteriaEnum;

public class SearchCriteriaDto {
	@NotNull

	private SexCriteriaEnum sex;
	@NotNull
	@Min(18)
	private int minAge;

	private int maxAge;

	@NotNull
	private List<String> city;

	public SearchCriteriaDto(String sex, int minAge, int maxAge, List<String> city) {
		super();
		this.sex = SexCriteriaEnum.fromCode(sex) ;
		this.minAge = minAge;
		this.maxAge = maxAge;
		this.city = city;
	}

	public SearchCriteriaDto() {
		super();
	}

	public SexCriteriaEnum getSex() {
		return sex;
	}
	public void setSex(SexCriteriaEnum sex) {
		this.sex = sex;
	}
	public int getMinAge() {
		return minAge;
	}
	public void setMinAge(int minAge) {
		this.minAge = minAge;
	}
	public int getMaxAge() {
		return maxAge;
	}
	public void setMaxAge(int maxAge) {
		this.maxAge = maxAge;
	}

	public List<String> getCity() {
		return city;
	}

	public void setCity(List<String> city) {
		this.city = city;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (city == null ? 0 : city.hashCode());
		result = prime * result + maxAge;
		result = prime * result + minAge;
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
		final SearchCriteriaDto other = (SearchCriteriaDto) obj;
		if (city == null) {
			if (other.city != null) {
				return false;
			}
		} else if (!city.equals(other.city)) {
			return false;
		}
		if (maxAge != other.maxAge) {
			return false;
		}
		if (minAge != other.minAge) {
			return false;
		}
		if (sex != other.sex) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "SearchCriteriaDto [sex=" + sex + ", minAge=" + minAge + ", maxAge=" + maxAge + ", city=" + city + "]";
	}





}
