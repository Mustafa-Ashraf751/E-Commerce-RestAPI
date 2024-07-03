package com.project.e_commerce.e_commerce_project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

  private PasswordEncoder passwordEncoder;

  @Autowired
  public SecurityConfig(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  @Bean
  public InMemoryUserDetailsManager userDetailsManager(){
    UserDetails mustafa = User.builder().
            username("mustafa")
            .password(passwordEncoder.encode("123"))
            .roles("ADMIN","USER")
            .build();

    UserDetails ahmed = User.builder().
            username("ahmed")
            .password(passwordEncoder.encode("555"))
            .roles("USER")
            .build();
    return new InMemoryUserDetailsManager(mustafa,ahmed);
  }



  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf().disable()
            .authorizeHttpRequests(configurer ->
                    configurer
                            .requestMatchers(HttpMethod.GET, "/api/**").permitAll() // Allow GET requests to /api/** without authentication
                            .requestMatchers(HttpMethod.POST, "/api/admin/add").hasRole("ADMIN") // Only ADMIN can add
                            .requestMatchers(HttpMethod.POST, "/api/**").authenticated() // All POST requests to /api/** require authentication
                            .requestMatchers(HttpMethod.PUT, "/api/**").authenticated() // All PUT requests to /api/** require authentication
                            .requestMatchers(HttpMethod.DELETE, "/api/**").authenticated() // All DELETE requests to /api/** require authentication
                            .anyRequest().authenticated() // Any other requests require authentication
            )
            .httpBasic(); // Use Basic authentication

    return http.build();
  }

}
