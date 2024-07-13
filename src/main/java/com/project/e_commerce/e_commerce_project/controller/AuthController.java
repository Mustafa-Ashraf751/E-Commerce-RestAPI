package com.project.e_commerce.e_commerce_project.controller;

import com.project.e_commerce.e_commerce_project.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

  private final AuthenticationService authenticationService;

  @PostMapping("/register")
  public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request){
   return ResponseEntity.ok(authenticationService.register(request));
  }


  @PostMapping("/login")
  public ResponseEntity<AuthenticationResponse> register(@RequestBody LoginRequest request){
    return ResponseEntity.ok(authenticationService.login(request));
  }

}
