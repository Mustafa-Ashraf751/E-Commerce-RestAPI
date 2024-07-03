package com.project.e_commerce.e_commerce_project.exception;

public class APIException extends RuntimeException {


  public APIException() {
  }

  public APIException(String message) {
    super(message);
  }
}
