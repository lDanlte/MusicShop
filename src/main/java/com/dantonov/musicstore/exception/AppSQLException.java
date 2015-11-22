package com.dantonov.musicstore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author Antonov Denis (den007230@gmail.com)
 */
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class AppSQLException extends RuntimeException {

    public AppSQLException() { }

    public AppSQLException(String message){ super(message); }

    
}
