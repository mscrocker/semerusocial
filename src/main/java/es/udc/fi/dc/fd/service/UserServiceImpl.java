package es.udc.fi.dc.fd.service;

import static com.google.common.base.Preconditions.checkNotNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fi.dc.fd.controller.exception.DuplicateInstanceException;
import es.udc.fi.dc.fd.controller.exception.IncorrectLoginException;
import es.udc.fi.dc.fd.controller.exception.InstanceNotFoundException;
import es.udc.fi.dc.fd.controller.exception.InvalidAgeException;
import es.udc.fi.dc.fd.controller.exception.InvalidDateException;
import es.udc.fi.dc.fd.dtos.LoginParamsDto;
import es.udc.fi.dc.fd.dtos.SearchCriteriaDto;
import es.udc.fi.dc.fd.model.persistence.CityCriteriaId;
import es.udc.fi.dc.fd.model.persistence.CityCriteriaImpl;
import es.udc.fi.dc.fd.model.persistence.UserImpl;
import es.udc.fi.dc.fd.repository.CityCriteriaRepository;
import es.udc.fi.dc.fd.repository.UserRepository;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	private final PermissionChecker permissionChecker;

	private final CityCriteriaRepository cityCriteriaRepository;

	@Autowired
	public UserServiceImpl(UserRepository userRepository, PermissionChecker permissionChecker , CityCriteriaRepository cityCriteriaRepository) {
		super();

		this.userRepository = checkNotNull(userRepository,
				"Received a null pointer as userRepository in UserServiceImpl");

		this.permissionChecker = checkNotNull(permissionChecker,
				"Received a null pointer as permissionChecker in UserServiceImpl");

		this.cityCriteriaRepository = checkNotNull(cityCriteriaRepository,
				"Received a null pointer as cityCriteriaRepository in UserServiceImpl");
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
	public void setSearchCriteria(Long userId, SearchCriteriaDto criteria)
			throws InstanceNotFoundException, InvalidAgeException {
		final UserImpl user = permissionChecker.checkUserByUserId(userId);

		if (criteria.getMinAge() < 18) {
			throw new InvalidAgeException("Age must be higher than 18 years, not " + criteria.getMinAge());
		}
		if (criteria.getMinAge() > criteria.getMaxAge() ) {
			throw new InvalidAgeException("MinAge must be lower than MaxAge : " + criteria.getMinAge() +" > " + criteria.getMaxAge() );
		}

		//Borramos de la base de datos todos las ciudades que tenia el usuario
		final List <String> toDeleteList = getCityCriteriaRepository().findCitiesByUserId(userId);
		for (final String city : toDeleteList) {
			final CityCriteriaImpl cityCriteriaId  = new CityCriteriaImpl(new CityCriteriaId(userId, city));
			getCityCriteriaRepository().delete(cityCriteriaId);
		}

		final List<String> cityList = criteria.getCity();

		//Para cada ciudad de la lista
		for (final String city : cityList) {
			//Creamos el par userId , ciudad
			final CityCriteriaId id = new CityCriteriaId(userId, city.toLowerCase());
			final CityCriteriaImpl cityCriteria = new CityCriteriaImpl(id);

			//guardamos la nueva ciudad
			getCityCriteriaRepository().save(cityCriteria);

		}

		user.setCriteriaSex(criteria.getSex());
		user.setCriteriaMaxAge(criteria.getMaxAge());
		user.setCriteriaMinAge(criteria.getMinAge());

		getUserRepository().save(user);
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

	public CityCriteriaRepository getCityCriteriaRepository() {
		return cityCriteriaRepository;
	}

}
