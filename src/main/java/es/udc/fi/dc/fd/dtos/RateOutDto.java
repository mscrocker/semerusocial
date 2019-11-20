package es.udc.fi.dc.fd.dtos;

import java.io.Serializable;

public final class RateOutDto implements Serializable {

	private static final long serialVersionUID = 1328776989450853492L;


	private double rate;


	public RateOutDto() {
		super();
	}

	public RateOutDto(double rate) {
		super();
		this.rate = rate;
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
		long temp;
		temp = Double.doubleToLongBits(rate);
		result = prime * result + (int) (temp ^ temp >>> 32);
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
		final RateOutDto other = (RateOutDto) obj;
		if (Double.doubleToLongBits(rate) != Double.doubleToLongBits(other.rate)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "RateOutDto [rate=" + rate + "]";
	}


}
