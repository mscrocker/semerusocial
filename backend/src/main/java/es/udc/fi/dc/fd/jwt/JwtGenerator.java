package es.udc.fi.dc.fd.jwt;

public interface JwtGenerator {

	String generate(JwtInfo info);

	JwtInfo getInfo(String token);

}
