package br.com.desafio.exception;

public class BadRequestAlertException extends RuntimeException {

    public BadRequestAlertException(String message) {
        super(message);
    }

}
