package io.github.djxy.spongejs.converters;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Object;

/**
 * Created by Samuel on 2016-09-07.
 */
public abstract class ConverterV8Object<V> extends Converter<V, V8Object> {

    abstract protected void setV8Object(V8Object v8Object, V8 v8, V v);

    public V8Object convertToV8(V8 v8, V v){
        V8Object v8Object = new V8Object(v8);

        setV8Object(v8Object, v8, v);

        return v8Object;
    }

}
