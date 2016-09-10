package io.github.djxy.spongejs.converters;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.util.Tristate;

/**
 * Created by samuelmarchildon-lavoie on 16-09-10.
 */
@ConverterInfo(type = Subject.class)
public class SubjectConverter extends ConverterV8Object<Subject>{

    @Override
    public void setV8Object(V8Object v8Object, V8 v8, Subject subject) {
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
    }

}
