package es.udc.fi.dc.fd.model;

import java.io.Serializable;

import es.udc.fi.dc.fd.model.persistence.UserImpl;

public interface Image extends Serializable {

	public Long getImageId();

	public User getUser();

	public String getType();

	public byte[] getData();

	public void setImageId(final Long imageId);

	public void setUser(final UserImpl user);

	public void setData(final byte[] data);

	public void setType(final String imageType);

}
