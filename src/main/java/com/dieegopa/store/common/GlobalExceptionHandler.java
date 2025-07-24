package com.dieegopa.store.common;

import com.dieegopa.store.carts.CartEmptyException;
import com.dieegopa.store.carts.CartNotFoundException;
import com.dieegopa.store.orders.OrderNotFoundException;
import com.dieegopa.store.payments.PaymentException;
import com.dieegopa.store.products.ProductNotFoundException;
import com.dieegopa.store.users.DuplicateUserException;
import com.dieegopa.store.users.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
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
        return ResponseEntity.badRequest().body(
                new ErrorDto("Product not found in the cart")
        );
    }

    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<Void> handleBadCredentialsException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<ErrorDto> handleUnreadableMessage() {
        return ResponseEntity.badRequest().body(
                new ErrorDto("Malformed JSON request")
        );
    }

    @ExceptionHandler({CartEmptyException.class})
    public ResponseEntity<ErrorDto> handleCartEmptyException(CartEmptyException e) {
        return ResponseEntity.badRequest().body(
                new ErrorDto(e.getMessage())
        );
    }

    @ExceptionHandler({OrderNotFoundException.class})
    public ResponseEntity<ErrorDto> handleOrderNotFoundException() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorDto("Order not found")
        );
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<ErrorDto> handleAccessDeniedException(
            AccessDeniedException e
    ) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                new ErrorDto(e.getMessage())
        );
    }

    @ExceptionHandler({PaymentException.class})
    public ResponseEntity<ErrorDto> handlePaymentException(PaymentException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ErrorDto("Error creating checkout session: " + e.getMessage())
        );
    }

    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<ErrorDto> handleDuplicateUser() {
        return ResponseEntity.badRequest().body(
                new ErrorDto("User with this email already exists")
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Void> handleUserNotFound() {
        return ResponseEntity.notFound().build();
    }
}
