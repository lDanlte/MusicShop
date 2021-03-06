package com.dantonov.musicstore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author Antonov Denis (den007230@gmail.com)
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class RequestDataException extends RuntimeException {

    public RequestDataException() { }

    public RequestDataException(String message) { super(message); }

}
