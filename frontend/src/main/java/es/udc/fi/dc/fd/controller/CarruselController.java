package es.udc.fi.dc.fd.controller;

import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class CarruselController {

  @GetMapping(path = "/carrusel")
  public final String displayLogin() {
    return UserViewConstants.CARRUSEL_FORM;
  }

  @GetMapping(path = "/addImage")
  public final String displayAddImage() {
    return UserViewConstants.ADD_IMAGE_FORM;
  }

  @GetMapping(path = "/users/profile")
  public final String displayGetUserData() {
    return UserViewConstants.GET_PROFILE;
  }

  @GetMapping(path = "/findFriend")
  public final String displayFriendFinder() {

    return UserViewConstants.FRIEND_FINDER;
  }

  @GetMapping(path = {"/chat/{id}", "/chat"})
  public final String chatScreen(@PathVariable Optional<Long> ignored) {

    return "chat";
  }
}
