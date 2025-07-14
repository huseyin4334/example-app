package org.example.routeplaner.userinterface;

import jakarta.validation.ValidationException;
import org.example.routeplaner.domain.exception.RouteDomainException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorController {

    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(Exception.class)
    public ErrorEntity handleException(Exception e) {
        ErrorEntity errorEntity = new ErrorEntity();
        errorEntity.addMessage(e.getMessage());
        return errorEntity;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {MethodArgumentNotValidException.class, BindException.class})
    public ErrorEntity handleValidationException(MethodArgumentNotValidException e) {
        ErrorEntity errorEntity = new ErrorEntity();
        e.getBindingResult().getFieldErrors().forEach(fieldError ->
            errorEntity.addMessage(fieldError.getDefaultMessage()));
        return errorEntity;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {ValidationException.class})
    public ErrorEntity handleValidationException(ValidationException e) {
        ErrorEntity errorEntity = new ErrorEntity();
        errorEntity.addMessage(e.getMessage());
        return errorEntity;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ErrorEntity handleValidationException(IllegalArgumentException e) {
        ErrorEntity errorEntity = new ErrorEntity();
        errorEntity.addMessage(e.getMessage());
        return errorEntity;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RouteDomainException.class)
    public ErrorEntity handleRouteDomainException(RouteDomainException e) {
        ErrorEntity errorEntity = new ErrorEntity();
        errorEntity.addMessage(e.getMessage());
        return errorEntity;
    }
}
