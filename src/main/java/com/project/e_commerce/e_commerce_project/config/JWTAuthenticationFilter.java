package com.project.e_commerce.e_commerce_project.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {

  private final JWTService jwtService;
  private final UserDetailsService userDetailsService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
          throws ServletException, IOException {
     final String authHeader = request.getHeader("Authorization");
     final String jwt;
     final String userEmail;
     if(authHeader == null || !authHeader.startsWith("Bearer ")){
       filterChain.doFilter(request,response);
       return;
     }
     jwt = authHeader.substring(7);
     userEmail = jwtService.extractUserName(jwt);
     if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){
       UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
       if(jwtService.isTokenValid(jwt,userDetails)){
         UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                 userDetails.getUsername(),
                 null,
                 userDetails.getAuthorities()
         );
         authToken.setDetails(
                 new WebAuthenticationDetailsSource().buildDetails(request)
         );
         SecurityContextHolder.getContext().setAuthentication(authToken);
       }else{
         throw new BadCredentialsException("Token is not valid");
       }
     }

     filterChain.doFilter(request,response);
  }
}
