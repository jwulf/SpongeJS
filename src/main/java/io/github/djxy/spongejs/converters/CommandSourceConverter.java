package io.github.djxy.spongejs.converters;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;

/**
 * Created by samuelmarchildon-lavoie on 16-09-09.
 */
@ConverterInfo(type = CommandSource.class)
public class CommandSourceConverter extends ConverterV8Object<CommandSource> {

    @Override
    public CommandSource convertFromV8(Object o) {
        String identifier = null;

        try{
            CommandSource player = Converter.convertFromV8(Player.class, o);

            if(player != null)
                return player;
        } catch (IllegalArgumentException e){}

        if(o instanceof String)
            identifier = (String) o;
        else if(o instanceof V8Object){
            if(((V8Object) o).getRuntime().getObject("console").equals(o))
                return Sponge.getServer().getConsole();
            if(((V8Object) o).contains("getIdentifier"))
                identifier = ((V8Object) o).executeStringFunction("getIdentifier", new V8Array(((V8Object) o).getRuntime()));
        }

        if(identifier == null)
            throw new IllegalArgumentException("Should be a string, the console or a commandSource.");

        if(Sponge.getServer().getConsole().getIdentifier().equals(identifier))
            return Sponge.getServer().getConsole();

        return super.convertFromV8(o);
    }

    @Override
    public void setV8Object(V8Object v8Object, V8 v8, CommandSource commandSource) {
        v8Object.add("getName", new V8Function(v8, (receiver, parameters) -> commandSource.getName()));
    }

}
