package io.github.djxy.spongejs.converters;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import org.spongepowered.api.service.context.Context;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Samuel on 2016-09-07.
 */
public class ContextConverter extends ConverterV8Object<Context> {

    public V8Array convertSetToV8(V8 v8, Set<Context> contextSet){
        V8Array v8Array = new V8Array(v8);

        for(Context context : contextSet)
            v8Array.push(convertToV8(v8, context));

        return v8Array;
    }

    public Set<Context> convertSetFromV8(Object o){
        Set<Context> contexts = new HashSet<>();
        V8Array v8Array = (V8Array) o;

        for(int i = 0; i < v8Array.length(); i++)
            contexts.add(convertFromV8(v8Array.get(i)));

        return contexts;
    }

    @Override
    protected void setV8Object(V8Object v8Object, V8 v8, Context context) {
        v8Object.add("getKey", new V8Function(v8, (receiver, parameters) -> context.getKey()));
        v8Object.add("getValue", new V8Function(v8, (receiver, parameters) -> context.getValue()));
        v8Object.add("getName", new V8Function(v8, (receiver, parameters) -> context.getName()));
        v8Object.add("getType", new V8Function(v8, (receiver, parameters) -> context.getType()));
        v8Object.add("setValue", new V8Function(v8, (receiver, parameters) -> {
            String lastValue = context.getValue();

            context.setValue(parameters.getString(0));

            return lastValue;
        }));
    }

    @Override
    public Context convertFromV8(Object o){
        V8Object v8Object = (V8Object) o;

        return new Context(v8Object.getString("type"), v8Object.getString("name"));
    }

}
