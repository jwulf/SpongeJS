package io.github.djxy.spongejs.converter.converters;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import io.github.djxy.spongejs.converter.Converter;
import io.github.djxy.spongejs.converter.ConverterInfo;
import io.github.djxy.spongejs.converter.ConverterV8Object;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageReceiver;

/**
 * Created by Samuel on 2016-09-09.
 */
@ConverterInfo(type = MessageReceiver.class)
public class MessageReceiverConverter extends ConverterV8Object<MessageReceiver> {

    @Override
    public void setV8Object(V8Object v8Object, V8 v8, MessageReceiver messageReceiver, Long uniqueIdentifier) {
        v8Object.add("sendMessage", registerV8Function(new V8Function(v8, (receiver, parameters) -> {
            messageReceiver.sendMessage(Converter.convertFromV8(Text.class, parameters.get(0)));
            return null;
        }), uniqueIdentifier));
    }

}
