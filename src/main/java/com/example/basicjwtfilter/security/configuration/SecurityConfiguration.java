package com.example.basicjwtfilter.security.configuration;

import com.example.basicjwtfilter.security.filter.JsonAuthenticationFilter;
import com.example.basicjwtfilter.security.filter.JwtFilter;
import com.example.basicjwtfilter.security.provider.CustomAuthenticationProvider;
import com.example.basicjwtfilter.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Autowired private CustomAuthenticationProvider customAuthenticationProvider;
  @Autowired private AuthenticationSuccessHandler authenticationSuccessHandler;
  @Autowired private JwtService jwtService;
  @Autowired private ObjectMapper objectMapper;

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    //    auth.authenticationProvider(customAuthenticationProvider);
    auth.inMemoryAuthentication()
        .withUser("user")
        .password(passwordEncoder().encode("user123"))
        .roles("USER", "ADMIN");
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf()
        .disable()
        .authorizeRequests()
        .anyRequest()
        .authenticated()
        .and()
        .exceptionHandling()
        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
        .and()
        .addFilter(authenticationFilter())
        .addFilterBefore(
            new JwtFilter(authenticationManager(), jwtService),
            UsernamePasswordAuthenticationFilter.class)
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public JsonAuthenticationFilter authenticationFilter() throws Exception {
    JsonAuthenticationFilter authenticationFilter =
        new JsonAuthenticationFilter(objectMapper, passwordEncoder());
    authenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
    authenticationFilter.setRequiresAuthenticationRequestMatcher(
        new AntPathRequestMatcher("/login", "POST"));
    authenticationFilter.setAuthenticationManager(authenticationManagerBean());
    return authenticationFilter;
  }
}
