package es.udc.fi.dc.fd.service;

import java.util.Optional;

import es.udc.fi.dc.fd.controller.exception.AlreadyRejectedException;
import es.udc.fi.dc.fd.controller.exception.InstanceNotFoundException;
import es.udc.fi.dc.fd.controller.exception.InvalidRecommendationException;
import es.udc.fi.dc.fd.model.persistence.UserImpl;

public interface FriendService {
	public void acceptRecommendation(Long subject, Long object)
			throws InstanceNotFoundException, InvalidRecommendationException, AlreadyRejectedException;

	public void rejectRecommendation(Long subject, Long object)
			throws InstanceNotFoundException, InvalidRecommendationException, AlreadyRejectedException;

	public Optional<UserImpl> suggestFriend(Long userId) throws InstanceNotFoundException;
}
