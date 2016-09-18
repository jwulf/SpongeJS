package io.github.djxy.spongejs.converter;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Object;

/**
 * Created by Samuel on 2016-09-07.
 */
public abstract class ConverterV8Object<V> extends Converter<V, V8Object> {

    public abstract void setV8Object(V8Object v8Object, V8 v8, V v, Long uniqueIdentifier);

    @Override
    public V8Object convertToV8(V8 v8, V v){
        V8Object v8Object = new V8Object(v8);

        setV8Object(v8Object, v8, v, v8Object.getHandle());

        return v8Object;
    }

}
