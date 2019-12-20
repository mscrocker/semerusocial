package es.udc.fi.dc.fd.model.persistence;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

@Embeddable
public class CityCriteriaId implements Serializable {

	@Transient
	private static final long serialVersionUID = 662827762820441L;

	private Long userId;
	
	private String city;
	

	public CityCriteriaId(Long userId, String city) {
		super();
		this.userId = userId;
		this.city = city;
	}


	public CityCriteriaId() {
		super();
	}


	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
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
		CityCriteriaId other = (CityCriteriaId) obj;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "CityCriteriaId [userId=" + userId + ", city=" + city + "]";
	}

}
