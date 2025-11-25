package com.example.agenda.handler; // Crie um novo pacote

import com.example.agenda.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
        // Retorna o status 404 NOT FOUND com a mensagem da exceção
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}