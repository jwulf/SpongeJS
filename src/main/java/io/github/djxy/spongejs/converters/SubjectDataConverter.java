package io.github.djxy.spongejs.converters;

import com.eclipsesource.v8.*;
import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.service.permission.SubjectData;
import org.spongepowered.api.util.Tristate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by samuelmarchildon-lavoie on 16-09-10.
 */
@ConverterInfo(type = SubjectData.class)
public class SubjectDataConverter extends ConverterV8Object<SubjectData>{

    @Override
    public void setV8Object(V8Object v8Object, V8 v8, SubjectData subject) {
        v8Object.add("addParent", new V8Function(v8, (receiver, parameters) -> subject.addParent(Converter.convertSetFromV8(Context.class, parameters.get(0)), Converter.convertFromV8(Subject.class, parameters.get(1)))));
        v8Object.add("clearOptions", new V8Function(v8, (receiver, parameters) -> {
            if(parameters.length() == 0)
                return subject.clearOptions();
            if(parameters.length() == 1)
                return subject.clearOptions(Converter.convertSetFromV8(Context.class, parameters.get(0)));

            return null;
        }));
        v8Object.add("clearParents", new V8Function(v8, (receiver, parameters) -> {
            if(parameters.length() == 0)
                return subject.clearParents();
            if(parameters.length() == 1)
                return subject.clearParents(Converter.convertSetFromV8(Context.class, parameters.get(0)));

            return null;
        }));
        v8Object.add("clearPermissions", new V8Function(v8, (receiver, parameters) -> {
            if(parameters.length() == 0)
                return subject.clearPermissions();
            if(parameters.length() == 1)
                return subject.clearPermissions(Converter.convertSetFromV8(Context.class, parameters.get(0)));

            return null;
        }));
        v8Object.add("getAllOptions", new V8Function(v8, (receiver, parameters) -> {
            Map<Set<Context>, Map<String,String>> options = subject.getAllOptions();

            V8Array v8Array = new V8Array(v8);

            for(Set<Context> contexts : options.keySet()){
                V8Object object = new V8Object(v8);

                object.add("contexts", (V8Value) Converter.convertIterableToV8(v8, Context.class, contexts));

                V8Object objectOptions = new V8Object(v8);

                Map<String,String> mapOption = options.get(contexts);

                for(String option : mapOption.keySet())
                    objectOptions.add(option, mapOption.get(option));

                object.add("options", objectOptions);

                v8Array.push(object);
            }

            return v8Array;
        }));
        v8Object.add("getOptions", new V8Function(v8, (receiver, parameters) -> {
            if(parameters.length() != 1)
                return null;

            Map<String,String> options = subject.getOptions(Converter.convertSetFromV8(Context.class, parameters.get(0)));

            V8Object objectOptions = new V8Object(v8);

            for(String option : options.keySet())
                objectOptions.add(option, options.get(option));

            return objectOptions;
        }));
        v8Object.add("getAllParents", new V8Function(v8, (receiver, parameters) -> {
            Map<Set<Context>, List<Subject>> subjects = subject.getAllParents();

            V8Array v8Array = new V8Array(v8);

            for(Set<Context> contexts : subjects.keySet()){
                V8Object object = new V8Object(v8);

                object.add("contexts", (V8Value) Converter.convertIterableToV8(v8, Context.class, contexts));
                object.add("subjects", (V8Value) Converter.convertIterableToV8(v8, Subject.class, subjects.get(contexts)));

                v8Array.push(object);
            }

            return v8Array;
        }));
        v8Object.add("getParents", new V8Function(v8, (receiver, parameters) -> {
            if(parameters.length() != 1)
                return null;

            return Converter.convertIterableToV8(v8, Subject.class, subject.getParents(Converter.convertSetFromV8(Context.class, parameters.get(0))));
        }));
        v8Object.add("getAllPermissions", new V8Function(v8, (receiver, parameters) -> {
            Map<Set<Context>, Map<String,Boolean>> permissions = subject.getAllPermissions();

            V8Array v8Array = new V8Array(v8);

            for(Set<Context> contexts : permissions.keySet()){
                V8Object object = new V8Object(v8);

                object.add("contexts", (V8Value) Converter.convertIterableToV8(v8, Context.class, contexts));

                V8Object objectPermissions = new V8Object(v8);

                Map<String,Boolean> mapPermission = permissions.get(contexts);

                for(String permission : mapPermission.keySet())
                    objectPermissions.add(permission, mapPermission.get(permission));

                object.add("permissions", objectPermissions);

                v8Array.push(object);
            }

            return v8Array;
        }));
        v8Object.add("getPermissions", new V8Function(v8, (receiver, parameters) -> {
            if(parameters.length() != 1)
                return null;

            Map<String,Boolean> permissions = subject.getPermissions(Converter.convertSetFromV8(Context.class, parameters.get(0)));

            V8Object objectPermissions = new V8Object(v8);

            for(String permission : permissions.keySet())
                objectPermissions.add(permission, permissions.get(permission));

            return objectPermissions;
        }));
        v8Object.add("setOption", new V8Function(v8, (receiver, parameters) -> {
            if(parameters.length() != 3)
                return null;

            return subject.setOption(Converter.convertSetFromV8(Context.class, parameters.get(0)), parameters.getString(1), parameters.getString(2));
        }));
        v8Object.add("setPermission", new V8Function(v8, (receiver, parameters) -> {
            if(parameters.length() != 3)
                return null;

            return subject.setPermission(Converter.convertSetFromV8(Context.class, parameters.get(0)), parameters.getString(1), Converter.convertFromV8(Tristate.class, parameters.get(2)));
        }));
        v8Object.add("removeParent", new V8Function(v8, (receiver, parameters) -> {
            if(parameters.length() != 2)
                return null;

            return subject.removeParent(Converter.convertSetFromV8(Context.class, parameters.get(0)), Converter.convertFromV8(Subject.class, parameters.get(2)));
        }));
    }

}
