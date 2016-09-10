package io.github.djxy.spongejs.module.modules;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import io.github.djxy.spongejs.converters.PlayerConverter;
import io.github.djxy.spongejs.module.Module;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;

/**
 * Created by Samuel on 2016-09-09.
 */
public class ServerModule implements Module {

    private static final PlayerConverter playerConverter = new PlayerConverter();

    @Override
    public void initilize(V8 serverRuntime) {
        V8Object serverModule = new V8Object(serverRuntime);
        Server server = Sponge.getServer();

        serverModule.add("getOnlinePlayers", new V8Function(serverRuntime, (receiver, parameters) -> {
            V8Array v8Array = new V8Array(serverRuntime);

            for(Player player : server.getOnlinePlayers())
                v8Array.push(playerConverter.convertToV8(serverRuntime, player));

            return v8Array;
        }));
        serverModule.add("getPlayer", new V8Function(serverRuntime, (receiver, parameters) -> playerConverter.convertToV8(serverRuntime, playerConverter.convertFromV8(parameters.get(0)))));

        serverRuntime.add("spongeServer", serverModule);
    }

}
