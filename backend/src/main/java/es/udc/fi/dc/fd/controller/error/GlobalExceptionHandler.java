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

package es.udc.fi.dc.fd.controller.error;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import es.udc.fi.dc.fd.controller.error.ErrorViewConstants;

/**
 * Captures and handles exceptions for all the controllers.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 */
@ControllerAdvice
public final class GlobalExceptionHandler
	extends AbstractHandlerExceptionResolver {

	/**
	 * Logger for the exception handler.
	 */
	private static final Logger LOGGER = LoggerFactory
		.getLogger(GlobalExceptionHandler.class);

	/**
	 * Default constructor.
	 */
	public GlobalExceptionHandler() {
		super();
	}

	@Override
	protected ModelAndView doResolveException(final HttpServletRequest request,
											  final HttpServletResponse response, final Object handler,
											  final Exception ex) {
		final ModelAndView modelView;

		LOGGER.error(ex.getMessage(), ex);

		modelView = new ModelAndView(ErrorViewConstants.VIEW_ERROR);
		modelView.getModel().put("code",
			HttpStatus.INTERNAL_SERVER_ERROR.value());
		modelView.getModel().put("message", ex.getMessage());

		return modelView;
	}

}
