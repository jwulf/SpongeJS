package io.github.djxy.spongejs.converters;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;

import java.math.BigDecimal;

/**
 * Created by Samuel on 2016-09-07.
 */
public class BigDecimalConverter extends Converter<BigDecimal, Double> {

    @Override
    public Double convertToV8(V8 v8, BigDecimal bigDecimal) {
        return bigDecimal.doubleValue();
    }

    @Override
    public BigDecimal convertFromV8(Object o){
        if(o.getClass().equals(Integer.class))
            return new BigDecimal((Integer) o);
        else if(o.getClass().equals(Double.class))
            return new BigDecimal((Double) o);
        else if(o.getClass().equals(String.class))
            return new BigDecimal((String) o);
        else
            throw new IllegalArgumentException("Should be a string, an integer or a double.");
    }

}
