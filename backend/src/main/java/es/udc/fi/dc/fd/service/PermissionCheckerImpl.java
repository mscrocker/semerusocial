package es.udc.fi.dc.fd.service;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fi.dc.fd.controller.exception.InstanceNotFoundException;
import es.udc.fi.dc.fd.model.persistence.UserImpl;
import es.udc.fi.dc.fd.repository.UserRepository;

@Service
@Transactional(readOnly=true)
public class PermissionCheckerImpl implements PermissionChecker {
	
	private UserRepository userRepository;
	
	@Autowired
	public PermissionCheckerImpl(UserRepository userRepository){
		super();

		this.userRepository = checkNotNull(userRepository,
                "Received a null pointer as userRepository in PermissionCheckerImpl");
	}
	
	@Override
	public void checkUserExists(Long userId) throws InstanceNotFoundException {
		
		if (!userRepository.existsById(userId)) {
			throw new InstanceNotFoundException("project.entities.user", userId);
		}
		
	}
	
	@Override
	public UserImpl checkUserByUserId(Long userId) throws InstanceNotFoundException {
		checkUserExists(userId);
		
		Optional<UserImpl> user = userRepository.findById(userId);
		if(!user.isPresent()) {
			throw new InstanceNotFoundException("user", userId);
		}
		return user.get();
	}

}
