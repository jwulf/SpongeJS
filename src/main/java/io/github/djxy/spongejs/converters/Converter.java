package io.github.djxy.spongejs.converters;

import com.eclipsesource.v8.V8;

/**
 * Created by Samuel on 2016-09-07.
 */
public abstract class Converter<V, T> {

    abstract public T convertToV8(V8 v8, V v);

    public V convertFromV8(Object o){
        return null;
    }

}
