package es.udc.fi.dc.fd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import es.udc.fi.dc.fd.jwt.JwtFilter;
import es.udc.fi.dc.fd.jwt.JwtGenerator;
import es.udc.fi.dc.fd.jwt.JwtGeneratorImpl;

@Configuration
@EnableWebSecurity
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private JwtGenerator jwtGenerator;
	
	@Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
	
	public SecurityConfig() {
		//this.jwtGenerator = new JwtGeneratorImpl();
	}
	
	@Override
	@Autowired
	protected void configure(AuthenticationManagerBuilder auth) {
		
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		/*
		http.cors().and().csrf().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
			.addFilter(new JwtFilter(authenticationManagerBean(), jwtGenerator))
			.authorizeRequests()
			.antMatchers("/users/signUp").permitAll()
			.antMatchers("/users/login").permitAll()
			.antMatchers("/users/loginFromServiceToken").permitAll()
			.antMatchers("/catalog/auctions").permitAll()
			.antMatchers("/catalog/auctions/*").permitAll()
			.antMatchers("/catalog/categories").permitAll()
			.antMatchers("/**").hasRole("USER");
		*/
		/*
		http.cors().and().csrf().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
			.addFilter(new JwtFilter(authenticationManagerBean(), jwtGenerator))
			.authorizeRequests()
			.antMatchers("/**").permitAll();
		*/
		http.addFilter(new JwtFilter(authenticationManagerBean(), jwtGenerator))
		.authorizeRequests()
		.antMatchers("/**").denyAll();

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
