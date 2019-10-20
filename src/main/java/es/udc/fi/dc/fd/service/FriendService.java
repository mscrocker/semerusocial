package es.udc.fi.dc.fd.service;

import java.util.Optional;

import es.udc.fi.dc.fd.controller.exception.AlreadyRejectedException;
import es.udc.fi.dc.fd.controller.exception.InstanceNotFoundException;
import es.udc.fi.dc.fd.controller.exception.InvalidRecommendationException;
import es.udc.fi.dc.fd.dtos.FriendDto;

public interface FriendService {
	public void acceptRecommendation(Long subject, Long object)
			throws InstanceNotFoundException, InvalidRecommendationException, AlreadyRejectedException;

	public void rejectRecommendation(Long subject, Long object)
			throws InstanceNotFoundException, InvalidRecommendationException, AlreadyRejectedException;

	public Optional<FriendDto> suggestFriend(Long userId) throws InstanceNotFoundException;
}
