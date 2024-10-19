package com.wizard.templateController;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.wizard.exceptions.EmailAlreadyExistsException;
import com.wizard.exceptions.RuoloNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)  // Imposta lo stato HTTP 409 per conflitto
    public String handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        return ex.getMessage();  // Puoi anche restituire un oggetto con più dettagli
    }

    @ExceptionHandler(RuoloNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)  // Imposta lo stato HTTP 404
    public String handleRuoloNotFoundException(RuoloNotFoundException ex) {
        return ex.getMessage();  // Puoi restituire un oggetto dettagliato se necessario
    }

    // Gestione generica delle altre eccezioni
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGenericException(Exception ex) {
        return "Errore generico: " + ex.getMessage();
    }


    // Gestione degli errori di inserimento dati
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> handleValidationExceptions(MethodArgumentNotValidException errore) {
        // Estrai i messaggi di errore predefiniti
        List<String> errorMessages = errore.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getDefaultMessage()) // Estrai solo il messaggio predefinito
                .collect(Collectors.toList());

        // Restituisci i messaggi di errore con lo stato HTTP 400 Bad Request
        return new ResponseEntity<>(errorMessages, HttpStatus.BAD_REQUEST);
    }
}