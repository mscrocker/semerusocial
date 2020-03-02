package es.udc.fi.dc.fd.dtos;

import es.udc.fi.dc.fd.model.persistence.FriendListOut;
import es.udc.fi.dc.fd.model.persistence.UserImpl;
import es.udc.fi.dc.fd.service.Block;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

public final class FriendConversor {

  /**
   * Converts a block of friend entities to a block DTO with a list of friend entities.
   * @param friends The block with the friend entities
   * @return The block dto with the friend entities
   */
  public static final BlockDto<RatedFriendDto> toGetFriendListOutDto(
      Block<FriendListOut> friends) {
    final List<FriendListOut> friendsIn = friends.getElements();

    final List<RatedFriendDto> friendsOut = friendsIn.stream().map(e -> toGetFriendListOutDto(e))
        .collect(Collectors.toList());

    return new BlockDto<>(friendsOut, friends.isExistMoreElements());
  }

  /**
   * Converts a friend entity to a ratedfriend dto.
   * @param out The friend entity
   * @return The ratedfriend dto
   */
  public static final RatedFriendDto toGetFriendListOutDto(FriendListOut out) {
    final LocalDateTime today = LocalDateTime.now();
    final Period period = Period.between(out.getUser().getDate().toLocalDate(),
        today.toLocalDate());

    return new RatedFriendDto(new UnratedFriendDto(out.getUser().getId(),
        out.getUser().getUserName(), new AgeUserProfileDto(period.getYears(),
        new AgelessUserProfileDto(out.getUser().getSex(), out.getUser().getCity(),
            out.getUser().getDescription()))), out.getMyRating());
  }

  /**
   * Transforms a user entity to an unrated friend dto.
   * @param userImpl The user entity
   * @return The unrated friend dto
   */
  public static final UnratedFriendDto fromUserImpl(UserImpl userImpl) {

    final LocalDateTime today = LocalDateTime.now();

    final Period period = Period.between(userImpl.getDate().toLocalDate(), today.toLocalDate());

    final UnratedFriendDto friendDto = new UnratedFriendDto(userImpl.getId(),
        userImpl.getUserName(), new AgeUserProfileDto(period.getYears(),
        new AgelessUserProfileDto(userImpl.getSex(), userImpl.getCity(),
            userImpl.getDescription())));
    return friendDto;
  }

  /**
   * Converts a block with a list of user entites to a block dto with a list of user profiles dto.
   * @param users The block with the list of user entities
   * @return The block with the list of user profiles dto
   */
  public static final BlockDto<FullUserProfileDto> toFullUserProfileDto(Block<UserImpl> users) {
    final List<UserImpl> usersIn = users.getElements();

    final List<FullUserProfileDto> usersOut = usersIn.stream().map(e -> toFullUserProfileDto(e))
        .collect(Collectors.toList());

    return new BlockDto<>(usersOut, users.isExistMoreElements());
  }

  /**
   * Converts a user entity to an user profile dto.
   * @param out The user entity
   * @return The user profile dto
   */
  public static final FullUserProfileDto toFullUserProfileDto(UserImpl out) {
    return new FullUserProfileDto(out.getRating(), out.isPremium(),
        new DateUserProfileDto(out.getDate().getDayOfMonth(), out.getDate().getMonthValue(),
            out.getDate().getYear(),
            new AgelessUserProfileDto(out.getSex(), out.getCity(), out.getDescription())));
  }

  private FriendConversor() {

  }
}
