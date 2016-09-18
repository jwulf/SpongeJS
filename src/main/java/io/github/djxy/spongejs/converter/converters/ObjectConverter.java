package io.github.djxy.spongejs.converter.converters;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Object;
import io.github.djxy.spongejs.converter.ConverterInfo;
import io.github.djxy.spongejs.converter.ConverterV8Object;

/**
 * Created by samuelmarchildon-lavoie on 16-09-15.
 */
@ConverterInfo(type = Object.class)
public class ObjectConverter extends ConverterV8Object<Object> {

    @Override
    public void setV8Object(V8Object v8Object, V8 v8, Object o, Long uniqueIdentifier) {
    }

}
