package com.alwx.backend.controllers.exceptionHandlers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.alwx.backend.dtos.AppError;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<AppError> handleFormatException(HttpMessageNotReadableException ex) {
        String fieldName = "";
        String message = "Неверный формат данных";
        
        if (ex.getCause() instanceof InvalidFormatException) {
            InvalidFormatException cause = (InvalidFormatException) ex.getCause();
            fieldName = cause.getPath().isEmpty() ? "" : cause.getPath().get(0).getFieldName();
            Class<?> targetType = cause.getTargetType();
            
            message = String.format("Неверный формат поля '%s'. Значение должно быть типа '%s'", 
                fieldName,
                targetType.getSimpleName());
        } else if (ex.getCause() instanceof JsonMappingException) {
            JsonMappingException cause = (JsonMappingException) ex.getCause();
            fieldName = cause.getPath().isEmpty() ? "" : cause.getPath().get(0).getFieldName();
            
            message = String.format("Ошибка в поле '%s': %s", 
                fieldName, 
                cause.getOriginalMessage());
        }
        
        return ResponseEntity
            .badRequest()
            .body(new AppError(
                HttpStatus.BAD_REQUEST.value(),
                message
            ));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<AppError> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String message = "Ошибка при сохранении данных: превышена максимальная длина поля";
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new AppError(HttpStatus.BAD_REQUEST.value(), message));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<AppError> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
            .getAllErrors()
            .stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .collect(Collectors.toList());
            
        return ResponseEntity
            .badRequest()
            .body(new AppError(
                HttpStatus.BAD_REQUEST.value(),
                errors.toString()
            ));
    }
}
