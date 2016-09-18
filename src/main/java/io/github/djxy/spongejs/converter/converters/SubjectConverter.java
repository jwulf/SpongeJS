package io.github.djxy.spongejs.converter.converters;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import io.github.djxy.spongejs.converter.Converter;
import io.github.djxy.spongejs.converter.ConverterInfo;
import io.github.djxy.spongejs.converter.ConverterV8Object;
import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.service.permission.SubjectCollection;
import org.spongepowered.api.service.permission.SubjectData;
import org.spongepowered.api.util.Tristate;

/**
 * Created by samuelmarchildon-lavoie on 16-09-10.
 */
@ConverterInfo(type = Subject.class)
public class SubjectConverter extends ConverterV8Object<Subject> {

    @Override
    public Subject convertFromV8(Object o) {
        if(!(o instanceof V8Object))
            throw new IllegalArgumentException("Should be a Subject.");

        V8Object v8Object = (V8Object) o;

        if(!v8Object.contains("getContainingCollection") || !v8Object.contains("getIdentifier"))
            return null;

        SubjectCollection collection = Converter.convertFromV8(SubjectCollection.class, v8Object.executeObjectFunction("getContainingCollection", null));

        if(collection == null)
            return null;

        return collection.get(v8Object.executeStringFunction("getIdentifier", null));
    }

    @Override
    public void setV8Object(V8Object v8Object, V8 v8, Subject subject, Long uniqueIdentifier) {
        v8Object.add("getContainingCollection", registerV8Function(new V8Function(v8, (receiver, parameters) -> Converter.convertToV8(v8, SubjectCollection.class, subject.getContainingCollection())), uniqueIdentifier));
        v8Object.add("hasPermission", registerV8Function(new V8Function(v8, (receiver, parameters) -> {
            if(parameters.length() == 1)
                return subject.hasPermission(parameters.getString(0));
            if(parameters.length() == 2)
                return subject.hasPermission(Converter.convertSetFromV8(Context.class, parameters.get(0)), parameters.getString(1));

            return null;
        }), uniqueIdentifier));
        v8Object.add("getOption", registerV8Function(new V8Function(v8, (receiver, parameters) -> {
            if(parameters.length() == 1)
                return subject.getOption(parameters.getString(0));
            if(parameters.length() == 2)
                return subject.getOption(Converter.convertSetFromV8(Context.class, parameters.get(0)), parameters.getString(1));

            return null;
        }), uniqueIdentifier));
        v8Object.add("getPermissionValue", registerV8Function(new V8Function(v8, (receiver, parameters) -> {
            if(parameters.length() == 2)
                return Converter.convertToV8(v8, Tristate.class, subject.getPermissionValue(Converter.convertSetFromV8(Context.class, parameters.get(0)), parameters.getString(1)));

            return null;
        }), uniqueIdentifier));
        v8Object.add("getParents", registerV8Function(new V8Function(v8, (receiver, parameters) -> {
            if (parameters.length() == 0)
                return Converter.convertIterableToV8(v8, Subject.class, subject.getParents());
            if (parameters.length() == 1)
                return Converter.convertIterableToV8(v8, Subject.class, subject.getParents(Converter.convertSetFromV8(Context.class, parameters.get(0))));

            return null;
        }), uniqueIdentifier));
        v8Object.add("getSubjectData", registerV8Function(new V8Function(v8, (receiver, parameters) -> Converter.convertToV8(v8, SubjectData.class, subject.getSubjectData())), uniqueIdentifier));
        v8Object.add("getTransientSubjectData", registerV8Function(new V8Function(v8, (receiver, parameters) -> Converter.convertToV8(v8, SubjectData.class, subject.getTransientSubjectData())), uniqueIdentifier));
        v8Object.add("isChildOf", registerV8Function(new V8Function(v8, (receiver, parameters) -> {
            if(parameters.length() == 1)
                return subject.isChildOf(Converter.convertFromV8(Subject.class, parameters.get(0)));
            if(parameters.length() == 2)
                return subject.isChildOf(Converter.convertSetFromV8(Context.class, parameters.get(0)), Converter.convertFromV8(Subject.class, parameters.get(0)));

            return null;
        }), uniqueIdentifier));
    }

}
