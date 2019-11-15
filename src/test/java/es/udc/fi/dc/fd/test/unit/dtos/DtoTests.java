package es.udc.fi.dc.fd.test.unit.dtos;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import es.udc.fi.dc.fd.dtos.*;
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
}
