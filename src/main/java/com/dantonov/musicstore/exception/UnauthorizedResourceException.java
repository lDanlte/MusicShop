package com.dantonov.musicstore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author Antonov Denis (den007230@gmail.com)
 */
@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class UnauthorizedResourceException extends RuntimeException {

    public UnauthorizedResourceException() { }

    public UnauthorizedResourceException(String message) { super(message); }

}
