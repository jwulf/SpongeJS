package io.github.djxy.spongejs.converters;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Object;
import org.spongepowered.api.service.permission.SubjectData;

/**
 * Created by samuelmarchildon-lavoie on 16-09-10.
 */
@ConverterInfo(type = SubjectData.class)
public class SubjectDataConverter extends ConverterV8Object<SubjectData>{

    @Override
    public void setV8Object(V8Object v8Object, V8 v8, SubjectData subject) {
    }

}
