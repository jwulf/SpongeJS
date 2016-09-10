package io.github.djxy.spongejs.converters;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import org.spongepowered.api.CatalogType;

/**
 * Created by Samuel on 2016-09-07.
 */
@ConverterInfo(type = CatalogType.class)
public class CatalogTypeConverter extends ConverterV8Object<CatalogType> {

    @Override
    public void setV8Object(V8Object v8Object, V8 v8, CatalogType v) {
        v8Object.add("getId", new V8Function(v8, (receiver, parameters) -> v.getId()));
        v8Object.add("getName", new V8Function(v8, (receiver, parameters) -> v.getName()));
    }

}
