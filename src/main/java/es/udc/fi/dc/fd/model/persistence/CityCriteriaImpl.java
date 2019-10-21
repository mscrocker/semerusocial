package es.udc.fi.dc.fd.model.persistence;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import es.udc.fi.dc.fd.model.CityCriteria;

@Entity(name = "Cities")
@Table(name = "CitiesCriteria")
public class CityCriteriaImpl implements CityCriteria {

	@Transient
	private static final long serialVersionUID = 7236756751L;

	@EmbeddedId
	private CityCriteriaId cityCriteriaId;

	public CityCriteriaImpl(CityCriteriaId cityCriteriaId) {
		super();
		this.cityCriteriaId = cityCriteriaId;
	}
	
	public CityCriteriaImpl() {
		super();
	}

	@Override
	public CityCriteriaId getCityCriteriaId() {
		return cityCriteriaId;
	}
	@Override
	public void setCityCriteriaId(CityCriteriaId cityCriteriaId) {
		this.cityCriteriaId = cityCriteriaId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cityCriteriaId == null) ? 0 : cityCriteriaId.hashCode());
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
		CityCriteriaImpl other = (CityCriteriaImpl) obj;
		if (cityCriteriaId == null) {
			if (other.cityCriteriaId != null)
				return false;
		} else if (!cityCriteriaId.equals(other.cityCriteriaId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CityCriteriaImpl [cityCriteriaId=" + cityCriteriaId + "]";
	}
	
	

}
