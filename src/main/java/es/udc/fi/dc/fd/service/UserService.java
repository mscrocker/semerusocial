package es.udc.fi.dc.fd.service;

import es.udc.fi.dc.fd.controller.exception.DuplicateInstanceException;
import es.udc.fi.dc.fd.controller.exception.IncorrectLoginException;
import es.udc.fi.dc.fd.controller.exception.InstanceNotFoundException;
import es.udc.fi.dc.fd.dtos.LoginParamsDto;
import es.udc.fi.dc.fd.model.persistence.UserImpl;

public interface UserService {

	public Long signUp(UserImpl user) 
			throws DuplicateInstanceException;

	public UserImpl login(LoginParamsDto params)  
			throws IncorrectLoginException;
	
	public UserImpl loginFromUserId(Long userId) 
			throws InstanceNotFoundException;

}
