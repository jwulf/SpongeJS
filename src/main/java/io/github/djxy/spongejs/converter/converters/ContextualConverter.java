package io.github.djxy.spongejs.converter.converters;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import io.github.djxy.spongejs.converter.Converter;
import io.github.djxy.spongejs.converter.ConverterInfo;
import io.github.djxy.spongejs.converter.ConverterV8Object;
import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.context.Contextual;

/**
 * Created by Samuel on 2016-09-07.
 */
@ConverterInfo(type = Contextual.class)
public class ContextualConverter extends ConverterV8Object<Contextual> {

    @Override
    public void setV8Object(V8Object v8Object, V8 v8, Contextual contextual, Long uniqueIdentifier) {
        v8Object.add("getIdentifier", registerV8Function(new V8Function(v8, (receiver, parameters) -> contextual.getIdentifier()), uniqueIdentifier));
        v8Object.add("getActiveContexts", registerV8Function(new V8Function(v8, (receiver, parameters) -> Converter.convertIterableToV8(v8, Context.class, contextual.getActiveContexts())), uniqueIdentifier));
    }

}
