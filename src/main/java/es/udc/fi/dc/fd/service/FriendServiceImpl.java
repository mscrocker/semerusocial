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
		RequestId inverseRequestId = new RequestId(object, subject);
		Optional<RequestImpl> inverse = requestRepository.findById(inverseRequestId);
		// If it exists it means we now have a match, we need to also delete the inverse
		// request
		if (inverse.isPresent()) {

			// There's a db requirement to sort the ids
			Long firstId = Math.min(object, subject);
			Long secondId = subject.equals(firstId) ? object : subject;
			MatchImpl match = new MatchImpl(new MatchId(firstId, secondId), LocalDateTime.now());
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
		Optional<UserImpl> subjectUserOpt = userRepository.findById(subject);
		Optional<UserImpl> objectUserOpt = userRepository.findById(object);

		if (subjectUserOpt.isEmpty())
			throw new InstanceNotFoundException("Subject User Doesn't Exist", subject);

		if (objectUserOpt.isEmpty())
			throw new InstanceNotFoundException("Object User Doesn't Exist", object);

		UserImpl subjectUser = subjectUserOpt.get();

		UserImpl objectUser = objectUserOpt.get();

		Optional<RejectedImpl> optRejected = rejectedRepository.findById(new RejectedId(subject, object));
		if (optRejected.isPresent())
			throw new AlreadyRejectedException("Object user was already rejected", object);

		int objectAge = Period.between(objectUser.getDate().toLocalDate(), LocalDate.now()).getYears();

		if (objectAge < subjectUser.getCriteriaMinAge() || objectAge > subjectUser.getCriteriaMaxAge())
			throw new InvalidRecommendationException("ObjectUser doesn't fit subject requirements", objectUser);

		if (!subjectUser.getCriteriaSex().equals(SexCriteriaEnum.ANY)) {
			if (!subjectUser.getCriteriaSex().toString().equals(objectUser.getSex()))
				throw new InvalidRecommendationException("ObjectUser doesn't fit subject requirements", objectUser);
		}
		// TODO City Criteria
	}

}
