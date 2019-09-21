package es.udc.fi.dc.fd.controller;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import es.udc.fi.dc.fd.jwt.JwtFilter;
import es.udc.fi.dc.fd.jwt.JwtGenerator;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter  {
	
	
	private JwtGenerator jwtGenerator;
	
	
	@Autowired
	public SecurityConfig(JwtGenerator jwtGenerator){
		super();
		
        this.jwtGenerator = checkNotNull(jwtGenerator,
                "Received a null pointer as jwtGenerator");
        
		
	}
	
	 @Override
	 public AuthenticationManager authenticationManagerBean() throws Exception {
	     return super.authenticationManagerBean();
	 }
	
	@Override
	   protected void configure(HttpSecurity httpSecurity) throws Exception {
	
			httpSecurity.cors().and().csrf().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
			.addFilter(new JwtFilter(authenticationManagerBean(), jwtGenerator))
			.authorizeRequests()
			.antMatchers("/**").permitAll();
	}
	

	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		
		CorsConfiguration config = new CorsConfiguration();
	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		
		config.setAllowCredentials(true);
	    config.addAllowedOrigin("*");
	    config.addAllowedHeader("*");
	    config.addAllowedMethod("*");
	    
	    source.registerCorsConfiguration("/**", config);
	    
	    return source;
	    
	 }

}
