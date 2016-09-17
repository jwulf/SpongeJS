package io.github.djxy.spongejs.converter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by samuelmarchildon-lavoie on 16-09-10.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ConverterInfo {
    public Class type();
    public boolean isV8Primitive() default false;//Should be true if return String, Integer, Double, Boolean
}
