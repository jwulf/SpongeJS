package io.github.djxy.spongejs.converters;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import org.spongepowered.api.command.CommandSource;

/**
 * Created by samuelmarchildon-lavoie on 16-09-09.
 */
public class CommandSourceConverter extends ConverterV8Object<CommandSource> {

    @Override
    protected void setV8Object(V8Object v8Object, V8 v8, CommandSource commandSource) {
        v8Object.add("getName", new V8Function(v8, (receiver, parameters) -> commandSource.getName()));
    }

}
