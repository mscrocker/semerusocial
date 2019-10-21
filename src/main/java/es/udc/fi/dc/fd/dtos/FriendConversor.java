package es.udc.fi.dc.fd.dtos;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

import es.udc.fi.dc.fd.model.persistence.UserImpl;
import es.udc.fi.dc.fd.service.BlockFriendList;

public class FriendConversor {

	public final static BlockGetFriendListDto<GetFriendListOutDto> toGetFriendListOutDto(
			BlockFriendList<UserImpl> friends) {
		final List<UserImpl> friendsIn = friends.getFriends();

		final List<GetFriendListOutDto> friendsOut = friendsIn.stream().map(e -> toGetFriendListOutDto(e))
				.collect(Collectors.toList());

		return new BlockGetFriendListDto<>(friendsOut, friends.getExistMoreFriends());
	}

	public final static GetFriendListOutDto toGetFriendListOutDto(UserImpl user) {
		final LocalDateTime today = LocalDateTime.now();
		final Period period = Period.between(user.getDate().toLocalDate(), today.toLocalDate());

		return new GetFriendListOutDto(user.getUserName(), period.getYears(), user.getSex(), user.getCity());
	}

}
