package es.udc.fi.dc.fd.service;

import java.util.Optional;

import es.udc.fi.dc.fd.controller.exception.AlreadyRejectedException;
import es.udc.fi.dc.fd.controller.exception.InstanceNotFoundException;
import es.udc.fi.dc.fd.controller.exception.InvalidRecommendationException;
import es.udc.fi.dc.fd.controller.exception.RequestParamException;
import es.udc.fi.dc.fd.dtos.FriendDto;
import es.udc.fi.dc.fd.model.persistence.UserImpl;

public interface FriendService {

	public BlockFriendList<UserImpl> getFriendList(Long userId, int page, int size)
			throws InstanceNotFoundException, RequestParamException;

	public void acceptRecommendation(Long subject, Long object)
			throws InstanceNotFoundException, InvalidRecommendationException, AlreadyRejectedException;

	public void rejectRecommendation(Long subject, Long object)
			throws InstanceNotFoundException, InvalidRecommendationException, AlreadyRejectedException;

	public Optional<FriendDto> suggestFriend(Long userId) throws InstanceNotFoundException;
}
