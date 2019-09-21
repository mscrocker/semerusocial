package es.udc.fi.dc.fd.service;

import es.udc.fi.dc.fd.controller.exception.InstanceNotFoundException;
import es.udc.fi.dc.fd.model.persistence.ImageImpl;

public interface ImageService {
	
	public ImageImpl addImage(ImageImpl image, Long userId) throws InstanceNotFoundException;
	
	public void removeImage(ImageImpl image, Long userId) throws InstanceNotFoundException;
    
}
