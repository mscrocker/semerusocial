package es.udc.fi.dc.fd.model;

import java.io.Serializable;

import es.udc.fi.dc.fd.model.persistence.CityCriteriaId;


public interface CityCriteria extends Serializable {

	public CityCriteriaId getCityCriteriaId();

	public void setCityCriteriaId(final CityCriteriaId cityCriteriaId);

}
