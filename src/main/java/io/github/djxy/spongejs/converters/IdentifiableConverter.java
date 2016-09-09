package io.github.djxy.spongejs.converters;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import org.spongepowered.api.util.Identifiable;

/**
 * Created by samuelmarchildon-lavoie on 16-09-09.
 */
public class IdentifiableConverter extends ConverterV8Object<Identifiable> {

    @Override
    protected void setV8Object(V8Object v8Object, V8 v8, Identifiable identifiable) {
        v8Object.add("getUniqueID", new V8Function(v8, (receiver, parameters) -> new UUIDConverter().convertToV8(v8, identifiable.getUniqueId())));
    }

}
