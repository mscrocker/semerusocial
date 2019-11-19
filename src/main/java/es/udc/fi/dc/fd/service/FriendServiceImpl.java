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
import es.udc.fi.dc.fd.controller.exception.AlreadyRejectedException;
import es.udc.fi.dc.fd.controller.exception.InstanceNotFoundException;
import es.udc.fi.dc.fd.controller.exception.InvalidRecommendationException;
import es.udc.fi.dc.fd.controller.exception.NotYourFriendException;
import es.udc.fi.dc.fd.controller.exception.RequestParamException;
import es.udc.fi.dc.fd.controller.exception.ValidationException;
import es.udc.fi.dc.fd.model.SexCriteriaEnum;
import es.udc.fi.dc.fd.model.persistence.MatchId;
import es.udc.fi.dc.fd.model.persistence.MatchImpl;
import es.udc.fi.dc.fd.model.persistence.MessageImpl;
import es.udc.fi.dc.fd.model.persistence.RejectedId;
import es.udc.fi.dc.fd.model.persistence.RejectedImpl;
import es.udc.fi.dc.fd.model.persistence.RequestId;
import es.udc.fi.dc.fd.model.persistence.RequestImpl;
import es.udc.fi.dc.fd.model.persistence.SearchCriteria;
import es.udc.fi.dc.fd.model.persistence.UserImpl;
import es.udc.fi.dc.fd.repository.MatchRepository;
import es.udc.fi.dc.fd.repository.MessageRepository;
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
	private UserRepository userRepository;

	@Autowired
	private MessageRepository messageRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private PermissionChecker permissionChecker;

	private static final int MAX_LENGTH_MESSAGE = 999;

	@Override
	@Transactional(readOnly = true)
	public BlockFriendList<UserImpl> getFriendList(Long userId, int page, int size)
			throws InstanceNotFoundException, RequestParamException {

		permissionChecker.checkUserExists(userId);

		if (page < 0) {
			throw new RequestParamException("Page must be at less 0, you have passed as page=" + page);
		}
		if (size < 1) {
			throw new RequestParamException("Size must be at less 1, you have passed as size=" + size);
		}

		final Slice<MatchImpl> friendsResult = matchRepository.findFriends(userId, PageRequest.of(page, size));

		final List<UserImpl> friends = new ArrayList<>();
		UserImpl user;
		Long friendId;
		for (final MatchImpl friend : friendsResult.getContent()) {
			if (friend.getMatchId().getUser1() == userId) {
				friendId = friend.getMatchId().getUser2();
			} else {
				friendId = friend.getMatchId().getUser1();
			}
			user = permissionChecker.checkUserByUserId(friendId);
			friends.add(user);
		}

		return new BlockFriendList<>(friends, friendsResult.hasNext());
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
		}
		// If it doesnt exist it means its the only existent request
		else {
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
	public void sendMessage(Long userId, Long friendId, String content)
			throws InstanceNotFoundException, NotYourFriendException, ValidationException {

		// Comprobamos que los ids no son nulos
		if (userId == null || friendId == null) {
			throw new ValidationException("Ids can not be null");
		}
		//Comprobamos que el mensaje no es nulo
		if (content==null) {
			throw new ValidationException("Message content can not be null");
		}
		//Validamos que el mensaje no se pase del largo permitido
		if (content.length() > MAX_LENGTH_MESSAGE || content.trim().length() == 0) {
			throw new ValidationException(
					"Message length too large or blank. It must be less than " + MAX_LENGTH_MESSAGE);
		}

		// Comprobamos que no te estás intentando mandar un mensaje a ti mismo
		if (userId.equals(friendId)) {
			throw new ValidationException("You can not send a message to yourself");
		}
		// Comprobamos que los usuarios existen
		final Optional<UserImpl> user = userRepository.findById(userId);
		if (user.isEmpty()) {
			throw new InstanceNotFoundException(UserImpl.class.getName(), userId);
		}
		final Optional<UserImpl> friend = userRepository.findById(friendId);
		if (friend.isEmpty()) {
			throw new InstanceNotFoundException(UserImpl.class.getName(), friendId);
		}

		//Comprobamos que sean amigos
		if ((matchRepository.findMatch(userId, friendId)).isEmpty()
				&& (matchRepository.findMatch(friendId, userId)).isEmpty()) {
			throw new NotYourFriendException("User with id "+friendId+" is not your friend.");
		}


		//Podemos almacenar el mensaje
		final MessageImpl msg = new MessageImpl();
		msg.setDate(LocalDateTime.now());
		msg.setMessageContent(content);
		if (userId < friendId) {
			msg.setUser1(user.get());
			msg.setUser2(friend.get());
		} else {
			msg.setUser2(user.get());
			msg.setUser1(friend.get());
		}
		msg.setTransmitter(user.get());
		messageRepository.save(msg);
	}

}
