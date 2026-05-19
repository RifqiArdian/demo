package com.example.demo.web.exception;

import com.example.demo.application.exception.AuthenticationFailedException;
import com.example.demo.application.exception.UsernameAlreadyUsedException;
import com.example.demo.web.dto.WebResponse;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public WebResponse<String> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex) {
        return WebResponse.<String>builder()
                .message("Method " + ex.getMethod() + " tidak diizinkan untuk URL ini. Gunakan method yang benar.")
                .build();
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public WebResponse<String> handleNotFound(NoHandlerFoundException ex) {
        return WebResponse.<String>builder()
                .message("Halaman atau API yang anda tuju tidak ditemukan.")
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public WebResponse<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage())
        );
        return WebResponse.<Map<String, String>>builder()
                .message("Validasi gagal")
                .data(errors)
                .build();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public WebResponse<String> handleReadableException(HttpMessageNotReadableException ex) {
        return WebResponse.<String>builder()
                .message("Format JSON tidak valid atau body kosong")
                .build();
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<WebResponse<String>> handleResponseStatusException(ResponseStatusException ex) {
        WebResponse<String> response = WebResponse.<String>builder()
                .message(ex.getReason())
                .build();
        return new ResponseEntity<>(response, ex.getStatusCode());
    }

    @ExceptionHandler(UsernameAlreadyUsedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public WebResponse<String> handleUsernameAlreadyUsedException(UsernameAlreadyUsedException ex) {
        return WebResponse.<String>builder()
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(AuthenticationFailedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public WebResponse<String> handleAuthenticationFailedException(AuthenticationFailedException ex) {
        return WebResponse.<String>builder()
                .message(ex.getMessage())
                .build();
    }
}