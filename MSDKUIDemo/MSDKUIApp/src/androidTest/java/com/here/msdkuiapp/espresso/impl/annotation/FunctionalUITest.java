package com.here.msdkuiapp.espresso.impl.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * FunctionalUITest annotation for tests to be executed for CI jobs.
 * The annotation for functional tests to be executed as a separate job!!!
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface FunctionalUITest { }

