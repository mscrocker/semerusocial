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

	private final UserRepository userRepository;

	private final PermissionChecker permissionChecker;

	@Autowired
	public UserServiceImpl(UserRepository userRepository, PermissionChecker permissionChecker) {
		super();

		this.userRepository = checkNotNull(userRepository,
				"Received a null pointer as userRepository in UserServiceImpl");

		this.permissionChecker = checkNotNull(permissionChecker,
				"Received a null pointer as permissionChecker in UserServiceImpl");
	}

	@Override
	public Long signUp(UserImpl user) throws DuplicateInstanceException, InvalidDateException {

		if (getUserRepository().existsByUserName(user.getUserName())) {
			throw new DuplicateInstanceException("project.entities.user", user.getUserName());
		}

		if (user.getDate().isAfter(LocalDateTime.now().minusYears(3))) {
			throw new InvalidDateException("Fecha de nacimiento minima: "+LocalDateTime.now().minusYears(3).toString());
		}

		final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		user.setPassword(passwordEncoder.encode(user.getPassword()));

		final UserImpl userSaved = getUserRepository().save(user);

		return userSaved.getId();
	}

	@Override
	@Transactional(readOnly = true)
	public UserImpl login(LoginParamsDto params) throws IncorrectLoginException {
		final String userName = params.getUserName();
		final String password = params.getPassword();

		final Optional<UserImpl> user = getUserRepository().findByUserName(userName);

		if (!user.isPresent()) {
			throw new IncorrectLoginException(userName, password);
		}
		final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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

	@Override
	public void updateProfile(Long userId, UserImpl user) throws InstanceNotFoundException, InvalidDateException {
		final Optional<UserImpl> userFound = getUserRepository().findById(userId);

		if (!userFound.isPresent()) {
			throw new InstanceNotFoundException("User with userId=" + userId + " doesn't exist", userFound);
		}

		if (user.getDate().isAfter(LocalDateTime.now().minusYears(3))) {
			throw new InvalidDateException(
					"Fecha de nacimiento minima: " + LocalDateTime.now().minusYears(3).toString());
		}

		userFound.get().setDate(user.getDate());
		userFound.get().setSex(user.getSex());
		userFound.get().setCity(user.getCity());
		userFound.get().setDescription(user.getDescription());

		getUserRepository().save(userFound.get());
	}

	public UserRepository getUserRepository() {
		return userRepository;
	}

}
