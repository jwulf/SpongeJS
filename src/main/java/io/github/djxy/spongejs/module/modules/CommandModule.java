package io.github.djxy.spongejs.module.modules;

import com.eclipsesource.v8.*;
import io.github.djxy.spongejs.converters.CommandResultConverter;
import io.github.djxy.spongejs.converters.CommandSourceConverter;
import io.github.djxy.spongejs.module.Module;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandManager;
import org.spongepowered.api.command.CommandSource;

/**
 * Created by samuelmarchildon-lavoie on 16-09-09.
 */
public class CommandModule implements Module {

    private static final CommandSourceConverter commandSourceConverter = new CommandSourceConverter();
    private static final CommandResultConverter commandResultConverter = new CommandResultConverter();

    @Override
    public void initilize(V8 serverRuntime) {
        V8Object commandManager = new V8Object(serverRuntime);
        CommandManager manager = Sponge.getCommandManager();

        commandManager.add("process", new V8Function(serverRuntime, (receiver, parameters) -> {
            if(parameters.length() != 2)
                return null;

            if(serverRuntime.getObject("console").equals(parameters.getObject(0)))
                return commandResultConverter.convertToV8(serverRuntime, manager.process(Sponge.getServer().getConsole(), parameters.getString(1)));


            CommandSource source = commandSourceConverter.convertFromV8(parameters.get(0));

            if(source != null)
                return commandResultConverter.convertToV8(serverRuntime, manager.process(source, parameters.getString(1)));

            return null;
        }));

        serverRuntime.add("commandManager", commandManager);
    }

}
