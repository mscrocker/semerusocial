package es.udc.fi.dc.fd.test.unit.dtos;

import org.junit.jupiter.api.Test;

import es.udc.fi.dc.fd.controller.chat.ChatMessage;
import es.udc.fi.dc.fd.dtos.AgeUserProfileDto;
import es.udc.fi.dc.fd.dtos.AgelessUserProfileDto;
import es.udc.fi.dc.fd.dtos.BlockDto;
import es.udc.fi.dc.fd.dtos.DateUserProfileDto;
import es.udc.fi.dc.fd.dtos.ErrorsDto;
import es.udc.fi.dc.fd.dtos.FieldErrorDto;
import es.udc.fi.dc.fd.dtos.FriendHeaderDto;
import es.udc.fi.dc.fd.dtos.FullUserProfileDto;
import es.udc.fi.dc.fd.dtos.IdDto;
import es.udc.fi.dc.fd.dtos.ImageDataDto;
import es.udc.fi.dc.fd.dtos.ImageDto;
import es.udc.fi.dc.fd.dtos.LoginParamsDto;
import es.udc.fi.dc.fd.dtos.MessageDetailsDto;
import es.udc.fi.dc.fd.dtos.RateDto;
import es.udc.fi.dc.fd.dtos.RatedFriendDto;
import es.udc.fi.dc.fd.dtos.RegisterParamsDto;
import es.udc.fi.dc.fd.dtos.SearchCriteriaDto;
import es.udc.fi.dc.fd.dtos.SearchUsersDto;
import es.udc.fi.dc.fd.dtos.UnratedFriendDto;
import es.udc.fi.dc.fd.dtos.UserAuthenticatedDto;
import es.udc.fi.dc.fd.test.utils.EntityTestUtils;

class DtoTests {

	@Test
	void testAgelessUserProfileDto() {
		EntityTestUtils.testEntity(AgelessUserProfileDto.class);
	}

	@Test
	void testAgeUserProfileDto() {
		EntityTestUtils.testEntity(AgeUserProfileDto.class);
	}

	@Test
	void testRatedFriendDto() {
		EntityTestUtils.testEntity(RatedFriendDto.class);
	}

	@Test
	void testBlockDto() {
		EntityTestUtils.testEntity(BlockDto.class);
	}

	@Test
	void testErrorsDto() {
		EntityTestUtils.testEntity(ErrorsDto.class);
	}

	@Test
	void testFieldErrorDto() {
		EntityTestUtils.testEntity(FieldErrorDto.class);
	}

	@Test
	void testFriendDto() {
		EntityTestUtils.testEntity(UnratedFriendDto.class);
	}

	@Test
	void testGetFriendListOutDto() {
		EntityTestUtils.testEntity(RatedFriendDto.class);
	}

	@Test
	void testIdDto() {
		EntityTestUtils.testEntity(IdDto.class);
	}

	@Test
	void testImageCreationDto() {
		EntityTestUtils.testEntity(ImageDataDto.class);
	}

	@Test
	void testLoginParamsDto() {
		EntityTestUtils.testEntity(LoginParamsDto.class);
	}

	@Test
	void testRegisterParamsDto() {
		EntityTestUtils.testEntity(RegisterParamsDto.class);
	}

	@Test
	void testReturnedImagesDto() {
		EntityTestUtils.testEntity(ImageDto.class);
	}

	@Test
	void testSearchCriteriaDto() {
		EntityTestUtils.testEntity(SearchCriteriaDto.class);
	}

	@Test
	void testUpdateProfileInDto() {
		EntityTestUtils.testEntity(DateUserProfileDto.class);
	}

	@Test
	void testUserAuthenticatedDto() {
		EntityTestUtils.testEntity(UserAuthenticatedDto.class);
	}

	@Test
	void testUserDataDto() {
		EntityTestUtils.testEntity(FullUserProfileDto.class);
	}

	@Test
	void testChatMessage() {
		EntityTestUtils.testEntity(ChatMessage.class);
	}

	@Test
	void testRateDto() {
		EntityTestUtils.testEntity(RateDto.class);
	}

	@Test
	void testMessageDetailsDto() {
		EntityTestUtils.testEntity(MessageDetailsDto.class);
	}

	@Test
	void testFriendHeaderDto() {
		EntityTestUtils.testEntity(FriendHeaderDto.class);
	}

	@Test
	void testSearchUsersDto() {
		EntityTestUtils.testEntity(SearchUsersDto.class);
	}

}
