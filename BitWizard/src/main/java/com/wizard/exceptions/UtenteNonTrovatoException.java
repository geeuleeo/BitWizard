package com.wizard.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UtenteNonTrovatoException extends RuntimeException {
    public UtenteNonTrovatoException(String message) {
        super(message);
    }
}
