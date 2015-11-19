package com.dantonov.musicstore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author Antonov Denis (den007230@gmail.com)
 */

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Page not found")
public class PageNotFoundException extends RuntimeException {

    public PageNotFoundException() { }

    public PageNotFoundException(String message) { super(message); }

}
