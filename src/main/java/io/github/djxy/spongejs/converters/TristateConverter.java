package io.github.djxy.spongejs.converters;

import com.eclipsesource.v8.V8;
import org.spongepowered.api.text.Text;
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
        if(o == null)
            return Tristate.UNDEFINED;

        return null;
    }

    @Override
    public Boolean convertToV8(V8 v8, Tristate tristate) {
        return tristate == Tristate.UNDEFINED?null:tristate.asBoolean();
    }

}
