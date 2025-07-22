package com.dieegopa.store.controllers;

import com.dieegopa.store.dtos.ErrorDto;
import com.dieegopa.store.exceptions.CartEmptyException;
import com.dieegopa.store.exceptions.CartNotFoundException;
import com.dieegopa.store.exceptions.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException e) {
        var errors = new HashMap<String, String>();

        e.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler({CartNotFoundException.class})
    public ResponseEntity<ErrorDto> handleCartNotFoundException() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorDto("Cart not found")
        );
    }

    @ExceptionHandler({ProductNotFoundException.class})
    public ResponseEntity<ErrorDto> handleProductNotFoundException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorDto("Product not found in the cart")
        );
    }

    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<Void> handleBadCredentialsException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<ErrorDto> handleUnreadableMessage() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorDto("Malformed JSON request")
        );
    }

    @ExceptionHandler({CartEmptyException.class})
    public ResponseEntity<ErrorDto> handleCartEmptyException(CartEmptyException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorDto(e.getMessage())
        );
    }
}
