package io.github.djxy.spongejs.converter.converters;

import com.eclipsesource.v8.V8;
import io.github.djxy.spongejs.converter.Converter;
import io.github.djxy.spongejs.converter.ConverterInfo;
import org.spongepowered.api.util.Tristate;

/**
 * Created by Samuel on 2016-09-07.
 */
@ConverterInfo(type = Tristate.class, isV8Primitive = true)
public class TristateConverter extends Converter<Tristate, Boolean> {

    @Override
    public Tristate convertFromV8(Object o) {
        if(o.equals(false))
            return Tristate.FALSE;
        if(o.equals(true))
            return Tristate.TRUE;

        return null;
    }

    @Override
    public Boolean convertToV8(V8 v8, Tristate tristate) {
        return tristate == Tristate.UNDEFINED?null:tristate.asBoolean();
    }

}
