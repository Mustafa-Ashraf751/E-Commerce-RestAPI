package com.project.e_commerce.e_commerce_project.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
public class ValidateResponse {
  private final Map<String,String> message;
  private final HttpStatus httpStatus;
  private final ZonedDateTime zonedDateTime;
}
