package io.github.djxy.spongejs.converter.converters;

import com.eclipsesource.v8.*;
import io.github.djxy.spongejs.converter.Converter;
import io.github.djxy.spongejs.converter.ConverterInfo;
import io.github.djxy.spongejs.converter.ConverterV8Object;
import org.spongepowered.api.world.Locatable;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

/**
 * Created by samuelmarchildon-lavoie on 16-09-21.
 */
@ConverterInfo(type = Locatable.class)
public class LocatableConverter extends ConverterV8Object<Locatable> {

    @Override
    public void setV8Object(V8Object v8Object, V8 v8, Locatable locatable, Long uniqueIdentifier) {
        v8Object.add("getWorld", registerV8Function(new V8Function(v8, (receiver, parameters) -> Converter.convertToV8(v8, World.class, locatable.getWorld())), uniqueIdentifier));
        v8Object.add("getLocation", registerV8Function(new V8Function(v8, (receiver, parameters) -> Converter.convertToV8(v8, Location.class, locatable.getLocation())), uniqueIdentifier));
    }

}
