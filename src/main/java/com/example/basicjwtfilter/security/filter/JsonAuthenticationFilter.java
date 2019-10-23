package com.example.basicjwtfilter.security.filter;

import com.example.basicjwtfilter.security.model.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JsonAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private ObjectMapper objectMapper;
  private PasswordEncoder passwordEncoder;

  public JsonAuthenticationFilter(ObjectMapper objectMapper, PasswordEncoder passwordEncoder) {
    this.objectMapper = objectMapper;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public Authentication attemptAuthentication(
      HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = null;

    try {
      LoginRequest loginRequest = objectMapper.readValue(request.getReader(), LoginRequest.class);
      usernamePasswordAuthenticationToken =
          new UsernamePasswordAuthenticationToken(
              loginRequest.getUsername(), loginRequest.getPassword());
    } catch (IOException ex) {
      logger.debug(ex.getMessage());
    }

    return this.getAuthenticationManager().authenticate(usernamePasswordAuthenticationToken);
  }
}
