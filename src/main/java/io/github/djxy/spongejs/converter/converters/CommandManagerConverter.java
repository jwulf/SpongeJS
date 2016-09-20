package io.github.djxy.spongejs.converter.converters;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import io.github.djxy.spongejs.converter.Converter;
import io.github.djxy.spongejs.converter.ConverterInfo;
import io.github.djxy.spongejs.converter.ConverterV8Object;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandManager;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;

/**
 * Created by Samuel on 2016-09-19.
 */
@ConverterInfo(type = CommandManager.class)
public class CommandManagerConverter extends ConverterV8Object<CommandManager> {

    @Override
    public void setV8Object(V8Object v8Object, V8 v8, CommandManager commandManager, Long uniqueIdentifier) {
        v8Object.add("process", registerV8Function(new V8Function(v8, (receiver, parameters) -> {
            if(parameters.length() != 2)
                return null;

            CommandSource source = Converter.convertFromV8(CommandSource.class, parameters.get(0));

            if(source != null)
                return Converter.convertToV8(v8, CommandResult.class, Sponge.getCommandManager().process(source, parameters.getString(1)));

            return null;
        }), uniqueIdentifier));
    }

}
