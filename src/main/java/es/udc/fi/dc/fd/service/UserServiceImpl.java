package es.udc.fi.dc.fd.service;

import static com.google.common.base.Preconditions.checkNotNull;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fi.dc.fd.controller.exception.DuplicateInstanceException;
import es.udc.fi.dc.fd.controller.exception.IncorrectLoginException;
import es.udc.fi.dc.fd.controller.exception.InstanceNotFoundException;
import es.udc.fi.dc.fd.controller.exception.InvalidDateException;
import es.udc.fi.dc.fd.dtos.LoginParamsDto;
import es.udc.fi.dc.fd.model.persistence.UserImpl;
import es.udc.fi.dc.fd.repository.UserRepository;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	private UserRepository userRepository;
	
	private PermissionChecker permissionChecker;

	@Autowired
	public UserServiceImpl(UserRepository userRepository, PermissionChecker permissionChecker){
		super();

		this.userRepository = checkNotNull(userRepository,
                "Received a null pointer as userRepository in UserServiceImpl");
		
		this.permissionChecker = checkNotNull(permissionChecker,
                "Received a null pointer as permissionChecker in UserServiceImpl");
	}

	@Override
	public Long signUp(UserImpl user) throws DuplicateInstanceException, InvalidDateException {
		
		if (getUserRepository().existsByUserName(user.getUserName()))
			throw new DuplicateInstanceException("project.entities.user", user.getUserName());
		
		if (user.getDate().isAfter(LocalDateTime.now().minusYears(3))) {
			throw new InvalidDateException("Fecha de nacimiento minima: "+LocalDateTime.now().minusYears(3).toString());
		}

		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		user.setPassword((passwordEncoder.encode(user.getPassword())));

		UserImpl userSaved = getUserRepository().save(user);

		return userSaved.getId();
	}
	
	@Override
	@Transactional(readOnly = true)
	public UserImpl login(LoginParamsDto params) throws IncorrectLoginException {
		String userName = params.getUserName();
		String password = params.getPassword();
		
		Optional<UserImpl> user = getUserRepository().findByUserName(userName);

		if (!user.isPresent()) {
			throw new IncorrectLoginException(userName, password);
		}
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

		if (!passwordEncoder.matches(password, user.get().getPassword())) {
			throw new IncorrectLoginException(userName, password);
		}

		return user.get();
	}
	
	@Override
	@Transactional(readOnly=true)
	public UserImpl loginFromUserId(Long userId) throws InstanceNotFoundException {
		return permissionChecker.checkUserByUserId(userId);
	}

	public UserRepository getUserRepository() {
		return userRepository;
	}

}
