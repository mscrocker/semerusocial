package es.udc.fi.dc.fd.dtos;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;

import es.udc.fi.dc.fd.model.User;
import es.udc.fi.dc.fd.model.persistence.UserImpl;

public class UserConversor {

	private static LocalDate fromMilis(Long milis) {
		return LocalDate.ofInstant(Instant.ofEpochMilli(milis), ZoneId.systemDefault());
	}

	public final static User fromRegisterDto(RegisterParamsDto params) {
		int year = Period.between(fromMilis(params.getAge()), LocalDate.now()).getYears();
		return new UserImpl(params.getUserName(), params.getPassword(), year, params.getSex(), params.getCity());
	}

	private UserConversor() {
	}

}
