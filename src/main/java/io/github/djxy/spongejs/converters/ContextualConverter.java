package io.github.djxy.spongejs.converters;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.context.Contextual;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Samuel on 2016-09-07.
 */
public class ContextualConverter extends ConverterV8Object<Contextual> {

    private static final ContextConverter contextConverter = new ContextConverter();

    @Override
    protected void setV8Object(V8Object v8Object, V8 v8, Contextual contextual) {
        v8Object.add("getIdentifier", new V8Function(v8, (receiver, parameters) -> contextual.getIdentifier()));
        v8Object.add("getActiveContexts", new V8Function(v8, (receiver, parameters) -> contextConverter.convertSetToV8(v8, contextual.getActiveContexts())));
    }

}
