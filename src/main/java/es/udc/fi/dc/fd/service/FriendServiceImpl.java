package es.udc.fi.dc.fd.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fi.dc.fd.controller.exception.AlreadyRejectedException;
import es.udc.fi.dc.fd.controller.exception.InstanceNotFoundException;
import es.udc.fi.dc.fd.controller.exception.InvalidRecommendationException;
import es.udc.fi.dc.fd.dtos.FriendDto;
import es.udc.fi.dc.fd.dtos.SearchCriteriaDto;
import es.udc.fi.dc.fd.model.SexCriteriaEnum;
import es.udc.fi.dc.fd.model.persistence.MatchId;
import es.udc.fi.dc.fd.model.persistence.MatchImpl;
import es.udc.fi.dc.fd.model.persistence.RejectedId;
import es.udc.fi.dc.fd.model.persistence.RejectedImpl;
import es.udc.fi.dc.fd.model.persistence.RequestId;
import es.udc.fi.dc.fd.model.persistence.RequestImpl;
import es.udc.fi.dc.fd.model.persistence.UserImpl;
import es.udc.fi.dc.fd.repository.MatchRepository;
import es.udc.fi.dc.fd.repository.RejectedRepository;
import es.udc.fi.dc.fd.repository.RequestRepository;
import es.udc.fi.dc.fd.repository.UserRepository;

@Service
@Transactional
public class FriendServiceImpl implements FriendService {

	@Autowired
	RequestRepository requestRepository;

	@Autowired
	RejectedRepository rejectedRepository;

	@Autowired
	MatchRepository matchRepository;

	@Autowired
	UserRepository userRepository;

	@Override
	public void acceptRecommendation(Long subject, Long object)
			throws InstanceNotFoundException, InvalidRecommendationException, AlreadyRejectedException {
		validateRecommendation(subject, object);

		// Check whether the request done from the other user exists
		final RequestId inverseRequestId = new RequestId(object, subject);
		final Optional<RequestImpl> inverse = requestRepository.findById(inverseRequestId);
		// If it exists it means we now have a match, we need to also delete the inverse
		// request
		if (inverse.isPresent()) {

			// There's a db requirement to sort the ids
			final Long firstId = Math.min(object, subject);
			final Long secondId = subject.equals(firstId) ? object : subject;
			final MatchImpl match = new MatchImpl(new MatchId(firstId, secondId), LocalDateTime.now());
			matchRepository.save(match);
			requestRepository.delete(inverse.get());
		}
		// If it doesnt exist it means its the only existent request
		else {
			requestRepository.save(new RequestImpl(new RequestId(subject, object), LocalDateTime.now()));
		}
	}

	@Override
	public void rejectRecommendation(Long subject, Long object)
			throws InstanceNotFoundException, InvalidRecommendationException, AlreadyRejectedException {
		validateRecommendation(subject, object);
		// TODO WE can safely delete the invert request if it were to exist because it
		// doesnt do anything anymore
		rejectedRepository.save(new RejectedImpl(new RejectedId(subject, object), LocalDateTime.now()));

	}

	private void validateRecommendation(Long subject, Long object)
			throws InstanceNotFoundException, InvalidRecommendationException, AlreadyRejectedException {
		final Optional<UserImpl> subjectUserOpt = userRepository.findById(subject);
		final Optional<UserImpl> objectUserOpt = userRepository.findById(object);

		if (subjectUserOpt.isEmpty()) {
			throw new InstanceNotFoundException("Subject User Doesn't Exist", subject);
		}

		if (objectUserOpt.isEmpty()) {
			throw new InstanceNotFoundException("Object User Doesn't Exist", object);
		}

		final UserImpl subjectUser = subjectUserOpt.get();

		final UserImpl objectUser = objectUserOpt.get();

		final Optional<RejectedImpl> optRejected = rejectedRepository.findById(new RejectedId(subject, object));
		if (optRejected.isPresent()) {
			throw new AlreadyRejectedException("Object user was already rejected", object);
		}

		final int objectAge = Period.between(objectUser.getDate().toLocalDate(), LocalDate.now()).getYears();

		if (objectAge < subjectUser.getCriteriaMinAge() || objectAge > subjectUser.getCriteriaMaxAge()) {
			throw new InvalidRecommendationException("ObjectUser doesn't fit subject requirements", objectUser);
		}

		if (!subjectUser.getCriteriaSex().equals(SexCriteriaEnum.ANY)) {
			if (!subjectUser.getCriteriaSex().toString().equals(objectUser.getSex())) {
				throw new InvalidRecommendationException("ObjectUser doesn't fit subject requirements", objectUser);
			}
		}
		// TODO City Criteria
	}

	@Override
	public Optional<FriendDto> suggestFriend(Long userId) throws InstanceNotFoundException {

		if (userId == null) {
			throw new InstanceNotFoundException("userId can not be null", userId);
		}
		if (!userRepository.existsById(userId)) {
			throw new InstanceNotFoundException("User does not exists", userId);
		}

		// TODO -> LLAMAR AL SERVICIO PARA OBTENER LA CRITERIA DEL USUARIO
		final SearchCriteriaDto criteriaMock = new SearchCriteriaDto();
		criteriaMock.setMaxAge(99);
		criteriaMock.setMinAge(18);
		criteriaMock.setSex(SexCriteriaEnum.FEMALE);

		final Optional<UserImpl> userSuggested = userRepository.findByCriteria(criteriaMock, userId);

		return userImplToFriendDto(userSuggested);
	}

	private Optional<FriendDto> userImplToFriendDto(Optional<UserImpl> user) {

		Optional<FriendDto> friend = Optional.empty();
		if (!user.isEmpty()) {
			final LocalDateTime today = LocalDateTime.now();
			final Period period = Period.between(user.get().getDate().toLocalDate(), today.toLocalDate());

			final FriendDto friendDto = new FriendDto(user.get().getUserName(), period.getYears(), user.get().getSex(),
					user.get().getCity(), user.get().getDescription());
			friend = Optional.of(friendDto);
		}
		return friend;
	}
}
