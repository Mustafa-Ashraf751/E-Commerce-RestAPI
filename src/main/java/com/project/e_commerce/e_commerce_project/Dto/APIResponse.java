package com.project.e_commerce.e_commerce_project.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
public class APIResponse {

private final String message;
private final HttpStatus httpStatus;
private final ZonedDateTime timestamp;


}
