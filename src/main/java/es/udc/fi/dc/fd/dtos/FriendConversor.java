package es.udc.fi.dc.fd.dtos;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

import es.udc.fi.dc.fd.model.persistence.FriendListOut;
import es.udc.fi.dc.fd.model.persistence.UserImpl;
import es.udc.fi.dc.fd.service.Block;

public class FriendConversor {

	public final static BlockDto<GetFriendListOutDto> toGetFriendListOutDto(
			Block<FriendListOut> friends) {
		final List<FriendListOut> friendsIn = friends.getElements();

		final List<GetFriendListOutDto> friendsOut = friendsIn.stream().map(e -> toGetFriendListOutDto(e))
				.collect(Collectors.toList());

		return new BlockDto<>(friendsOut, friends.isExistMoreElements());
	}

	public final static GetFriendListOutDto toGetFriendListOutDto(FriendListOut out) {
		final LocalDateTime today = LocalDateTime.now();
		final Period period = Period.between(out.getUser().getDate().toLocalDate(), today.toLocalDate());

		return new GetFriendListOutDto(out.getUser().getId(), out.getUser().getUserName(), period.getYears(),
				out.getUser().getSex(), out.getUser().getCity(), out.getMyRating());
	}

	public final static FriendDto fromUserImpl(UserImpl userImpl) {

		final LocalDateTime today = LocalDateTime.now();

		final Period period = Period.between(userImpl.getDate().toLocalDate(), today.toLocalDate());

		final FriendDto friendDto = new FriendDto(userImpl.getId(), userImpl.getUserName(), period.getYears(),
				userImpl.getSex(), userImpl.getCity(), userImpl.getDescription());
		return friendDto;
	}

}
