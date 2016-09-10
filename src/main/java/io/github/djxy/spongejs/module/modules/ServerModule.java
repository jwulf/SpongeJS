package io.github.djxy.spongejs.module.modules;

import com.eclipsesource.v8.*;
import io.github.djxy.spongejs.converters.Converter;
import io.github.djxy.spongejs.module.Module;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;

/**
 * Created by Samuel on 2016-09-09.
 */
public class ServerModule implements Module {

    @Override
    public void initilize(V8 serverRuntime) {
        V8Object serverModule = new V8Object(serverRuntime);
        Server server = Sponge.getServer();

        serverModule.add("getOnlinePlayers", new V8Function(serverRuntime, (receiver, parameters) -> {
            V8Array v8Array = new V8Array(serverRuntime);

            for(Player player : server.getOnlinePlayers())
                v8Array.push((V8Value) Converter.convertToV8(serverRuntime, Player.class, player));

            return v8Array;
        }));
        serverModule.add("getPlayer", new V8Function(serverRuntime, (receiver, parameters) -> Converter.convertToV8(serverRuntime, Player.class, Converter.convertFromV8(Player.class, parameters.get(0)))));

        serverRuntime.add("spongeServer", serverModule);
    }

}
