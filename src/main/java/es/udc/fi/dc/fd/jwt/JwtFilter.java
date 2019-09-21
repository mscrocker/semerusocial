package es.udc.fi.dc.fd.jwt;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class JwtFilter extends BasicAuthenticationFilter {
	
	private JwtGenerator jwtGenerator;

	public JwtFilter(AuthenticationManager authenticationManager, JwtGenerator jwtGenerator) {
		
		super(authenticationManager);
		
		this.jwtGenerator = jwtGenerator;
		
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {

		String authHeaderValue = request.getHeader(HttpHeaders.AUTHORIZATION);
		System.out.println("Si hijo si");
		if (authHeaderValue == null || !authHeaderValue.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}
		
		try {
			
			String serviceToken = authHeaderValue.replace("Bearer ", "");
			JwtInfo jwtInfo = jwtGenerator.getInfo(serviceToken);
			
			request.setAttribute("serviceToken", serviceToken);
			request.setAttribute("userId", jwtInfo.getUserId());
			
			configureSecurityContext(jwtInfo.getUserName());
			
		} catch (Exception e) {
			 response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			 return;
		}
		
		filterChain.doFilter(request, response);
		
	}
	
	private void configureSecurityContext(String userName) {
		
		Set<GrantedAuthority> authorities = new HashSet<>();
				
		SecurityContextHolder.getContext().setAuthentication(
			new UsernamePasswordAuthenticationToken(userName, null, authorities));
		
	}

}
