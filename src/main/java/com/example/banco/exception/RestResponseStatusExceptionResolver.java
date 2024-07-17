package com.example.banco.exception;

import com.example.banco.entities.ApiError;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestResponseStatusExceptionResolver {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiError> genericException(Exception ex) {
    ApiError apiError =
        ApiError.builder()
            .timestamp(LocalDateTime.now())
            .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .status(HttpStatus.INTERNAL_SERVER_ERROR.name())
            .errors(List.of(ex.getMessage()))
            .build();
    return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(BussinesException.class)
  public ResponseEntity<ApiError> bussinesException(RuntimeException ex) {
    ApiError apiError =
        ApiError.builder()
            .timestamp(LocalDateTime.now())
            .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .status(HttpStatus.INTERNAL_SERVER_ERROR.name())
            .errors(List.of(ex.getMessage()))
            .build();
    return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(InsufficientFundsException.class)
  public ResponseEntity<ApiError> insufficientFundsException(RuntimeException ex) {
    ApiError apiError =
        ApiError.builder()
            .timestamp(LocalDateTime.now())
            .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .status(HttpStatus.INTERNAL_SERVER_ERROR.name())
            .errors(List.of(ex.getMessage()))
            .build();
    return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
