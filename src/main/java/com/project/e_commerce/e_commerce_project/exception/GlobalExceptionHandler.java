package com.project.e_commerce.e_commerce_project.exception;

import com.project.e_commerce.e_commerce_project.Dto.APIResponse;
import com.project.e_commerce.e_commerce_project.Dto.ValidateResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<APIResponse> myResourceNotFoundException(ResourceNotFoundException exception){
      APIResponse res = new APIResponse(
              exception.getMessage(),
              HttpStatus.NOT_FOUND,
              ZonedDateTime.now()
      );
    return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(APIException.class)
  public ResponseEntity<APIResponse> myAPIException(APIException e){
    APIResponse res = new APIResponse(
            e.getMessage(),
            HttpStatus.BAD_REQUEST,
            ZonedDateTime.now()
    );
    return new ResponseEntity<>(res,HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ValidateResponse> myMethodArgumentNotValidException(MethodArgumentNotValidException e){
    Map<String,String> map = new HashMap<>();
       e.getBindingResult().getAllErrors().forEach(error ->{
         String fieldName =  ((FieldError)error).getField();
         String message = error.getDefaultMessage();
         map.put(fieldName,message);
       });
       ValidateResponse res = new ValidateResponse(
               map,
               HttpStatus.BAD_REQUEST,
               ZonedDateTime.now()
       );
     return new ResponseEntity<>(res,HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ValidateResponse> myConstraintViolationException(ConstraintViolationException e){
    Map<String,String> map = new HashMap<>();
    e.getConstraintViolations().forEach(violation ->{
      String fieldName =  violation.getPropertyPath().toString();
      String message = violation.getMessage();
      map.put(fieldName,message);
    });
    ValidateResponse res = new ValidateResponse(
            map,
            HttpStatus.BAD_REQUEST,
            ZonedDateTime.now()
    );
    return new ResponseEntity<>(res,HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MissingPathVariableException.class)
  public ResponseEntity<APIResponse> myAPIException(MissingPathVariableException e){
    APIResponse res = new APIResponse(
            e.getMessage(),
            HttpStatus.BAD_REQUEST,
            ZonedDateTime.now()
    );
    return new ResponseEntity<>(res,HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<APIResponse> myAPIException(DataIntegrityViolationException e){
    APIResponse res = new APIResponse(
            e.getMessage(),
            HttpStatus.BAD_REQUEST,
            ZonedDateTime.now()
    );
    return new ResponseEntity<>(res,HttpStatus.BAD_REQUEST);
  }


}
