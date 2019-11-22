package es.udc.fi.dc.fd.dtos;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import es.udc.fi.dc.fd.model.persistence.UserImpl;

@JsonIgnoreProperties(value = { "id", "userName", "password", "suggestion", "criteriaSex", "criteriaMinAge", "criteriaMaxAge", "ratingVotes", "minRateCriteria" })
public class UserDataDto extends UserImpl {

	private static final long serialVersionUID = 1L;

	public UserDataDto() {
		super();
	}

	public UserDataDto(LocalDateTime date, String sex, String city, String description, double rating, boolean premium) {
		super();
		this.setDate(date);
		this.setSex(sex);
		this.setCity(city);
		this.setDescription(description);
		this.setRating(rating);
		this.setPremium(premium);
	}

}
