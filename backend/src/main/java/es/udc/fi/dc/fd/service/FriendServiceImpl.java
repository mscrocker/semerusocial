package es.udc.fi.dc.fd.service;

import es.udc.fi.dc.fd.controller.exception.AlreadyAceptedException;
import es.udc.fi.dc.fd.controller.exception.AlreadyBlockedException;
import es.udc.fi.dc.fd.controller.exception.AlreadyRejectedException;
import es.udc.fi.dc.fd.controller.exception.CantFindMoreFriendsException;
import es.udc.fi.dc.fd.controller.exception.InstanceNotFoundException;
import es.udc.fi.dc.fd.controller.exception.InvalidRecommendationException;
import es.udc.fi.dc.fd.controller.exception.ItsNotYourFriendException;
import es.udc.fi.dc.fd.controller.exception.RequestParamException;
import es.udc.fi.dc.fd.dtos.SearchUsersDto;
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
import es.udc.fi.dc.fd.model.persistence.SuggestedSearchCriteria;
import es.udc.fi.dc.fd.model.persistence.UserImpl;
import es.udc.fi.dc.fd.repository.BlockedRepository;
import es.udc.fi.dc.fd.repository.MatchRepository;
import es.udc.fi.dc.fd.repository.RateRepository;
import es.udc.fi.dc.fd.repository.RejectedRepository;
import es.udc.fi.dc.fd.repository.RequestRepository;
import es.udc.fi.dc.fd.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FriendServiceImpl implements FriendService {

	@Autowired
	private RequestRepository requestRepository;

	@Autowired
	private RejectedRepository rejectedRepository;

	@Autowired
	private RateRepository rateRepository;

	@Autowired
	private MatchRepository matchRepository;

	@Autowired
	private BlockedRepository blockedRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private PermissionChecker permissionChecker;

	@Override
	@Transactional(readOnly = true)
	public Block<FriendListOut> getFriendList(Long userId, int page, int size)
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
			if (friend.getMatchId().getUser1().equals(userId)) {
				friendId = friend.getMatchId().getUser2();
			} else {
				friendId = friend.getMatchId().getUser1();
			}
			user = permissionChecker.checkUserByUserId(friendId);
			myRating = rateRepository.findById(new RateId(userId, friendId));
			if (myRating.isPresent()) {
				out = new FriendListOut(user, myRating.get().getPoints());
			} else {
				out = new FriendListOut(user, 0);
			}
			friends.add(out);
		}

		return new Block<>(friends, friendsResult.hasNext());
	}

	@Override
	public void acceptRecommendation(Long subject, Long object) throws InstanceNotFoundException,
		InvalidRecommendationException, AlreadyRejectedException, AlreadyAceptedException {

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
		} else {
			// If it doesnt exist it means its the only existent request
			requestRepository.save(new RequestImpl(new RequestId(subject, object), LocalDateTime.now()));
		}
	}

	@Override
	public void rejectRecommendation(Long subject, Long object) throws InstanceNotFoundException,
		InvalidRecommendationException, AlreadyRejectedException, AlreadyAceptedException {

		validateRecommendation(subject, object);
		// TODO WE can safely delete the invert request if it were to exist because it
		// doesnt do anything anymore
		rejectedRepository.save(new RejectedImpl(new RejectedId(subject, object), LocalDateTime.now()));

	}

	private void validateRecommendation(Long subject, Long object) throws InstanceNotFoundException,
		InvalidRecommendationException, AlreadyRejectedException, AlreadyAceptedException {
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

		if (objectUser.isPremium()) {
			return;
		}

		if (!matchesCriteria(objectUser, subjectUser) || !matchesCriteria(subjectUser, objectUser)) {
			throw new InvalidRecommendationException("Invalid recommendation", null);
		}


	}


	private boolean matchesCriteria(UserImpl criteria, UserImpl user) throws InstanceNotFoundException {

		final int objectAge = Period.between(user.getDate().toLocalDate(), LocalDate.now()).getYears();

		// If object user age not in between the criteria -> exception
		if (objectAge < criteria.getCriteriaMinAge() || objectAge > criteria.getCriteriaMaxAge()) {
			return false;
		}

		if ((user.getRatingVotes() > 0) && (criteria.getMinRateCriteria() > user.getRating())) {
			return false;
		}

		switch (criteria.getCriteriaSex()) {
			case OTHER:
				if (user.getSex().equals("Male") || user.getSex().equals("Female")) {
					return false;
				}
				break;
			case FEMALE:
				if (!user.getSex().equals("Female")) {
					return false;
				}
				break;
			case MALE:
				if (!user.getSex().equals("Male")) {
					return false;
				}
				break;
			default:
				break;

		}

		// If object user city doesnt fit criteria -> exception
		// If CityCriteria is blank means we accept all possible cities
		final SearchCriteria searchCriteria = userService.getSearchCriteria(criteria.getId());
		return searchCriteria.getCity() == null || searchCriteria.getCity().isEmpty()
			|| searchCriteria.getCity().stream().anyMatch(user.getCity()::equalsIgnoreCase);
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
		final Optional<UserImpl> user = userRepository.findByCriteria(searchCriteria, userId);

		return user;
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

	/**
	 * precondition: user dont find friends with her/his actual criteria.
	 */
	@Override
	public SuggestedSearchCriteria suggestNewCriteria(Long userId)
		throws InstanceNotFoundException, CantFindMoreFriendsException {

		if (userId == null) {
			throw new InstanceNotFoundException("userId can not be null", userId);
		}
		if (!userRepository.existsById(userId)) {
			throw new InstanceNotFoundException("User does not exists", userId);
		}

		final SearchCriteria searchCriteria = userService.getSearchCriteria(userId);
		final int minPosibleAge = 18;
		final int maxPosibleAge = 200;
		final int minPosibleRate = 1;
		int newUsersSuggested;
		int newUsersSuggestedLimit;

		for (int minRate = searchCriteria.getMinRate(); minRate >= minPosibleRate; minRate -= 1) {

			SearchCriteria newCriteria = new SearchCriteria(searchCriteria.getSex(), searchCriteria.getMinAge(),
				maxPosibleAge, searchCriteria.getCity(), minRate);
			// MaxAge
			newUsersSuggestedLimit = userRepository.findByCriteriaMaxResults(newCriteria, userId);

			if (newUsersSuggestedLimit > 0) { // Si maxAge=200 encuentra a alguien
				for (int maxAge = searchCriteria.getMaxAge(); maxAge <= maxPosibleAge; maxAge += 5) {
					newCriteria = new SearchCriteria(searchCriteria.getSex(), searchCriteria.getMinAge(), maxAge,
						searchCriteria.getCity(), minRate);
					newUsersSuggested = userRepository.findByCriteriaMaxResults(newCriteria, userId);
					if (newUsersSuggested > 0) {
						return new SuggestedSearchCriteria(0, newCriteria.getMaxAge() - searchCriteria.getMaxAge(),
							minRate - searchCriteria.getMinRate(), newUsersSuggested);
					}
				} // Para el caso del maximo
				return new SuggestedSearchCriteria(0, maxPosibleAge - searchCriteria.getMaxAge(), 0,
					newUsersSuggestedLimit);
			}
			// minAge
			newCriteria = new SearchCriteria(searchCriteria.getSex(), minPosibleAge, searchCriteria.getMaxAge(),
				searchCriteria.getCity(), minRate);
			newUsersSuggestedLimit = userRepository.findByCriteriaMaxResults(newCriteria, userId);

			if (newUsersSuggestedLimit > 0) { // Si minAge = 18 encuentra a alguien
				for (int minAge = searchCriteria.getMinAge(); minAge >= minPosibleAge; minAge -= 5) {
					newCriteria = new SearchCriteria(searchCriteria.getSex(), minAge, searchCriteria.getMaxAge(),
						searchCriteria.getCity(), minRate);
					newUsersSuggested = userRepository.findByCriteriaMaxResults(newCriteria, userId);
					if (newUsersSuggested > 0) {
						return new SuggestedSearchCriteria(newCriteria.getMinAge() - searchCriteria.getMinAge(), 0,
							minRate - searchCriteria.getMinRate(), newUsersSuggested);
					}
				} // Para el caso del minimo
				return new SuggestedSearchCriteria(minPosibleAge - searchCriteria.getMinAge(), 0, 0,
					newUsersSuggestedLimit);
			}
		}
		throw new CantFindMoreFriendsException("Even if you change your criteria you cant find more friends");

	}

	@Override
	public Block<UserImpl> searchUsersByMetadataAndKeywords(SearchUsersDto params, int page, int size) {
		final Slice<UserImpl> users = userRepository.searchUsersByMetadataAndKeywords(params, page, size);

		return new Block<>(users.getContent(), users.hasNext());
	}

}
