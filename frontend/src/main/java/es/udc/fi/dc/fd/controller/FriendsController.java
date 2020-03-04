package es.udc.fi.dc.fd.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FriendsController {

  @GetMapping(path = "/users/friends")
  public final String displayGetUserData() {
    return UserViewConstants.GET_FRIEND_LIST;
  }
}
