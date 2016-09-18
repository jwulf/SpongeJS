package io.github.djxy.spongejs.converter.converters;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import io.github.djxy.spongejs.converter.ConverterInfo;
import io.github.djxy.spongejs.converter.ConverterV8Object;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;

import java.util.Optional;
import java.util.UUID;

/**
 * Created by samuelmarchildon-lavoie on 16-09-09.
 */
@ConverterInfo(type = Player.class)
public class PlayerConverter extends ConverterV8Object<Player> {

    @Override
    public Player convertFromV8(Object o) {
        String identifier = null;

        if(o instanceof String)
            identifier = (String) o;
        else if(o instanceof V8Object){
            if(((V8Object) o).contains("getIdentifier"))
                identifier = ((V8Object) o).executeStringFunction("getIdentifier", null);
            else if(((V8Object) o).contains("getUniqueID"))
                identifier = ((V8Object) o).executeStringFunction("getUniqueID", null);
        }

        if(identifier == null)
            throw new IllegalArgumentException("Should be a string, a UUID or a Player.");

        try{
            UUID uuid = UUID.fromString(identifier);

            Optional<Player> player = Sponge.getServer().getPlayer(uuid);

            if(player.isPresent())
                return player.get();
        }catch (Exception e){}

        return super.convertFromV8(o);
    }

    @Override
    public void setV8Object(V8Object v8Object, V8 v8, Player player, Long uniqueIdentifier) {
        v8Object.add("hasPlayedBefore", registerV8Function(new V8Function(v8, (receiver, parameters) -> player.hasPlayedBefore()), uniqueIdentifier));
        v8Object.add("kick", registerV8Function(new V8Function(v8, (receiver, parameters) -> {
            player.kick();
            return null;
        }), uniqueIdentifier));
    }

}
