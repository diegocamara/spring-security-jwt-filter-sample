package com.example.basicjwtfilter.security.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.basicjwtfilter.service.JwtService;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JwtFilter extends BasicAuthenticationFilter {

  private static final String TOKEN_PREFIX = "Bearer ";
  private JwtService jwtService;

  public JwtFilter(AuthenticationManager authenticationManager, JwtService jwtService) {
    super(authenticationManager);
    this.jwtService = jwtService;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    Authentication authentication = getAuthentication(request);
    if (authentication != null) {
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    chain.doFilter(request, response);
    SecurityContextHolder.getContext().setAuthentication(null);
  }

  private Authentication getAuthentication(HttpServletRequest request) {
    Authentication authentication = null;
    try {
      Optional<String> tokenOptional = getToken(request);
      if (tokenOptional.isPresent()) {
        String jwt = tokenOptional.get();
        if (StringUtils.hasText(jwt)) {
          DecodedJWT decodedJWT = jwtService.verify(jwt);
          String username = decodedJWT.getSubject();
          authentication = new UsernamePasswordAuthenticationToken(username, null, null);
        }
      }
    } catch (JWTVerificationException jwtVerificationException) {
      logger.debug(jwtVerificationException.getMessage());
    }
    return authentication;
  }

  private Optional<String> getToken(HttpServletRequest httpServletRequest) {
    Optional<String> tokenOptional = Optional.empty();
    String authorizationHeaderValue = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
    if (StringUtils.hasText(authorizationHeaderValue)) {
      String token = authorizationHeaderValue.replace(TOKEN_PREFIX, "");
      tokenOptional = Optional.of(token);
    }
    return tokenOptional;
  }

  private Optional<Cookie> getCookie(Cookie[] cookies, String cookieName) {
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (cookieName.equalsIgnoreCase(cookie.getName())) {
          return Optional.of(cookie);
        }
      }
    }

    return Optional.empty();
  }
}
