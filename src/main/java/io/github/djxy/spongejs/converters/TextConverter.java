package io.github.djxy.spongejs.converters;

import com.eclipsesource.v8.V8;
import org.spongepowered.api.text.Text;

/**
 * Created by Samuel on 2016-09-07.
 */
public class TextConverter extends Converter<Text, String> {

    @Override
    public String convertToV8(V8 v8, Text text) {
        return text.toPlain();
    }

}
