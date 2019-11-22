package es.udc.fi.dc.fd.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fi.dc.fd.controller.exception.AlreadyAceptedException;
import es.udc.fi.dc.fd.controller.exception.AlreadyBlockedException;
import es.udc.fi.dc.fd.controller.exception.AlreadyRejectedException;
import es.udc.fi.dc.fd.controller.exception.InstanceNotFoundException;
import es.udc.fi.dc.fd.controller.exception.InvalidRecommendationException;
import es.udc.fi.dc.fd.controller.exception.ItsNotYourFriendException;
import es.udc.fi.dc.fd.controller.exception.RequestParamException;
import es.udc.fi.dc.fd.model.SexCriteriaEnum;
import es.udc.fi.dc.fd.model.persistence.BlockedId;
import es.udc.fi.dc.fd.model.persistence.BlockedImpl;
import es.udc.fi.dc.fd.model.persistence.FriendListOut;
import es.udc.fi.dc.fd.model.persistence.MatchId;
import es.udc.fi.dc.fd.model.persistence.MatchImpl;
import es.udc.fi.dc.fd.model.persistence.RateId;
import es.udc.fi.dc.fd.model.persistence.RateImpl;
import es.udc.fi.dc.fd.model.persistence.RejectedId;
import es.udc.fi.dc.fd.model.persistence.RejectedImpl;
import es.udc.fi.dc.fd.model.persistence.RequestId;
import es.udc.fi.dc.fd.model.persistence.RequestImpl;
import es.udc.fi.dc.fd.model.persistence.SearchCriteria;
import es.udc.fi.dc.fd.model.persistence.UserImpl;
import es.udc.fi.dc.fd.repository.BlockedRepository;
import es.udc.fi.dc.fd.repository.MatchRepository;
import es.udc.fi.dc.fd.repository.RateRepository;
import es.udc.fi.dc.fd.repository.RejectedRepository;
import es.udc.fi.dc.fd.repository.RequestRepository;
import es.udc.fi.dc.fd.repository.UserRepository;

@Service
@Transactional
public class FriendServiceImpl implements FriendService {

	@Autowired
	private RequestRepository requestRepository;

	@Autowired
	private RejectedRepository rejectedRepository;

	@Autowired
	private MatchRepository matchRepository;

	@Autowired
	private BlockedRepository blockedRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RateRepository rateRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private PermissionChecker permissionChecker;

	@Override
	@Transactional(readOnly = true)
	public BlockFriendList<FriendListOut> getFriendList(Long userId, int page, int size)
			throws InstanceNotFoundException, RequestParamException {

		permissionChecker.checkUserExists(userId);

		if (page < 0) {
			throw new RequestParamException("Page must be at less 0, you have passed as page=" + page);
		}
		if (size < 1) {
			throw new RequestParamException("Size must be at less 1, you have passed as size=" + size);
		}

		final Slice<MatchImpl> friendsResult = matchRepository.findFriends(userId, PageRequest.of(page, size));

		final List<FriendListOut> friends = new ArrayList<>();
		FriendListOut out;
		UserImpl user;
		Optional<RateImpl> myRating;
		Long friendId;
		for (final MatchImpl friend : friendsResult.getContent()) {
			if (friend.getMatchId().getUser1() == userId) {
				friendId = friend.getMatchId().getUser2();
			} else {
				friendId = friend.getMatchId().getUser1();
			}
			user=permissionChecker.checkUserByUserId(friendId);
			myRating = rateRepository.findById(new RateId(userId, friendId));
			if (myRating.isPresent()) {
				out = new FriendListOut(user, myRating.get().getPoints());
			} else {
				out = new FriendListOut(user, 0);
			}
			friends.add(out);
		}

		return new BlockFriendList<>(friends, friendsResult.hasNext());
	}

