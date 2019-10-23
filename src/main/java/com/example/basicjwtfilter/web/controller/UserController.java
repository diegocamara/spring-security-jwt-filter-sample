package com.example.basicjwtfilter.web.controller;

import com.example.basicjwtfilter.web.model.UserResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

  @GetMapping
  public UserResponse userResponse() {
    return new UserResponse("username");
  }
}
