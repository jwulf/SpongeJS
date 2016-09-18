package io.github.djxy.spongejs.converter.converters;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Object;
import io.github.djxy.spongejs.converter.ConverterInfo;
import io.github.djxy.spongejs.converter.ConverterV8Object;
import org.spongepowered.api.command.CommandResult;

/**
 * Created by samuelmarchildon-lavoie on 16-09-09.
 */
@ConverterInfo(type = CommandResult.class)
public class CommandResultConverter extends ConverterV8Object<CommandResult> {

    @Override
    public void setV8Object(V8Object v8Object, V8 v8, CommandResult commandResult, Long uniqueIdentifier) {}

}
