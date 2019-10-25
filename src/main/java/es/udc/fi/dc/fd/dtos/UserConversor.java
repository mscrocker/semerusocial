package es.udc.fi.dc.fd.dtos;

import java.time.LocalDateTime;

import es.udc.fi.dc.fd.model.User;
import es.udc.fi.dc.fd.model.persistence.UserImpl;

public class UserConversor {

	public final static User fromRegisterDto(RegisterParamsDto params) {

		final LocalDateTime date = LocalDateTime.of(params.getYear(), params.getMonth(), params.getDay(), 00, 01);
		return new UserImpl(params.getUserName(), params.getPassword(), date, params.getSex(), params.getCity(),
				params.getDescription());
	}

	public final static UserImpl toUserImpl(UpdateProfileInDto updateProfileInDto) {

		final LocalDateTime date = LocalDateTime.of(updateProfileInDto.getYear(), updateProfileInDto.getMonth(),
				updateProfileInDto.getDay(), 00, 01);
		return new UserImpl(date, updateProfileInDto.getSex(), updateProfileInDto.getCity(),
				updateProfileInDto.getDescription());
	}

	private UserConversor() {}

}