	@Override
	public void acceptRecommendation(Long subject, Long object)
			throws InstanceNotFoundException, InvalidRecommendationException, AlreadyRejectedException,
			AlreadyAceptedException {

		validateRecommendation(subject, object);

		// MAYBE can be easily implemented with Database triggers instead

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
			throws InstanceNotFoundException, InvalidRecommendationException, AlreadyRejectedException,
			AlreadyAceptedException {

		validateRecommendation(subject, object);
		// TODO WE can safely delete the invert request if it were to exist because it
		// doesnt do anything anymore
		rejectedRepository.save(new RejectedImpl(new RejectedId(subject, object), LocalDateTime.now()));

	}

	private void validateRecommendation(Long subject, Long object)
			throws InstanceNotFoundException, InvalidRecommendationException, AlreadyRejectedException,
			AlreadyAceptedException {
		final Optional<UserImpl> subjectUserOpt = userRepository.findById(subject);

		if (subjectUserOpt.isEmpty()) {
			throw new InstanceNotFoundException("Subject User Doesn't Exist", subject);
		}

		final Optional<UserImpl> objectUserOpt = userRepository.findById(object);

		if (objectUserOpt.isEmpty()) {
			throw new InstanceNotFoundException("Object User Doesn't Exist", object);
		}

		final UserImpl subjectUser = subjectUserOpt.get();

		final UserImpl objectUser = objectUserOpt.get();

		// If the object user was already rejected -> exception
		final Optional<RejectedImpl> optRejected = rejectedRepository.findById(new RejectedId(subject, object));
		if (optRejected.isPresent()) {
			throw new AlreadyRejectedException("Object user was already rejected", object);
		}

		// If the object user was already acepted -> exception
		final Optional<RequestImpl> optAcepted = requestRepository.findById(new RequestId(subject, object));
		if (optAcepted.isPresent()) {
			throw new AlreadyAceptedException("Object user was already acepted", object);
		}

		final int objectAge = Period.between(objectUser.getDate().toLocalDate(), LocalDate.now()).getYears();
		// If object user age not in between the criteria -> exception
		if (objectAge < subjectUser.getCriteriaMinAge() || objectAge > subjectUser.getCriteriaMaxAge()) {
			throw new InvalidRecommendationException("ObjectUser doesn't fit subject requirements", objectUser);
		}

		// If object user sex doesnt fit criteria -> exception
		if (!subjectUser.getCriteriaSex().equals(SexCriteriaEnum.ANY)) {
			if (!subjectUser.getCriteriaSex().toString().equalsIgnoreCase(objectUser.getSex())) {
				throw new InvalidRecommendationException("ObjectUser doesn't fit subject requirements", objectUser);
			}
		}

		// If object user city doesnt fit criteria -> exception
		// If CityCriteria is blank means we accept all possible cities
		final SearchCriteria searchCriteria = userService.getSearchCriteria(subject);
		if (searchCriteria.getCity() != null && !searchCriteria.getCity().isEmpty()
				&& searchCriteria.getCity().stream().noneMatch(objectUser.getCity()::equalsIgnoreCase)) {
			throw new InvalidRecommendationException("ObjectUser doesn't fit subject requirements", objectUser);
		}

	}

	@Override
	public Optional<UserImpl> suggestFriend(Long userId) throws InstanceNotFoundException {

		if (userId == null) {
			throw new InstanceNotFoundException("userId can not be null", userId);
		}
		if (!userRepository.existsById(userId)) {
			throw new InstanceNotFoundException("User does not exists", userId);
		}

		final SearchCriteria searchCriteria = userService.getSearchCriteria(userId);

		return userRepository.findByCriteria(searchCriteria, userId);
	}


	@Override
	public void blockUser(Long userId, Long friendId)
			throws InstanceNotFoundException, ItsNotYourFriendException, AlreadyBlockedException {
		permissionChecker.checkUserExists(userId);
		permissionChecker.checkUserExists(friendId);

		final Long firstId = Math.min(userId, friendId);
		final Long secondId = friendId.equals(firstId) ? userId : friendId;
		final Optional<MatchImpl> match = matchRepository.findById(new MatchId(firstId, secondId));
		final Optional<BlockedImpl> block = blockedRepository.findById(new BlockedId(userId, friendId));
		// check if its your friend
		if (!match.isPresent() && !block.isPresent()) {
			throw new ItsNotYourFriendException("It's not your friend");
		}
		// check if its already blocked
		if (block.isPresent()) {
			throw new AlreadyBlockedException("Already blocked");
		}

		final BlockedImpl block2 = new BlockedImpl(new BlockedId(userId, friendId), LocalDateTime.now());

		matchRepository.delete(match.get());
		blockedRepository.save(block2);
	}


}
