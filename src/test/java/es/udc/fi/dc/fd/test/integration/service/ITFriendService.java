package es.udc.fi.dc.fd.test.integration.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;

import es.udc.fi.dc.fd.controller.exception.DuplicateInstanceException;
import es.udc.fi.dc.fd.controller.exception.InstanceNotFoundException;
import es.udc.fi.dc.fd.controller.exception.InvalidDateException;
import es.udc.fi.dc.fd.controller.exception.RequestParamException;
import es.udc.fi.dc.fd.model.persistence.MatchId;
import es.udc.fi.dc.fd.model.persistence.MatchImpl;
import es.udc.fi.dc.fd.model.persistence.UserImpl;
import es.udc.fi.dc.fd.repository.MatchRepository;
import es.udc.fi.dc.fd.service.BlockFriendList;
import es.udc.fi.dc.fd.service.FriendService;
import es.udc.fi.dc.fd.service.UserService;

@RunWith(JUnitPlatform.class)
@ExtendWith(SpringExtension.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class })
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:context/service.xml", "classpath:context/persistence.xml",
"classpath:context/application-context.xml" })
@TestPropertySource({ "classpath:config/persistence-access.properties", "classpath:config/service.properties" })
public class ITFriendService {

	@Autowired
	private UserService userService;

	@Autowired
	private FriendService friendService;

	@Autowired
	private MatchRepository matchRespository;

	@Autowired
	public ITFriendService() {
		super();
	}

	private UserImpl createUser(String userName, String password, LocalDateTime date, String sex, String city,
			String description) {
		return new UserImpl(userName, password, date, sex, city, description);
	}

	private LocalDateTime getDateTime(int day, int month, int year) {
		return LocalDateTime.of(year, month, day, 00, 01);
	}

	// ----- getFriendList -----

	@Test
	public void testGetFriendList()
			throws DuplicateInstanceException, InvalidDateException, InstanceNotFoundException, RequestParamException {
		final UserImpl user1 = createUser("usuarioFriendList1", "contraseñaFriendList1", getDateTime(1, 1, 2000),
				"hombre", "coruna", "descripcion");
		final UserImpl user2 = createUser("usuarioFriendList2", "contraseñaFriendList2", getDateTime(1, 1, 2000),
				"hombre", "coruna", "descripcion");
		final UserImpl user3 = createUser("usuarioFriendList3", "contraseñaFriendList3", getDateTime(1, 1, 2000),
				"hombre", "coruna", "descripcion");
		final UserImpl user4 = createUser("usuarioFriendList4", "contraseñaFriendList4", getDateTime(1, 1, 2000),
				"hombre", "coruna", "descripcion");
		final UserImpl user5 = createUser("usuarioFriendList5", "contraseñaFriendList5", getDateTime(1, 1, 2000),
				"hombre", "coruna", "descripcion");

		userService.signUp(user1);
		userService.signUp(user2);
		userService.signUp(user3);
		userService.signUp(user4);
		userService.signUp(user5);

		matchRespository.save(new MatchImpl(new MatchId(user1.getId(), user2.getId()), getDateTime(1, 1, 2000)));
		matchRespository.save(new MatchImpl(new MatchId(user1.getId(), user3.getId()), getDateTime(1, 1, 2000)));
		matchRespository.save(new MatchImpl(new MatchId(user1.getId(), user4.getId()), getDateTime(1, 1, 2000)));
		matchRespository.save(new MatchImpl(new MatchId(user1.getId(), user5.getId()), getDateTime(1, 1, 2000)));

		BlockFriendList<UserImpl> user1Result = friendService.getFriendList(user1.getId(), 0, 2);
		assertEquals(user1Result.getFriends().size(), 2);
		assertEquals(user1Result.getExistMoreFriends(), true);

		user1Result = friendService.getFriendList(user1.getId(), 1, 2);
		assertEquals(user1Result.getFriends().size(), 2);
		assertEquals(user1Result.getExistMoreFriends(), false);

		user1Result = friendService.getFriendList(user1.getId(), 2, 2);
		assertEquals(user1Result.getFriends().size(), 0);
		assertEquals(user1Result.getExistMoreFriends(), false);
	}

	@Test
	public void testGetFriendListWithInstanceNotFoundException() {
		assertThrows(InstanceNotFoundException.class, () -> {
			friendService.getFriendList(-1L, 0, 10);
		});
	}

	@Test
	public void testGetFriendListRequestParamException() throws DuplicateInstanceException, InvalidDateException {
		final UserImpl user = createUser("usuarioFriendListRPE", "contraseñaFriendListRPE", getDateTime(1, 1, 2000),
				"hombre", "coruna", "descripcion");

		userService.signUp(user);

		assertThrows(RequestParamException.class, () -> {
			friendService.getFriendList(user.getId(), -1, 10);
		});

		assertThrows(RequestParamException.class, () -> {
			friendService.getFriendList(user.getId(), 0, 0);
		});
	}

}
