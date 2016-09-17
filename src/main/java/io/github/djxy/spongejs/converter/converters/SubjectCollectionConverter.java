package io.github.djxy.spongejs.converter.converters;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import com.eclipsesource.v8.V8Value;
import io.github.djxy.spongejs.converter.Converter;
import io.github.djxy.spongejs.converter.ConverterInfo;
import io.github.djxy.spongejs.converter.ConverterV8Object;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.permission.PermissionService;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.service.permission.SubjectCollection;

import java.util.Map;
import java.util.UUID;

/**
 * Created by samuelmarchildon-lavoie on 16-09-10.
 */
@ConverterInfo(type = SubjectCollection.class)
public class SubjectCollectionConverter extends ConverterV8Object<SubjectCollection> {

    @Override
    public SubjectCollection convertFromV8(Object o) {
        if(!(o instanceof V8Object))
            throw new IllegalArgumentException("Should be a SubjectCollection.");

        V8Object v8Object = (V8Object) o;

        if(!v8Object.contains("getIdentifier"))
            return null;

        String collectionName = v8Object.executeStringFunction("getIdentifier", null);

        PermissionService service = Sponge.getServiceManager().provide(PermissionService.class).get();

        return service.getKnownSubjects().get(collectionName);
    }

    @Override
    public void setV8Object(V8Object v8Object, V8 v8, SubjectCollection collection, UUID uniqueIdentifier) {
        v8Object.add("get", new V8Function(v8, (receiver, parameters) -> collection.get(parameters.getString(0))));
        v8Object.add("getAllSubjects", new V8Function(v8, (receiver, parameters) -> Converter.convertIterableToV8(v8, Subject.class, collection.getAllSubjects())));
        v8Object.add("getAllSubjects", new V8Function(v8, (receiver, parameters) -> {
            Map<Subject,Boolean> map = null;

            if(parameters.length() == 1)
                map = collection.getAllWithPermission(parameters.getString(0));
            else if(parameters.length() == 1)
                map = collection.getAllWithPermission(Converter.convertSetFromV8(Context.class, parameters.get(0)), parameters.getString(1));

            if(map == null)
                return new V8Array(v8);

            V8Array v8Array = new V8Array(v8);

            for(Subject subject : map.keySet()){
                V8Object object = new V8Object(v8);

                object.add("subject", (V8Value) Converter.convertToV8(v8, Subject.class, subject));
                object.add("value", map.get(subject));

                v8Array.push(object);
            }

            return v8Array;
        }));
        v8Object.add("getDefaults", new V8Function(v8, (receiver, parameters) -> Converter.convertToV8(v8, Subject.class, collection.getDefaults())));
        v8Object.add("getIdentifier", new V8Function(v8, (receiver, parameters) -> collection.getIdentifier()));
        v8Object.add("hasRegistered", new V8Function(v8, (receiver, parameters) -> collection.hasRegistered(parameters.getString(0))));
    }

}
