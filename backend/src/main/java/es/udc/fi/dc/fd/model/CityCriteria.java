package es.udc.fi.dc.fd.model;

import es.udc.fi.dc.fd.model.persistence.CityCriteriaId;
import java.io.Serializable;


public interface CityCriteria extends Serializable {

  public CityCriteriaId getCityCriteriaId();

  public void setCityCriteriaId(final CityCriteriaId cityCriteriaId);

}
