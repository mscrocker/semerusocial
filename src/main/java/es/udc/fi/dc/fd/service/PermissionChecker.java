package es.udc.fi.dc.fd.service;

import es.udc.fi.dc.fd.controller.exception.InstanceNotFoundException;
import es.udc.fi.dc.fd.model.persistence.UserImpl;

public interface PermissionChecker {
	
	public void checkUserExists(int userId) throws InstanceNotFoundException;
	
	public UserImpl checkUser(int userId) throws InstanceNotFoundException;
	
}
