package es.udc.fi.dc.fd.dtos;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import es.udc.fi.dc.fd.model.User;
import es.udc.fi.dc.fd.model.persistence.UserImpl;
import es.udc.fi.dc.fd.service.Block;

public class UserConversor {

	public final static User fromRegisterDto(RegisterParamsDto params) {

		final LocalDateTime date = LocalDateTime.of(params.getProfileData().getYear(), params.getProfileData().getMonth(), params.getProfileData().getDay(), 00, 01);
		return new UserImpl(params.getLoginParams().getUserName(), params.getLoginParams().getPassword(), date, params.getProfileData().getAgelessFields().getSex(), params.getProfileData().getAgelessFields().getCity(),
				params.getProfileData().getAgelessFields().getDescription());
	}

	public final static UserImpl toUserImpl(DateUserProfileDto updateProfileInDto) {

		final LocalDateTime date = LocalDateTime.of(updateProfileInDto.getYear(), updateProfileInDto.getMonth(),
				updateProfileInDto.getDay(), 00, 01);
		return new UserImpl(date, updateProfileInDto.getAgelessFields().getSex(), updateProfileInDto.getAgelessFields().getCity(),
				updateProfileInDto.getAgelessFields().getDescription());
	}

	public final static BlockDto<FullUserProfileDto> toReturnedUserBlockDto(Block<UserImpl> users) {
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

	private UserConversor() {}

}
