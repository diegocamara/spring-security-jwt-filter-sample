package com.example.basicjwtfilter.security.handler;

import com.example.basicjwtfilter.security.model.LoginResponse;
import com.example.basicjwtfilter.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

  @Autowired private JwtService jwtService;

  @Autowired private ObjectMapper objectMapper;

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse,
      Authentication authentication)
      throws IOException, ServletException {

    httpServletResponse.setStatus(HttpServletResponse.SC_OK);
    httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
    httpServletResponse.setCharacterEncoding("UTF-8");
    LoginResponse loginResponse = new LoginResponse(jwtService.generate(authentication.getName()));
    try (PrintWriter writer = httpServletResponse.getWriter()) {
      writer.println(objectMapper.writeValueAsString(loginResponse));
    }
  }
}
