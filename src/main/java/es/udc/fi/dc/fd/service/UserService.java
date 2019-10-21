package es.udc.fi.dc.fd.service;

import java.util.List;

import es.udc.fi.dc.fd.controller.exception.DuplicateInstanceException;
import es.udc.fi.dc.fd.controller.exception.IncorrectLoginException;
import es.udc.fi.dc.fd.controller.exception.InstanceNotFoundException;
import es.udc.fi.dc.fd.controller.exception.InvalidDateException;
import es.udc.fi.dc.fd.controller.exception.NotEnoughAgeException;
import es.udc.fi.dc.fd.controller.exception.TooMuchAgeException;
import es.udc.fi.dc.fd.dtos.LoginParamsDto;
import es.udc.fi.dc.fd.dtos.SearchCriteriaDto;
import es.udc.fi.dc.fd.model.persistence.UserImpl;

public interface UserService {

	public Long signUp(UserImpl user)
			throws DuplicateInstanceException, InvalidDateException;

	public UserImpl login(LoginParamsDto params)
			throws IncorrectLoginException;

	public UserImpl loginFromUserId(Long userId)
			throws InstanceNotFoundException;

	public List<String> setSearchCriteria(Long userId , SearchCriteriaDto criteria)
			throws InstanceNotFoundException, TooMuchAgeException, NotEnoughAgeException ;
}
