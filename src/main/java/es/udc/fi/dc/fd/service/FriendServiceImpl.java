package es.udc.fi.dc.fd.service;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fi.dc.fd.controller.exception.InstanceNotFoundException;
import es.udc.fi.dc.fd.controller.exception.RequestParamException;
import es.udc.fi.dc.fd.model.persistence.MatchImpl;
import es.udc.fi.dc.fd.model.persistence.UserImpl;
import es.udc.fi.dc.fd.repository.MatchRepository;
import es.udc.fi.dc.fd.repository.UserRepository;

@Service
@Transactional
public class FriendServiceImpl implements FriendService {

	private final UserRepository userRepository;

	private final MatchRepository matchRepository;

	private final PermissionChecker permissionChecker;

	@Autowired
	public FriendServiceImpl(UserRepository userRepository, MatchRepository matchRepository,
			PermissionChecker permissionChecker) {
		super();

		this.userRepository = checkNotNull(userRepository,
				"Received a null pointer as userRepository in FriendServiceImpl");

		this.matchRepository = checkNotNull(matchRepository,
				"Received a null pointer as matchRepository in FriendServiceImpl");

		this.permissionChecker = checkNotNull(permissionChecker,
				"Received a null pointer as permissionChecker in FriendServiceImpl");
	}

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

		final Slice<MatchImpl> friendsResult = getMatchRepository().findFriends(userId, PageRequest.of(page, size));

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

	public UserRepository getUserRepository() {
		return userRepository;
	}

	public MatchRepository getMatchRepository() {
		return matchRepository;
	}

}
