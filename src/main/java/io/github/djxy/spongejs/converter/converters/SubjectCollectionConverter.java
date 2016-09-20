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
    public void setV8Object(V8Object v8Object, V8 v8, SubjectCollection collection, Long uniqueIdentifier) {
        v8Object.add("get", registerV8Function(new V8Function(v8, (receiver, parameters) -> collection.get(parameters.getString(0))), uniqueIdentifier));
        v8Object.add("getAllSubjects", registerV8Function(new V8Function(v8, (receiver, parameters) -> Converter.convertIterableToV8(v8, Subject.class, collection.getAllSubjects())), uniqueIdentifier));
        v8Object.add("getAllSubjects", registerV8Function(new V8Function(v8, (receiver, parameters) -> {
            Map<Subject,Boolean> map = null;

            if(parameters.length() == 1)
                map = collection.getAllWithPermission(parameters.getString(0));
            else if(parameters.length() == 1)
                map = collection.getAllWithPermission(Converter.convertSetFromV8(Context.class, parameters.get(0)), parameters.getString(1));

            if(map == null)
                return new V8Array(v8);

            V8Array v8Array = new V8Array(v8);

            for(Subject subject : map.keySet()){
                V8Object object = registerV8Object(new V8Object(v8), v8Array.getHandle());

                object.add("subject", registerV8Object((V8Object) Converter.convertToV8(v8, Subject.class, subject), object.getHandle()));
                object.add("value", map.get(subject));

                v8Array.push(object);
            }

            return v8Array;
        }), uniqueIdentifier));
        v8Object.add("getDefaults", registerV8Function(new V8Function(v8, (receiver, parameters) -> Converter.convertToV8(v8, Subject.class, collection.getDefaults())), uniqueIdentifier));
        v8Object.add("getIdentifier", registerV8Function(new V8Function(v8, (receiver, parameters) -> collection.getIdentifier()), uniqueIdentifier));
        v8Object.add("hasRegistered", registerV8Function(new V8Function(v8, (receiver, parameters) -> collection.hasRegistered(parameters.getString(0))), uniqueIdentifier));
    }

}
