package io.github.djxy.spongejs.converters;

import com.eclipsesource.v8.V8;
import org.spongepowered.api.text.Text;

/**
 * Created by Samuel on 2016-09-07.
 */
@ConverterInfo(type = Text.class, isV8Primitive = true)
public class TextConverter extends Converter<Text, String> {

    @Override
    public Text convertFromV8(Object o) {
        return Text.of(o);
    }

    @Override
    public String convertToV8(V8 v8, Text text) {
        return text.toPlain();
    }

}
