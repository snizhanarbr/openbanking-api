package com.gdnext.openbanking.exceptions;

import com.gdnext.openbanking.exceptions.InsufficientFundsException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InsufficientFundsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInsufficientFunds(InsufficientFundsException ex) {
        return new ErrorResponse("INSUFFICIENT_FUNDS", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleAll(Exception ex) {
        return new ErrorResponse("INTERNAL_ERROR", "Unexpected error occurred");
    }

    public record ErrorResponse(String code, String message) {}
}