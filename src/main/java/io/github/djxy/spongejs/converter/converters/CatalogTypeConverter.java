package io.github.djxy.spongejs.converter.converters;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import io.github.djxy.spongejs.converter.ConverterInfo;
import io.github.djxy.spongejs.converter.ConverterV8Object;
import org.spongepowered.api.CatalogType;

import java.util.UUID;

/**
 * Created by Samuel on 2016-09-07.
 */
@ConverterInfo(type = CatalogType.class)
public class CatalogTypeConverter extends ConverterV8Object<CatalogType> {

    @Override
    public void setV8Object(V8Object v8Object, V8 v8, CatalogType v, UUID uniqueIdentifier) {
        v8Object.add("getId", registerV8Function(new V8Function(v8, (receiver, parameters) -> v.getId()), uniqueIdentifier));
        v8Object.add("getName", registerV8Function(new V8Function(v8, (receiver, parameters) -> v.getName()), uniqueIdentifier));
    }

}
