package io.github.djxy.spongejs.converter.converters;

import com.eclipsesource.v8.*;
import io.github.djxy.spongejs.converter.ConverterInfo;
import io.github.djxy.spongejs.converter.ConverterV8Object;
import org.spongepowered.api.world.World;

/**
 * Created by samuelmarchildon-lavoie on 16-09-21.
 */
@ConverterInfo(type = World.class)
public class WorldConverter extends ConverterV8Object<World> {

    @Override
    public void setV8Object(V8Object v8Object, V8 v8, World world, Long uniqueIdentifier) {
        v8Object.add("getName", registerV8Function(new V8Function(v8, (receiver, parameters) -> world.getName()), uniqueIdentifier));
    }

}
