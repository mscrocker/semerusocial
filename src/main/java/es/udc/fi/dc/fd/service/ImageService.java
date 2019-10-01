package es.udc.fi.dc.fd.service;

import es.udc.fi.dc.fd.controller.exception.InstanceNotFoundException;
import es.udc.fi.dc.fd.controller.exception.ItsNotYourImageException;
import es.udc.fi.dc.fd.model.persistence.ImageImpl;

public interface ImageService {

	public ImageImpl addImage(ImageImpl image, Long userId) 
			throws InstanceNotFoundException;

	public ImageImpl editImage(ImageImpl image, Long imageId, Long userId)
			throws InstanceNotFoundException, ItsNotYourImageException;

	public void removeImage(ImageImpl image, Long userId) 
			throws InstanceNotFoundException, ItsNotYourImageException;

	public Block<ImageImpl> getImagesByUserId(Long userId, int page) 
			throws InstanceNotFoundException;
	
	public BlockImageByUserId<ImageImpl> getImageByUserId(Long userId, Long imageId) 
			throws InstanceNotFoundException, ItsNotYourImageException;
	
	public Long getFirstImageIdByUserId(Long userId) 
			throws InstanceNotFoundException;

}
