package es.udc.fi.dc.fd.service;

import es.udc.fi.dc.fd.controller.exception.InstanceNotFoundException;
import es.udc.fi.dc.fd.model.persistence.UserImpl;

public interface FriendService {

	public BlockFriendList<UserImpl> getFriendList(Long userId, int page, int request)
			throws InstanceNotFoundException;

}
