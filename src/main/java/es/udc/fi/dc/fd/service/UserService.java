package es.udc.fi.dc.fd.service;

import es.udc.fi.dc.fd.controller.exception.DuplicateInstanceException;
import es.udc.fi.dc.fd.controller.exception.IncorrectLoginException;
import es.udc.fi.dc.fd.controller.exception.InstanceNotFoundException;
import es.udc.fi.dc.fd.controller.exception.InvalidAgeException;
import es.udc.fi.dc.fd.controller.exception.InvalidDateException;
import es.udc.fi.dc.fd.controller.exception.InvalidRateException;
import es.udc.fi.dc.fd.controller.exception.ItsNotYourFriendException;
import es.udc.fi.dc.fd.controller.exception.NotRatedException;
import es.udc.fi.dc.fd.dtos.LoginParamsDto;
import es.udc.fi.dc.fd.model.persistence.SearchCriteria;
import es.udc.fi.dc.fd.model.persistence.UserImpl;

public interface UserService {

	public Long signUp(UserImpl user) throws DuplicateInstanceException, InvalidDateException;

	public UserImpl login(LoginParamsDto params) throws IncorrectLoginException;

	public UserImpl loginFromUserId(Long userId) throws InstanceNotFoundException;

	public void setSearchCriteria(Long userId, SearchCriteria criteria)
			throws InstanceNotFoundException, InvalidAgeException, InvalidRateException, NotRatedException;

	public SearchCriteria getSearchCriteria(Long userId) throws InstanceNotFoundException;

	public void updateProfile(Long userId, UserImpl user) throws InstanceNotFoundException, InvalidDateException;

	public double rateUser(int rate, Long userSubject, Long userObject)
			throws InstanceNotFoundException, InvalidRateException, ItsNotYourFriendException;

	public void updatePremium(Long userId, boolean premium) throws InstanceNotFoundException;
}
