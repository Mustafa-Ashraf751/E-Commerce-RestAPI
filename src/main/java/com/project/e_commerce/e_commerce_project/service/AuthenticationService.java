package com.project.e_commerce.e_commerce_project.service;

import com.project.e_commerce.e_commerce_project.config.JWTService;
import com.project.e_commerce.e_commerce_project.controller.AuthenticationResponse;
import com.project.e_commerce.e_commerce_project.controller.LoginRequest;
import com.project.e_commerce.e_commerce_project.controller.RegisterRequest;
import com.project.e_commerce.e_commerce_project.entity.Role;
import com.project.e_commerce.e_commerce_project.entity.User;
import com.project.e_commerce.e_commerce_project.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationService {


  private final UserRepo userRepo;
  private final PasswordEncoder passwordEncoder;
  private final JWTService jwtService;
  private final AuthenticationManager authenticationManager;

  public AuthenticationResponse register(RegisterRequest request) {
    Role userRole = new Role();
    userRole.setRoleName("USER");
    Set<Role> roles = new HashSet<>();
    roles.add(userRole);
    User user = User.builder()
    .firstName(request.getFirstName())
    .lastName(request.getLastName())
    .email(request.getEmail())
    .password(passwordEncoder.encode(request.getPassword()))
    .roles(roles)
    .build();
    userRepo.save(user);
    String jwtToken = jwtService.generateToken(user);
    return AuthenticationResponse.builder()
            .token(jwtToken)
            .build();
  }

  public AuthenticationResponse login(LoginRequest request) {
    authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(
              request.getEmail(),
              request.getPassword()
      )
    );
    User user = userRepo.findUserByEmail(request.getEmail()).
            orElseThrow();
    String jwtToken = jwtService.generateToken(user);
    return AuthenticationResponse.builder()
            .token(jwtToken)
            .build();
  }
}
