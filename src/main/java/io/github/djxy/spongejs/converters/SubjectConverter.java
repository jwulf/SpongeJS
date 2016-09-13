package io.github.djxy.spongejs.converters;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.service.permission.SubjectCollection;
import org.spongepowered.api.service.permission.SubjectData;
import org.spongepowered.api.util.Tristate;

/**
 * Created by samuelmarchildon-lavoie on 16-09-10.
 */
@ConverterInfo(type = Subject.class)
public class SubjectConverter extends ConverterV8Object<Subject>{

    @Override
    public Subject convertFromV8(Object o) {
        return super.convertFromV8(o);
    }

    @Override
    public void setV8Object(V8Object v8Object, V8 v8, Subject subject) {
        v8Object.add("getContainingCollection", new V8Function(v8, (receiver, parameters) -> Converter.convertToV8(v8, SubjectCollection.class, subject.getContainingCollection())));
        v8Object.add("hasPermission", new V8Function(v8, (receiver, parameters) -> {
            if(parameters.length() == 1)
                return subject.hasPermission(parameters.getString(0));
            if(parameters.length() == 2)
                return subject.hasPermission(Converter.convertSetFromV8(Context.class, parameters.get(0)), parameters.getString(1));

            return null;
        }));
        v8Object.add("getOption", new V8Function(v8, (receiver, parameters) -> {
            if(parameters.length() == 1)
                return subject.getOption(parameters.getString(0));
            if(parameters.length() == 2)
                return subject.getOption(Converter.convertSetFromV8(Context.class, parameters.get(0)), parameters.getString(1));

            return null;
        }));
        v8Object.add("getPermissionValue", new V8Function(v8, (receiver, parameters) -> {
            if(parameters.length() == 2)
                return Converter.convertToV8(v8, Tristate.class, subject.getPermissionValue(Converter.convertSetFromV8(Context.class, parameters.get(0)), parameters.getString(1)));

            return null;
        }));
        v8Object.add("getParents", new V8Function(v8, (receiver, parameters) -> {
            if (parameters.length() == 0)
                return Converter.convertIterableToV8(v8, Subject.class, subject.getParents());
            if (parameters.length() == 1)
                return Converter.convertIterableToV8(v8, Subject.class, subject.getParents(Converter.convertSetFromV8(Context.class, parameters.get(0))));

            return null;
        }));
        v8Object.add("getSubjectData", new V8Function(v8, (receiver, parameters) -> Converter.convertToV8(v8, SubjectData.class, subject.getSubjectData())));
        v8Object.add("getTransientSubjectData", new V8Function(v8, (receiver, parameters) -> Converter.convertToV8(v8, SubjectData.class, subject.getTransientSubjectData())));
        v8Object.add("isChildOf", new V8Function(v8, (receiver, parameters) -> {
            if(parameters.length() == 1)
                return subject.isChildOf(Converter.convertFromV8(Subject.class, parameters.get(0)));
            if(parameters.length() == 2)
                return subject.isChildOf(Converter.convertSetFromV8(Context.class, parameters.get(0)), Converter.convertFromV8(Subject.class, parameters.get(0)));

            return null;
        }));
    }

}
