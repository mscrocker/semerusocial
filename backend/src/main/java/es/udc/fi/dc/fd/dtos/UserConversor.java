package es.udc.fi.dc.fd.dtos;

import es.udc.fi.dc.fd.model.User;
import es.udc.fi.dc.fd.model.persistence.UserImpl;
import es.udc.fi.dc.fd.service.Block;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public final class UserConversor {

  /**
   * Trannsforms a RegisterParamsDto into an user entity.
   * @param params The DTO
   * @return The user entity
   */
  public static final User fromRegisterDto(RegisterParamsDto params) {

    final LocalDateTime date = LocalDateTime.of(params.getProfileData().getYear(),
        params.getProfileData().getMonth(), params.getProfileData().getDay(), 00, 01);
    return new UserImpl(params.getLoginParams().getUserName(),
        params.getLoginParams().getPassword(), date,
        params.getProfileData().getAgelessFields().getSex(),
        params.getProfileData().getAgelessFields().getCity(),
        params.getProfileData().getAgelessFields().getDescription());
  }

  /**
   * Transforms a DateUserProfileDto into an user entity.
   * @param updateProfileInDto The DTO
   * @return The user entity
   */
  public static final UserImpl toUserImpl(DateUserProfileDto updateProfileInDto) {

    final LocalDateTime date = LocalDateTime.of(updateProfileInDto.getYear(),
        updateProfileInDto.getMonth(), updateProfileInDto.getDay(), 00, 01);
    return new UserImpl(date, updateProfileInDto.getAgelessFields().getSex(),
        updateProfileInDto.getAgelessFields().getCity(),
        updateProfileInDto.getAgelessFields().getDescription());
  }

  /**
   * Transforms a block of user entities into a block dto of FullUserProfileDto.
   * @param users The block with the user entities
   * @return The block dto with the FullUserProfileDto
   */
  public static final BlockDto<FullUserProfileDto> toReturnedUserBlockDto(Block<UserImpl> users) {
    final List<UserImpl> usersIn = users.getElements();

    final List<FullUserProfileDto> usersOut = usersIn.stream().map(
        u ->
            new FullUserProfileDto(
                u.getRating(),
                u.isPremium(),
                new DateUserProfileDto(
                    u.getDate().getYear(),
                    u.getDate().getMonthValue(),
                    u.getDate().getDayOfMonth(),
                    new AgelessUserProfileDto(
                        u.getSex(),
                        u.getCity(),
                        u.getDescription()
                    )
                )
            )).collect(Collectors.toList());

    return new BlockDto<>(usersOut, users.isExistMoreElements());
  }

  private UserConversor() {
  }

}
