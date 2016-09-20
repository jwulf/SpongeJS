package io.github.djxy.spongejs.module.modules;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import io.github.djxy.spongejs.converter.Converter;
import io.github.djxy.spongejs.module.Module;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandManager;

/**
 * Created by Samuel on 2016-09-19.
 */
public class SpongeModule implements Module {

    @Override
    public void initialize(V8 serverRuntime) {
        V8Object serverModule = new V8Object(serverRuntime);

        serverModule.add("getServer", new V8Function(serverRuntime, (receiver, parameters) -> Converter.convertToV8(serverRuntime, Server.class, Sponge.getServer())));
        serverModule.add("getCommandManager", new V8Function(serverRuntime, (receiver, parameters) -> Converter.convertToV8(serverRuntime, CommandManager.class, Sponge.getCommandManager())));

        serverRuntime.add("Sponge", serverModule);
    }

}
