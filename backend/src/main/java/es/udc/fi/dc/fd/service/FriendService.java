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
import es.udc.fi.dc.fd.model.persistence.FriendListOut;
import es.udc.fi.dc.fd.model.persistence.SuggestedSearchCriteria;
import es.udc.fi.dc.fd.model.persistence.UserImpl;
import java.util.Optional;

public interface FriendService {

  public Block<FriendListOut> getFriendList(Long userId, int page, int size)
      throws InstanceNotFoundException, RequestParamException;

  public void acceptRecommendation(Long subject, Long object) throws InstanceNotFoundException,
      InvalidRecommendationException, AlreadyRejectedException, AlreadyAceptedException;

  public void rejectRecommendation(Long subject, Long object) throws InstanceNotFoundException,
      InvalidRecommendationException, AlreadyRejectedException, AlreadyAceptedException;

  public Optional<UserImpl> suggestFriend(Long userId) throws InstanceNotFoundException;

  void blockUser(Long userId, Long friendId)
      throws InstanceNotFoundException, ItsNotYourFriendException, AlreadyBlockedException;

  public SuggestedSearchCriteria suggestNewCriteria(Long userId)
      throws InstanceNotFoundException, CantFindMoreFriendsException;

  public Block<UserImpl> searchUsersByMetadataAndKeywords(SearchUsersDto params, int page,
                                                          int size);

}
