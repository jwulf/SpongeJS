package io.github.djxy.spongejs.module.modules;

import com.eclipsesource.v8.*;
import io.github.djxy.spongejs.converters.Converter;
import io.github.djxy.spongejs.module.Module;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandManager;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;

/**
 * Created by samuelmarchildon-lavoie on 16-09-09.
 */
public class CommandModule implements Module {

    @Override
    public void initilize(V8 serverRuntime) {
        V8Object commandManager = new V8Object(serverRuntime);
        CommandManager manager = Sponge.getCommandManager();

        commandManager.add("process", new V8Function(serverRuntime, (receiver, parameters) -> {
            if(parameters.length() != 2)
                return null;

            CommandSource source = Converter.convertFromV8(CommandSource.class, parameters.get(0));

            if(source != null)
                return Converter.convertToV8(serverRuntime, CommandResult.class, manager.process(source, parameters.getString(1)));

            return null;
        }));

        serverRuntime.add("commandManager", commandManager);
    }

}
