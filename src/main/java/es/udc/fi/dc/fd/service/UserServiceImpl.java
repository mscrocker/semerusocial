package es.udc.fi.dc.fd.service;

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
import es.udc.fi.dc.fd.controller.exception.InvalidRateException;
import es.udc.fi.dc.fd.controller.exception.ItsNotYourFriendException;
import es.udc.fi.dc.fd.dtos.LoginParamsDto;
import es.udc.fi.dc.fd.model.persistence.CityCriteriaId;
import es.udc.fi.dc.fd.model.persistence.CityCriteriaImpl;
import es.udc.fi.dc.fd.model.persistence.MatchImpl;
import es.udc.fi.dc.fd.model.persistence.RateId;
import es.udc.fi.dc.fd.model.persistence.RateImpl;
import es.udc.fi.dc.fd.model.persistence.SearchCriteria;
import es.udc.fi.dc.fd.model.persistence.UserImpl;
import es.udc.fi.dc.fd.repository.CityCriteriaRepository;
import es.udc.fi.dc.fd.repository.MatchRepository;
import es.udc.fi.dc.fd.repository.RateRepository;
import es.udc.fi.dc.fd.repository.UserRepository;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PermissionChecker permissionChecker;
	@Autowired
	private CityCriteriaRepository cityCriteriaRepository;
	@Autowired
	private MatchRepository matchRepository;
	@Autowired
	private RateRepository rateRepository;

	@Override
	public Long signUp(UserImpl user) throws DuplicateInstanceException, InvalidDateException {

		if (getUserRepository().existsByUserName(user.getUserName())) {
			throw new DuplicateInstanceException("project.entities.user", user.getUserName());
		}

		if (user.getDate().isAfter(LocalDateTime.now().minusYears(3))) {
			throw new InvalidDateException(
					"Fecha de nacimiento minima: " + LocalDateTime.now().minusYears(3).toString());
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
	@Transactional(readOnly = true)
	public UserImpl loginFromUserId(Long userId) throws InstanceNotFoundException {
		return permissionChecker.checkUserByUserId(userId);
	}

	@Override
	public void setSearchCriteria(Long userId, SearchCriteria criteria)
			throws InstanceNotFoundException, InvalidAgeException, InvalidRateException {
		final UserImpl user = permissionChecker.checkUserByUserId(userId);

		if (criteria.getMinAge() < 18) {
			throw new InvalidAgeException("Age must be higher than 18 years, not " + criteria.getMinAge());
		}
		if (criteria.getMinAge() > criteria.getMaxAge()) {
			throw new InvalidAgeException(
					"MinAge must be lower than MaxAge : " + criteria.getMinAge() + " > " + criteria.getMaxAge());
		}

		// Borramos de la base de datos todos las ciudades que tenia el usuario
		final List<String> toDeleteList = getCityCriteriaRepository().findCitiesByUserId(userId);
		for (final String city : toDeleteList) {
			final CityCriteriaImpl cityCriteriaId = new CityCriteriaImpl(new CityCriteriaId(userId, city));
			getCityCriteriaRepository().delete(cityCriteriaId);
		}

		final List<String> cityList = criteria.getCity();

		// Para cada ciudad de la lista
		for (final String city : cityList) {
			// Creamos el par userId , ciudad
			final CityCriteriaId id = new CityCriteriaId(userId, city.toLowerCase());
			final CityCriteriaImpl cityCriteria = new CityCriteriaImpl(id);

			// guardamos la nueva ciudad
			getCityCriteriaRepository().save(cityCriteria);

		}


		// Cumplimos las condiciones para establecer minRateCriteria
		if (user.getRatingVotes() > 0 || user.isPremium()) {

			// Validamos criteriaMinRate
			if (criteria.getMinRate() < 1 || criteria.getMinRate() > 5) {
				throw new InvalidRateException(
						"Your minRating must be between 1 and 5");
			}

			// Si es premium puede poner el minRate que quiera
			// Y si no lo es tiene que ser minRate válido (<userRate+1)
			if (user.isPremium() || criteria.getMinRate() <= user.getRating() + 1) {
				user.setMinRateCriteria(criteria.getMinRate());
			} else {
				throw new InvalidRateException(
						"Your minRating must be lower than : " + user.getRating() + 1 + " and higher than 1");
			}

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

	@Override
	public SearchCriteria getSearchCriteria(Long userId) throws InstanceNotFoundException {

		final UserImpl user = permissionChecker.checkUserByUserId(userId);

		final List<String> cityList = getCityCriteriaRepository().findCitiesByUserId(userId);

		return new SearchCriteria(user.getCriteriaSex(), user.getCriteriaMinAge(), user.getCriteriaMaxAge(), cityList,
				user.getMinRateCriteria());

	}

	// Subject el user que vota y object el user que es votado
	@Override
	public double rateUser(int rate, Long userSubject, Long userObject)
			throws InstanceNotFoundException, InvalidRateException, ItsNotYourFriendException {
		// Comprobamos que ambos usuarios existen
		permissionChecker.checkUserExists(userSubject);
		permissionChecker.checkUserExists(userObject);

		if (rate < 1 || rate > 5) { // Comprobamos que el rate es correcto
			throw new InvalidRateException("Your rate was :" + rate + " but it must be between 1 > 5");
		}

		final Optional<MatchImpl> match = matchRepository.weAreFriends(userSubject, userObject);
		if (!match.isPresent()) { // Si no existe la relacion de amistad
			throw new ItsNotYourFriendException("The user " + userObject + " is not your friend");
		}

		final Optional<UserImpl> userOptional = userRepository.findById(userObject);
		if (userOptional.isPresent()) {// Si el usuario al que se vota existe

			final UserImpl user = userOptional.get();
			final double userRate = user.getRating();
			final long userVotes = user.getRatingVotes();
			double newRate;
			final RateImpl newRateImpl;

			final Optional<RateImpl> rateOptional = rateRepository.findById(new RateId(userSubject, userObject));

			if (!rateOptional.isPresent()) {// Si el usuario no habia votado añadimos una nueva fila
				newRateImpl = new RateImpl(new RateId(userSubject, userObject), rate);
				rateRepository.save(newRateImpl);

				newRate = (userVotes * userRate + rate) / (userVotes + 1); // Calculamos la nueva media
				user.setRating(newRate);
				user.setRatingVotes(userVotes + 1);

			} else {// Si el usuario ya habia votado
				newRateImpl = rateOptional.get();// Obtenemos la votacion del subject

				if (newRateImpl.getPoints() == rate) {// Si hace la misma votacion la media se queda igual
					return user.getRating();
				} else { // Si no , modificamos la fila y calculamos la nueva media
					final double totalVotes = user.getRating() * user.getRatingVotes();

					newRate = (totalVotes - newRateImpl.getPoints() + rate) / userVotes;// Calculamos la nueva media

					user.setRating(newRate);

					newRateImpl.setPoints(rate);
					rateRepository.save(newRateImpl);
				}
			}
			userRepository.save(user);

			return newRate;

		} else {// Si el usuario al que se vota no existe
			throw new InstanceNotFoundException("Not found userId :", userObject);
		}

	}

	@Override
	public void updatePremium(Long userId, boolean premium) throws InstanceNotFoundException {
		final UserImpl user = permissionChecker.checkUserByUserId(userId);

		if (user.isPremium() != premium) {
			user.setPremium(premium);
			userRepository.save(user);
		}

	}

}
