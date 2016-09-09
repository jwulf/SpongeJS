package io.github.djxy.spongejs.converters;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;

/**
 * Created by samuelmarchildon-lavoie on 16-09-09.
 */
public class CommandSourceConverter extends ConverterV8Object<CommandSource> {

    private static final ContextualConverter contextualConverter = new ContextualConverter();
    private static final PlayerConverter playerConverter = new PlayerConverter();

    @Override
    public CommandSource convertFromV8(Object o) {
        String identifier = null;

        CommandSource player = playerConverter.convertFromV8(o);

        if(player != null)
            return player;

        if(o instanceof String)
            identifier = (String) o;
        else if(o instanceof V8Object){
            if(((V8Object) o).contains("getIdentifier"))
                identifier = ((V8Object) o).executeStringFunction("getIdentifier", new V8Array(((V8Object) o).getRuntime()));
        }

        if(identifier == null)
            throw new IllegalArgumentException("Should be a string or a commandSource.");

        if(Sponge.getServer().getConsole().getIdentifier().equals(identifier))
            return Sponge.getServer().getConsole();

        return super.convertFromV8(o);
    }

    @Override
    protected void setV8Object(V8Object v8Object, V8 v8, CommandSource commandSource) {
        contextualConverter.setV8Object(v8Object, v8, commandSource);
        v8Object.add("getName", new V8Function(v8, (receiver, parameters) -> commandSource.getName()));
    }

}
