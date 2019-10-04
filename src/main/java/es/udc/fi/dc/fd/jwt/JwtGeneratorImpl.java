package es.udc.fi.dc.fd.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtGeneratorImpl implements JwtGenerator {

	private String signKey = "Bu:GW8bgPlEw";

	private long expirationMinutes = 1440;

	@Override
	public String generate(JwtInfo info) {

		Claims claims = Jwts.claims();

		claims.setSubject(info.getUserName())
				.setExpiration(new Date(System.currentTimeMillis() + expirationMinutes * 60 * 1000));
		claims.put("userId", info.getUserId());

		return Jwts.builder().setClaims(claims)
				.signWith(SignatureAlgorithm.HS512, signKey.getBytes(StandardCharsets.UTF_8)).compact();

	}

	@Override
	public JwtInfo getInfo(String token) {

		Claims claims = Jwts.parser().setSigningKey(signKey.getBytes(StandardCharsets.UTF_8)).parseClaimsJws(token)
				.getBody();

		return new JwtInfo(((Integer) claims.get("userId")).longValue(), claims.getSubject());

	}

}
