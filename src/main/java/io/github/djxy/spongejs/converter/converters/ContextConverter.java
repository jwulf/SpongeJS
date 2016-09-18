package io.github.djxy.spongejs.converter.converters;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import io.github.djxy.spongejs.converter.ConverterInfo;
import io.github.djxy.spongejs.converter.ConverterV8Object;
import org.spongepowered.api.service.context.Context;

/**
 * Created by Samuel on 2016-09-07.
 */
@ConverterInfo(type = Context.class)
public class ContextConverter extends ConverterV8Object<Context> {

    @Override
    public void setV8Object(V8Object v8Object, V8 v8, Context context, Long uniqueIdentifier) {
        v8Object.add("getKey", registerV8Function(new V8Function(v8, (receiver, parameters) -> context.getKey()), uniqueIdentifier));
        v8Object.add("getValue", registerV8Function(new V8Function(v8, (receiver, parameters) -> context.getValue()), uniqueIdentifier));
        v8Object.add("getName", registerV8Function(new V8Function(v8, (receiver, parameters) -> context.getName()), uniqueIdentifier));
        v8Object.add("getType", registerV8Function(new V8Function(v8, (receiver, parameters) -> context.getType()), uniqueIdentifier));
        v8Object.add("setValue", registerV8Function(new V8Function(v8, (receiver, parameters) -> {
            String lastValue = context.getValue();

            context.setValue(parameters.getString(0));

            return lastValue;
        }), uniqueIdentifier));
    }

    @Override
    public Context convertFromV8(Object o){
        V8Object v8Object = (V8Object) o;

        return new Context(v8Object.getString("type"), v8Object.getString("name"));
    }

}
