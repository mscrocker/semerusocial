package es.udc.fi.dc.fd.dtos;

import es.udc.fi.dc.fd.model.SexCriteriaEnum;

public class SearchCriteriaDto {

	private SexCriteriaEnum sex;
	private int minAge;
	private int maxAge;

	public SearchCriteriaDto(String sex, int minAge, int maxAge) {
		super();
		this.sex = SexCriteriaEnum.fromCode(sex) ;
		this.minAge = minAge;
		this.maxAge = maxAge;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		return "SearchCriteriaDto [sex=" + sex + ", minAge=" + minAge + ", maxAge=" + maxAge + "]";
	}




}
