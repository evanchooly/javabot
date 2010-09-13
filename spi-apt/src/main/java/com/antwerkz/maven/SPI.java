package com.antwerkz.maven;

public @interface SPI {
    Class[] value() default {};
}