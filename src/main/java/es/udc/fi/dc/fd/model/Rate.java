package es.udc.fi.dc.fd.model;

import java.io.Serializable;

import es.udc.fi.dc.fd.model.persistence.RateId;

public interface Rate extends Serializable {

	public RateId getRateId();

	public int getPoints();

	public void setRateId(final RateId rateId);

	public void setPoints(final int points);

}
