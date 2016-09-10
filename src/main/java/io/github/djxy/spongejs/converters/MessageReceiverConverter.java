package io.github.djxy.spongejs.converters;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import org.spongepowered.api.text.channel.MessageReceiver;

/**
 * Created by Samuel on 2016-09-09.
 */
public class MessageReceiverConverter extends ConverterV8Object<MessageReceiver> {

    private static final TextConverter textConverter = new TextConverter();

    @Override
    protected void setV8Object(V8Object v8Object, V8 v8, MessageReceiver messageReceiver) {
        v8Object.add("sendMessage", new V8Function(v8, (receiver, parameters) -> {
            messageReceiver.sendMessage(textConverter.convertFromV8(parameters.get(0)));
            return null;
        }));
    }

}
