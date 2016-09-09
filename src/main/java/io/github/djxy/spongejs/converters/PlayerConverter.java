package io.github.djxy.spongejs.converters;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Object;
import org.spongepowered.api.entity.living.player.Player;

/**
 * Created by samuelmarchildon-lavoie on 16-09-09.
 */
public class PlayerConverter extends ConverterV8Object<Player> {

    private static final CommandSourceConverter commandSourceConverter = new CommandSourceConverter();
    private static final IdentifiableConverter identifiableConverter = new IdentifiableConverter();

    @Override
    protected void setV8Object(V8Object v8Object, V8 v8, Player player) {
        identifiableConverter.setV8Object(v8Object, v8, player);
        commandSourceConverter.setV8Object(v8Object, v8, player);
    }

}
