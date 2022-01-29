package com.scheduler.model;

import dagger.MapKey;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * @author Serhii_Udaltsov on 4/17/2021
 */
@Documented
@Target(ElementType.METHOD)
@MapKey
public @interface CommandKey {
    CommandType value();
}
