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

	public static final String URL_USER_REGISTER_POST = "/users/signUp";

	public static final String URL_USER_LOGIN_POST = "/users/login";

	public static final String URL_USER_GET_USER_DATA = "/users/data";

	public static final String URL_USER_CRITERIA_GET = "/users/searchCriteria";

	public static final String URL_USER_CRITERIA_PUT = "/users/searchCriteria";

	public static final String URL_USER_UPDATEPROFILE_PUT = "/users/updateProfile";

	public static final String URL_USER_RATE_POST = "/users/rate";

	public static final String URL_USER_PREMIUM_PUT = "/users/premium";

	public static final String URL_USER_TOPUSERS_GET = "/users/topUsers";

	public static final String URL_IMAGE_ADD_POST = "/images/add";

	public static final String URL_IMAGE_EDIT_PUT = "/images/edit/{imageId}";

	public static final String URL_IMAGE_REMOVE_DELETE = "/images/remove/{imageId}";

	public static final String URL_IMAGE_GETIMAGESBYID_GET = "/images/carrusel";

	public static final String URL_IMAGE_GETIMAGEBYID_GET = "/images/carrusel/{imageId}";

	public static final String URL_IMAGE_GETFIRST_GET = "/images/first";

	public static final String URL_IMAGE_GETANONYMOUS_GET = "/images/anonymousCarrusel";

	public static final String URL_FRIEND_ACCEPT_POST = "/friends/accept";

	public static final String URL_FRIEND_REJECT_POST = "/friends/reject";

	public static final String URL_FRIEND_SUGGESTION_GET = "/friends/suggestion";

	public static final String URL_FRIEND_FRIENDLIST_GET = "/friends/friendList";

	public static final String URL_FRIEND_BLOCK_POST = "/friends/block";

	public static final String URL_FRIEND_SUGGESTCRITERIA_GET = "/friends/suggestNewCriteria";

	public static final String URL_FRIEND_SEARCH_USERS_GET = "/friends/searchUsers";

	public static final String URL_CHAT_GETCONVERSATION_GET = "/chat/conversation";

	public static final String URL_CHAT_FRIENDHEADERS_GET = "/chat/friendHeaders";

	public static final String URL_CHAT_SENDMENSSAGE = "/chat.sendMessage";

	/**
	 * Default constructor to avoid initialization.
	 */
	private UrlConfig() {
		super();
	}

}
