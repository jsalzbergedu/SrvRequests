package com.ncsurobotics.srvrequests;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.immutables.value.Value;

/*
 * Style definition boilerplate taken from
 * http://immutables.github.io/style.html
 */

/**
 * A common style for all the objects.
 * @author Jacob Salzberg
 */
@Target({ElementType.PACKAGE, ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
@Value.Style(stagedBuilder = true)
public @interface DefaultStyle {
}
