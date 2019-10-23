package com.example.basicjwtfilter.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.example.basicjwtfilter.properties.JwtProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class JwtService {

  @Autowired private JwtProperties jwtProperties;
  private Algorithm algorithm;
  private JWTVerifier jwtVerifier;

  @PostConstruct
  public void init() {
    this.algorithm = Algorithm.HMAC512(jwtProperties.getSecret());
    this.jwtVerifier = JWT.require(algorithm).withIssuer(jwtProperties.getIssuer()).build();
  }

  public String generate(String name) {
    return JWT.create().withIssuer(jwtProperties.getIssuer()).withSubject(name).sign(algorithm);
  }

  public DecodedJWT verify(String jwt) {
    return this.jwtVerifier.verify(jwt);
  }
}
