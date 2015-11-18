package com.dantonov.musicstore.annotation;

import com.dantonov.musicstore.util.RoleEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Antonov Denis (den007230@gmail.com)
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Secured {
    RoleEnum role() default RoleEnum.USER;
}
