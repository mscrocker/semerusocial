package es.udc.fi.dc.fd.jwt;

public interface JwtGenerator {
	
	public String generate(JwtInfo info);
	
	public JwtInfo getInfo(String token);

}
