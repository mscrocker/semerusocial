package es.udc.fi.dc.fd.dtos;

import java.io.Serializable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public final class RateDto implements Serializable {

	private static final long serialVersionUID = 1328776989450853492L;

	@NotNull
	@Min(value = 1)
	@Max(value = 5)
	private int rate;

	@NotNull
	private Long userSubject;

	@NotNull
	private Long userObject;

	public RateDto() {
		super();
	}

	public RateDto(int rate, Long userSubject, Long userObject) {
		super();
		this.rate = rate;
		this.userSubject = userSubject;
		this.userObject = userObject;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public Long getUserSubject() {
		return userSubject;
	}

	public void setUserSubject(Long userSubject) {
		this.userSubject = userSubject;
	}

	public Long getUserObject() {
		return userObject;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + rate;
		result = prime * result + (int) (userObject ^ userObject >>> 32);
		result = prime * result + (int) (userSubject ^ userSubject >>> 32);
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
		final RateDto other = (RateDto) obj;
		if (rate != other.rate) {
			return false;
		}
		if (userObject != other.userObject) {
			return false;
		}
		if (userSubject != other.userSubject) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "RateDto [rate=" + rate + ", userSubject=" + userSubject + ", userObject=" + userObject + "]";
	}

}
