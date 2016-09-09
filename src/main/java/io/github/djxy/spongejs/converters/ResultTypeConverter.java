package io.github.djxy.spongejs.converters;

import com.eclipsesource.v8.V8;
import org.spongepowered.api.service.economy.transaction.ResultType;

/**
 * Created by Samuel on 2016-09-07.
 */
public class ResultTypeConverter extends Converter<ResultType, String> {

    @Override
    public String convertToV8(V8 v8, ResultType resultType) {
        return resultType.name();
    }
}
