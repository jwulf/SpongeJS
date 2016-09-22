package io.github.djxy.spongejs.converter.converters;

import com.eclipsesource.v8.*;
import io.github.djxy.spongejs.converter.ConverterInfo;
import io.github.djxy.spongejs.converter.ConverterV8Object;
import org.spongepowered.api.world.Location;

/**
 * Created by samuelmarchildon-lavoie on 16-09-21.
 */
@ConverterInfo(type = Location.class)
public class LocationConverter extends ConverterV8Object<Location> {

    @Override
    public void setV8Object(V8Object v8Object, V8 v8, Location location, Long uniqueIdentifier) {
        v8Object.add("getX", registerV8Function(new V8Function(v8, (receiver, parameters) -> location.getX()), uniqueIdentifier));
        v8Object.add("getY", registerV8Function(new V8Function(v8, (receiver, parameters) -> location.getY()), uniqueIdentifier));
        v8Object.add("getZ", registerV8Function(new V8Function(v8, (receiver, parameters) -> location.getZ()), uniqueIdentifier));
    }

}
