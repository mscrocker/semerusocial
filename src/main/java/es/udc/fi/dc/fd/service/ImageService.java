package es.udc.fi.dc.fd.service;

import java.util.List;

import es.udc.fi.dc.fd.controller.exception.InstanceNotFoundException;
import es.udc.fi.dc.fd.controller.exception.InvalidImageException;
import es.udc.fi.dc.fd.model.persistence.ImageImpl;

public interface ImageService {

	public ImageImpl addImage(ImageImpl image, Long userId) throws InstanceNotFoundException;

	public ImageImpl editImage(ImageImpl image, Long imageId, Long userId)
			throws InstanceNotFoundException, InvalidImageException;

	public void removeImage(ImageImpl image, Long userId) throws InstanceNotFoundException, InvalidImageException;

	public List<ImageImpl> getImagesByUserId(Long userId) throws InstanceNotFoundException;

}
