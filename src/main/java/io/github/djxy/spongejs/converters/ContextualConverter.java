package io.github.djxy.spongejs.converters;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.context.Contextual;

/**
 * Created by Samuel on 2016-09-07.
 */
@ConverterInfo(type = Contextual.class)
public class ContextualConverter extends ConverterV8Object<Contextual> {

    @Override
    public void setV8Object(V8Object v8Object, V8 v8, Contextual contextual) {
        v8Object.add("getIdentifier", new V8Function(v8, (receiver, parameters) -> contextual.getIdentifier()));
        v8Object.add("getActiveContexts", new V8Function(v8, (receiver, parameters) -> Converter.convertSetToV8(v8, Context.class, contextual.getActiveContexts())));
    }

}
