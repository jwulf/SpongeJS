package io.github.djxy.spongejs.converter.converters;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import io.github.djxy.spongejs.converter.Converter;
import io.github.djxy.spongejs.converter.ConverterInfo;
import io.github.djxy.spongejs.converter.ConverterV8Object;
import org.spongepowered.api.util.Identifiable;

import java.util.UUID;

/**
 * Created by samuelmarchildon-lavoie on 16-09-09.
 */
@ConverterInfo(type = Identifiable.class)
public class IdentifiableConverter extends ConverterV8Object<Identifiable> {

    @Override
    public void setV8Object(V8Object v8Object, V8 v8, Identifiable identifiable, Long uniqueIdentifier) {
        v8Object.add("getUniqueId", registerV8Function(new V8Function(v8, (receiver, parameters) -> Converter.convertToV8(v8, UUID.class, identifiable.getUniqueId())), uniqueIdentifier));
    }

}
