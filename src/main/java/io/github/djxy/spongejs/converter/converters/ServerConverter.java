package io.github.djxy.spongejs.converter.converters;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import com.eclipsesource.v8.V8Value;
import io.github.djxy.spongejs.converter.Converter;
import io.github.djxy.spongejs.converter.ConverterInfo;
import io.github.djxy.spongejs.converter.ConverterV8Object;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;

/**
 * Created by Samuel on 2016-09-19.
 */
@ConverterInfo(type = Server.class)
public class ServerConverter extends ConverterV8Object<Server> {

    @Override
    public void setV8Object(V8Object v8Object, V8 v8, Server o, Long uniqueIdentifier) {
        Server server = Sponge.getServer();

        v8Object.add("getOnlinePlayers", registerV8Function(new V8Function(v8, (receiver, parameters) -> {
            V8Array v8Array = new V8Array(v8);

            for(Player player : server.getOnlinePlayers())
                v8Array.push((V8Value) Converter.convertToV8(v8, Player.class, player));

            return v8Array;
        }), uniqueIdentifier));
        v8Object.add("getPlayer", registerV8Function(new V8Function(v8, (receiver, parameters) -> Converter.convertToV8(v8, Player.class, Converter.convertFromV8(Player.class, parameters.get(0)))), uniqueIdentifier));
        v8Object.add("getConsole", registerV8Function(new V8Function(v8, (receiver, parameters) -> v8.get("console")), uniqueIdentifier));
    }

}
