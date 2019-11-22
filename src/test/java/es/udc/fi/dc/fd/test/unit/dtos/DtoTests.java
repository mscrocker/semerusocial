package es.udc.fi.dc.fd.test.unit.dtos;

import org.junit.jupiter.api.Test;

import es.udc.fi.dc.fd.controller.chat.ChatMessage;
import es.udc.fi.dc.fd.dtos.BlockDto;
import es.udc.fi.dc.fd.dtos.BlockGetFriendListDto;
import es.udc.fi.dc.fd.dtos.BlockImageByUserIdDto;
import es.udc.fi.dc.fd.dtos.ErrorsDto;
import es.udc.fi.dc.fd.dtos.FieldErrorDto;
import es.udc.fi.dc.fd.dtos.FriendDto;
import es.udc.fi.dc.fd.dtos.FriendHeaderDto;
import es.udc.fi.dc.fd.dtos.GetFriendListOutDto;
import es.udc.fi.dc.fd.dtos.IdDto;
import es.udc.fi.dc.fd.dtos.ImageCreatedDto;
import es.udc.fi.dc.fd.dtos.ImageCreationDto;
import es.udc.fi.dc.fd.dtos.ImageEditionDto;
import es.udc.fi.dc.fd.dtos.LoginParamsDto;
import es.udc.fi.dc.fd.dtos.MessageDetailsDto;
import es.udc.fi.dc.fd.dtos.RateDto;
import es.udc.fi.dc.fd.dtos.RegisterParamsDto;
import es.udc.fi.dc.fd.dtos.ReturnedImageDto;
import es.udc.fi.dc.fd.dtos.ReturnedImagesDto;
import es.udc.fi.dc.fd.dtos.SearchCriteriaDto;
import es.udc.fi.dc.fd.dtos.UpdateProfileInDto;
import es.udc.fi.dc.fd.dtos.UserAuthenticatedDto;
import es.udc.fi.dc.fd.dtos.UserDataDto;
import es.udc.fi.dc.fd.test.utils.EntityTestUtils;

class DtoTests {

	@Test
	void testBlockDto() {
		EntityTestUtils.testEntity(BlockDto.class);
	}

	@Test
	void testBlockGetFriendListDto() {
		EntityTestUtils.testEntity(BlockGetFriendListDto.class);
	}

	@Test
	void testBlockImageByUserIdDto() {
		EntityTestUtils.testEntity(BlockImageByUserIdDto.class);
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
		EntityTestUtils.testEntity(FriendDto.class);
	}

	@Test
	void testGetFriendListOutDto() {
		EntityTestUtils.testEntity(GetFriendListOutDto.class);
	}

	@Test
	void testIdDto() {
		EntityTestUtils.testEntity(IdDto.class);
	}

	@Test
	void testImageCreatedDto() {
		EntityTestUtils.testEntity(ImageCreatedDto.class);
	}

	@Test
	void testImageCreationDto() {
		EntityTestUtils.testEntity(ImageCreationDto.class);
	}

	@Test
	void testImageEditionDto() {
		EntityTestUtils.testEntity(ImageEditionDto.class);
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
	void testReturnedImageDto() {
		EntityTestUtils.testEntity(ReturnedImageDto.class);
	}

	@Test
	void testReturnedImagesDto() {
		EntityTestUtils.testEntity(ReturnedImagesDto.class);
	}

	@Test
	void testSearchCriteriaDto() {
		EntityTestUtils.testEntity(SearchCriteriaDto.class);
	}

	@Test
	void testUpdateProfileInDto() {
		EntityTestUtils.testEntity(UpdateProfileInDto.class);
	}

	@Test
	void testUserAuthenticatedDto() {
		EntityTestUtils.testEntity(UserAuthenticatedDto.class);
	}

	@Test
	void testUserDataDto() {
		EntityTestUtils.testEntity(UserDataDto.class);
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

}
