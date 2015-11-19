package com.dantonov.musicstore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author Antonov Denis (den007230@gmail.com)
 */
@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class UnauthorizedPageException extends RuntimeException {

    public UnauthorizedPageException() { }

    public UnauthorizedPageException(String message) { super(message); }

    
}
