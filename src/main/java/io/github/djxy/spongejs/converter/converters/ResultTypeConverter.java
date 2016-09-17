package io.github.djxy.spongejs.converter.converters;

import com.eclipsesource.v8.V8;
import io.github.djxy.spongejs.converter.Converter;
import io.github.djxy.spongejs.converter.ConverterInfo;
import org.spongepowered.api.service.economy.transaction.ResultType;

/**
 * Created by Samuel on 2016-09-07.
 */
@ConverterInfo(type = ResultType.class, isV8Primitive = true)
public class ResultTypeConverter extends Converter<ResultType, String> {

    @Override
    public String convertToV8(V8 v8, ResultType resultType) {
        return resultType.name();
    }
}
