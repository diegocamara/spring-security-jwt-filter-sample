package com.example.basicjwtfilter.security.model;

import lombok.Data;

@Data
public class LoginRequest {

  private String username;
  private String password;
}
