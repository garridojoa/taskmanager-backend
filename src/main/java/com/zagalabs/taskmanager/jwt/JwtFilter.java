package com.zagalabs.taskmanager.jwt;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.TextCodec;

/**
 * JWT Filter to intercept API requests.
 * Purpose of this filter is to intercept each call to the API and validate JWT token.
 * @author jgarrido
 */
@WebFilter(urlPatterns = "/task/*")
public class JwtFilter implements Filter {
	@Value("${jwt.secret}")
	private String secret;
 
	@Override
	public void init(final FilterConfig filterConfig) throws ServletException {}
 
	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
		final HttpServletRequest httpRequest = (HttpServletRequest) request;
		final HttpServletResponse httpResponse = (HttpServletResponse) response;
		final String authHeader = httpRequest.getHeader("authorization");
 
		if (HttpMethod.OPTIONS.toString().equals(httpRequest.getMethod())) {
			httpResponse.setStatus(HttpServletResponse.SC_OK);
			chain.doFilter(httpRequest, httpResponse);
		} else {
 			if (authHeader == null || !authHeader.startsWith("Bearer ")) {
				httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				return;
			}
 
			final String token = authHeader.substring(7);
 			try {
				final Claims claims = Jwts.parser()
						.setSigningKey(TextCodec.BASE64.encode(secret))
						.parseClaimsJws(token)
						.getBody();
				httpRequest.setAttribute("claims", claims);
			} catch (final JwtException jwtExp) {
				httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				return;
			}
 			chain.doFilter(httpRequest, httpResponse);
		}
	}
 
	@Override
	public void destroy() {}
}