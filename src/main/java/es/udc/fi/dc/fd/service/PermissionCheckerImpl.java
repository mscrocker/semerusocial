package es.udc.fi.dc.fd.service;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fi.dc.fd.controller.exception.InstanceNotFoundException;
import es.udc.fi.dc.fd.model.persistence.UserImpl;
import es.udc.fi.dc.fd.repository.UserRepository;

@Service
@Transactional(readOnly=true)
public class PermissionCheckerImpl implements PermissionChecker {
	
	private UserRepository userDao;
	
	@Autowired
	public PermissionCheckerImpl(UserRepository userDao){
		super();

		this.userDao = checkNotNull(userDao,
                "Received a null pointer as userDao in PermissionCheckerImpl");

	}
	
	@Override
	public void checkUserExists(Long userId) throws InstanceNotFoundException {
		
		if (!userDao.existsById(userId)) {
			throw new InstanceNotFoundException("project.entities.user", userId);
		}
		
	}
	
	@Override
	public UserImpl checkUserByUserId(Long userId) throws InstanceNotFoundException {
		checkUserExists(userId);
		UserImpl user = userDao.getOne(userId);
		return user;
	}

}
