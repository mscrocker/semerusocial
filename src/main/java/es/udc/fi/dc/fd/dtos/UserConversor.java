package es.udc.fi.dc.fd.dtos;

import java.time.LocalDateTime;

import es.udc.fi.dc.fd.model.User;
import es.udc.fi.dc.fd.model.persistence.UserImpl;

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

	private UserConversor() {}

}
