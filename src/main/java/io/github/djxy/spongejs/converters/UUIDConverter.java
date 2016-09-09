package io.github.djxy.spongejs.converters;

import com.eclipsesource.v8.V8;

import java.util.UUID;

/**
 * Created by Samuel on 2016-09-07.
 */
public class UUIDConverter extends Converter<UUID, String> {

    @Override
    public String convertToV8(V8 v8, UUID uuid) {
        return uuid.toString();
    }

    @Override
    public UUID convertFromV8(Object o) {
        return UUID.fromString((String) o);
    }

}
