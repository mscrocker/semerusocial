package es.udc.fi.dc.fd.service;

import es.udc.fi.dc.fd.controller.exception.InstanceNotFoundException;
import es.udc.fi.dc.fd.model.Image;
import es.udc.fi.dc.fd.model.persistence.ImageImpl;

public interface ImageService {
	
	public Image addImage(ImageImpl image, Long userId) throws InstanceNotFoundException;
	
	public void removeImage(ImageImpl image, Long userId) throws InstanceNotFoundException;
    
}
