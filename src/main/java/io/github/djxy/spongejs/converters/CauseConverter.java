package io.github.djxy.spongejs.converters;

import com.eclipsesource.v8.V8;
import org.spongepowered.api.event.cause.Cause;

/**
 * Created by Samuel on 2016-09-07.
 */
@ConverterInfo(type = Cause.class, isV8Primitive = true)
public class CauseConverter extends Converter<Cause, String> {

    @Override
    public String convertToV8(V8 v8, Cause cause) {
        return cause.toString();
    }

    @Override
    public Cause convertFromV8(Object o) {
        return Cause.source(o).build();
    }

}
