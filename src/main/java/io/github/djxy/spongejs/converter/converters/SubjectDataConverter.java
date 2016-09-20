package io.github.djxy.spongejs.converter.converters;

import com.eclipsesource.v8.*;
import io.github.djxy.spongejs.converter.Converter;
import io.github.djxy.spongejs.converter.ConverterInfo;
import io.github.djxy.spongejs.converter.ConverterV8Object;
import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.service.permission.SubjectData;
import org.spongepowered.api.util.Tristate;

import java.util.*;

/**
 * Created by samuelmarchildon-lavoie on 16-09-10.
 */
@ConverterInfo(type = SubjectData.class)
public class SubjectDataConverter extends ConverterV8Object<SubjectData> {

    @Override
    public void setV8Object(V8Object v8Object, V8 v8, SubjectData subject, Long uniqueIdentifier) {
        v8Object.add("addParent", registerV8Function(new V8Function(v8, (receiver, parameters) -> subject.addParent(Converter.convertSetFromV8(Context.class, parameters.get(0)), Converter.convertFromV8(Subject.class, parameters.get(1)))), uniqueIdentifier));
        v8Object.add("clearOptions", registerV8Function(new V8Function(v8, (receiver, parameters) -> {
            if(parameters.length() == 0)
                return subject.clearOptions();
            if(parameters.length() == 1)
                return subject.clearOptions(Converter.convertSetFromV8(Context.class, parameters.get(0)));

            return null;
        }), uniqueIdentifier));
        v8Object.add("clearParents", registerV8Function(new V8Function(v8, (receiver, parameters) -> {
            if(parameters.length() == 0)
                return subject.clearParents();
            if(parameters.length() == 1)
                return subject.clearParents(Converter.convertSetFromV8(Context.class, parameters.get(0)));

            return null;
        }), uniqueIdentifier));
        v8Object.add("clearPermissions", registerV8Function(new V8Function(v8, (receiver, parameters) -> {
            if(parameters.length() == 0)
                return subject.clearPermissions();
            if(parameters.length() == 1)
                return subject.clearPermissions(Converter.convertSetFromV8(Context.class, parameters.get(0)));

            return null;
        }), uniqueIdentifier));
        v8Object.add("getAllOptions", registerV8Function(new V8Function(v8, (receiver, parameters) -> {
            Map<Set<Context>, Map<String,String>> options = subject.getAllOptions();

            V8Array v8Array = new V8Array(v8);

            for(Set<Context> contexts : options.keySet()){
                V8Object object = registerV8Object(new V8Object(v8), v8Array.getHandle());

                object.add("contexts", registerV8Object((V8Object) Converter.convertIterableToV8(v8, Context.class, contexts), object.getHandle()));

                V8Object objectOptions = registerV8Object(new V8Object(v8), object.getHandle());

                Map<String, String> mapOption = options.get(contexts);

                for(String option : mapOption.keySet())
                    objectOptions.add(option, mapOption.get(option));

                object.add("options", objectOptions);

                v8Array.push(object);
            }

            return v8Array;
        }), uniqueIdentifier));
        v8Object.add("getOptions", registerV8Function(new V8Function(v8, (receiver, parameters) -> {
            if(parameters.length() != 1)
                return null;

            Map<String,String> options = subject.getOptions(Converter.convertSetFromV8(Context.class, parameters.get(0)));

            V8Object objectOptions = new V8Object(v8);

            for(String option : options.keySet())
                objectOptions.add(option, options.get(option));

            return objectOptions;
        }), uniqueIdentifier));
        v8Object.add("getAllParents", registerV8Function(new V8Function(v8, (receiver, parameters) -> {
            Map<Set<Context>, List<Subject>> subjects = subject.getAllParents();

            V8Array v8Array = new V8Array(v8);

            for(Set<Context> contexts : subjects.keySet()){
                V8Object object = registerV8Object(new V8Object(v8), v8Array.getHandle());

                object.add("contexts", registerV8Value((V8Value) Converter.convertIterableToV8(v8, Context.class, contexts), object.getHandle()));
                object.add("subjects", registerV8Value((V8Value) Converter.convertIterableToV8(v8, Subject.class, subjects.get(contexts)), object.getHandle()));

                v8Array.push(object);
            }

            return v8Array;
        }), uniqueIdentifier));
        v8Object.add("getParents", registerV8Function(new V8Function(v8, (receiver, parameters) -> {
            if(parameters.length() != 1)
                return null;

            return Converter.convertIterableToV8(v8, Subject.class, subject.getParents(Converter.convertSetFromV8(Context.class, parameters.get(0))));
        }), uniqueIdentifier));
        v8Object.add("getAllPermissions", registerV8Function(new V8Function(v8, (receiver, parameters) -> {
            Map<Set<Context>, Map<String,Boolean>> permissions = subject.getAllPermissions();

            V8Array v8Array = new V8Array(v8);

            for(Set<Context> contexts : permissions.keySet()){
                V8Object object = registerV8Object(new V8Object(v8), v8Array.getHandle());

                object.add("contexts", registerV8Value((V8Value) Converter.convertIterableToV8(v8, Context.class, contexts), object.getHandle()));

                V8Object objectPermissions = registerV8Object(new V8Object(v8), object.getHandle());

                Map<String,Boolean> mapPermission = permissions.get(contexts);

                for(String permission : mapPermission.keySet())
                    objectPermissions.add(permission, mapPermission.get(permission));

                object.add("permissions", objectPermissions);

                v8Array.push(object);
            }

            return v8Array;
        }), uniqueIdentifier));
        v8Object.add("getPermissions", registerV8Function(new V8Function(v8, (receiver, parameters) -> {
            if(parameters.length() != 1)
                return null;

            Map<String,Boolean> permissions = subject.getPermissions(Converter.convertSetFromV8(Context.class, parameters.get(0)));

            V8Object objectPermissions = new V8Object(v8);

            for(String permission : permissions.keySet())
                objectPermissions.add(permission, permissions.get(permission));

            return objectPermissions;
        }), uniqueIdentifier));
        v8Object.add("setOption", registerV8Function(new V8Function(v8, (receiver, parameters) -> {
            if(parameters.length() != 3)
                return null;

            return subject.setOption(Converter.convertSetFromV8(Context.class, parameters.get(0)), parameters.getString(1), parameters.getString(2));
        }), uniqueIdentifier));
        v8Object.add("setPermission", registerV8Function(new V8Function(v8, (receiver, parameters) -> {
            if(parameters.length() != 3)
                return null;

            return subject.setPermission(Converter.convertSetFromV8(Context.class, parameters.get(0)), parameters.getString(1), Converter.convertFromV8(Tristate.class, parameters.get(2)));
        }), uniqueIdentifier));
        v8Object.add("removeParent", registerV8Function(new V8Function(v8, (receiver, parameters) -> {
            if(parameters.length() != 2)
                return null;

            return subject.removeParent(Converter.convertSetFromV8(Context.class, parameters.get(0)), Converter.convertFromV8(Subject.class, parameters.get(2)));
        }), uniqueIdentifier));
    }

}
