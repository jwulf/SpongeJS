package io.github.djxy.spongejs.converters;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import org.spongepowered.api.service.permission.SubjectCollection;

/**
 * Created by samuelmarchildon-lavoie on 16-09-10.
 */
@ConverterInfo(type = SubjectCollection.class)
public class SubjectCollectionConverter extends ConverterV8Object<SubjectCollection>{

    @Override
    public void setV8Object(V8Object v8Object, V8 v8, SubjectCollection collection) {
        v8Object.add("hasPermission", new V8Function(v8, (receiver, parameters) -> {

            return null;
        }));
    }

}
