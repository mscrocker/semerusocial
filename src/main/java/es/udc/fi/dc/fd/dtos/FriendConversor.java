package es.udc.fi.dc.fd.dtos;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

import es.udc.fi.dc.fd.model.persistence.FriendListOut;
import es.udc.fi.dc.fd.model.persistence.UserImpl;
import es.udc.fi.dc.fd.service.Block;

public class FriendConversor {

	public final static BlockDto<RatedFriendDto> toGetFriendListOutDto(
			Block<FriendListOut> friends) {
		final List<FriendListOut> friendsIn = friends.getElements();

		final List<RatedFriendDto> friendsOut = friendsIn.stream().map(e -> toGetFriendListOutDto(e))
				.collect(Collectors.toList());

		return new BlockDto<>(friendsOut, friends.isExistMoreElements());
	}

	public final static RatedFriendDto toGetFriendListOutDto(FriendListOut out) {
		final LocalDateTime today = LocalDateTime.now();
		final Period period = Period.between(out.getUser().getDate().toLocalDate(), today.toLocalDate());

		return new RatedFriendDto(new UnratedFriendDto(out.getUser().getId(), out.getUser().getUserName(),new AgeUserProfileDto( period.getYears(),
				new AgelessUserProfileDto(out.getUser().getSex(), out.getUser().getCity(), out.getUser().getDescription()))), out.getMyRating());
	}

	public final static UnratedFriendDto fromUserImpl(UserImpl userImpl) {

		final LocalDateTime today = LocalDateTime.now();

		final Period period = Period.between(userImpl.getDate().toLocalDate(), today.toLocalDate());

		final UnratedFriendDto friendDto = new UnratedFriendDto(userImpl.getId(), userImpl.getUserName(), new AgeUserProfileDto(period.getYears(),
				new AgelessUserProfileDto(userImpl.getSex(), userImpl.getCity(), userImpl.getDescription())));
		return friendDto;
	}

}
