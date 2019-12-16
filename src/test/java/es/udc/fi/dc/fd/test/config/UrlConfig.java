/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2019 the original author or authors.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package es.udc.fi.dc.fd.test.config;

/**
 * Contains configuration information for the controller URLs.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 */
public final class UrlConfig {

	public static final String URL_USER_REGISTER_POST = "/backend/users/signUp";

	public static final String URL_USER_LOGIN_POST = "/backend/users/login";

	public static final String URL_USER_GET_USER_DATA = "/backend/users/data";

	public static final String URL_USER_CRITERIA_GET = "/backend/users/searchCriteria";

	public static final String URL_USER_CRITERIA_PUT = "/backend/users/searchCriteria";

	public static final String URL_USER_UPDATEPROFILE_PUT = "/backend/users/updateProfile";

	public static final String URL_USER_RATE_POST = "/backend/users/rate";

	public static final String URL_USER_PREMIUM_PUT = "/backend/users/premium";

	public static final String URL_USER_TOPUSERS_GET = "/backend/users/topUsers";

	public static final String URL_IMAGE_ADD_POST = "/backend/images/add";

	public static final String URL_IMAGE_EDIT_PUT = "/backend/images/edit/{imageId}";

	public static final String URL_IMAGE_REMOVE_DELETE = "/backend/images/remove/{imageId}";

	public static final String URL_IMAGE_GETIMAGESBYID_GET = "/backend/images/carrusel";

	public static final String URL_IMAGE_GETIMAGEBYID_GET = "/backend/images/carrusel/{imageId}";

	public static final String URL_IMAGE_GETFIRST_GET = "/backend/images/first";

	public static final String URL_FRIEND_ACCEPT_POST = "/backend/friends/accept";

	public static final String URL_FRIEND_REJECT_POST = "/backend/friends/reject";

	public static final String URL_FRIEND_SUGGESTION_GET = "/backend/friends/suggestion";

	public static final String URL_FRIEND_FRIENDLIST_GET = "/backend/friends/friendList";

	public static final String URL_FRIEND_BLOCK_POST = "/backend/friends/block";

	public static final String URL_CHAT_GETCONVERSATION_GET = "/backend/chat/conversation";

	public static final String URL_CHAT_FRIENDHEADERS_GET = "/backend/chat/friendHeaders";

	public static final String URL_CHAT_SENDMENSSAGE = "/chat.sendMessage";

	/********
	 * FRONT CONTROLLERS URLS
	 **************/
	public static final String FRONT_CARROUSEL_CHATSCREEN = "/chat/{userId}";
	public static final String FRONT_CARROUSEL_CHATSCREEN_1 = "/chat";

	public static final String FRONT_CARROUSEL_FRIENDFINDER = "/findFriend";
	public static final String FRONT_CARROUSEL_DISPLAYUSERDATA = "/users/profile";
	public static final String FRONT_CARROUSEL_DISPLAYADDIMAGE = "/addImage";
	public static final String FRONT_CARROUSEL_DISPLAYLOGIN = "/carrusel/**";

	/**
	 * Default constructor to avoid initialization.
	 */
	private UrlConfig() {
		super();
	}

}
