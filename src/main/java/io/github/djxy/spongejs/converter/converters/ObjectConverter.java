package io.github.djxy.spongejs.converter.converters;

import com.eclipsesource.v8.*;
import io.github.djxy.spongejs.converter.Converter;
import io.github.djxy.spongejs.converter.ConverterInfo;
import io.github.djxy.spongejs.converter.ConverterV8Object;

import java.util.UUID;

/**
 * Created by samuelmarchildon-lavoie on 16-09-15.
 */
@ConverterInfo(type = Object.class)
public class ObjectConverter extends ConverterV8Object<Object> {

    @Override
    public void setV8Object(V8Object v8Object, V8 v8, Object o, UUID uniqueIdentifier) {
        v8Object.add("release", registerV8Function(new V8Function(v8, (receiver, parameters) -> {
            receiver.release();
            Converter.releaseRegistredFunctions(uniqueIdentifier);
            return null;
        }), uniqueIdentifier));
    }

}
