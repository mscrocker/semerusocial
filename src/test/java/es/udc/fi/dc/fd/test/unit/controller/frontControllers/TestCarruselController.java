package es.udc.fi.dc.fd.test.unit.controller.frontControllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import es.udc.fi.dc.fd.controller.CarruselController;
import es.udc.fi.dc.fd.test.config.UrlConfig;

@RunWith(JUnitPlatform.class)
public class TestCarruselController {

	private MockMvc mockMvc;

	public TestCarruselController() {
		super();
	}

	private final CarruselController getController() {
		return new CarruselController();
	}

	@BeforeEach
	public void setup() {
		final InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/jsp/view/");
		viewResolver.setSuffix(".jsp");

		mockMvc = MockMvcBuilders.standaloneSetup(getController()).setViewResolvers(viewResolver).build();
	}

	/*************** TESTS **************************************/

	@Test
	public void TestChatScreen() throws Exception {

		// @formatter:off
		mockMvc.perform(get(UrlConfig.FRONT_CARROUSEL_CHATSCREEN, 1)).andExpect(status().isOk());
		// @formatter:on

	}

	@Test
	public void TestChatScreen1() throws Exception {

		// @formatter:off
		mockMvc.perform(get(UrlConfig.FRONT_CARROUSEL_CHATSCREEN_1)).andExpect(status().isOk());
		// @formatter:on

	}

	@Test
	public void TestDisplayFriendFinder() throws Exception {

		// @formatter:off
		mockMvc.perform(get(UrlConfig.FRONT_CARROUSEL_FRIENDFINDER)).andExpect(status().isOk());
		// @formatter:on

	}

	@Test
	public void TestDisplayUserData() throws Exception {

		// @formatter:off
		mockMvc.perform(get(UrlConfig.FRONT_CARROUSEL_DISPLAYUSERDATA)).andExpect(status().isOk());
		// @formatter:on

	}

	@Test
	public void TestDisplayAddImage() throws Exception {

		// @formatter:off
		mockMvc.perform(get(UrlConfig.FRONT_CARROUSEL_DISPLAYADDIMAGE)).andExpect(status().isOk());
		// @formatter:on

	}

	@Test
	public void TestDisplayLogin() throws Exception {

		// @formatter:off
		mockMvc.perform(get(UrlConfig.FRONT_CARROUSEL_DISPLAYLOGIN, 1)).andExpect(status().isOk());
		// @formatter:on

	}

}
