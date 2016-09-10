package io.github.djxy.spongejs.converters;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageReceiver;

/**
 * Created by Samuel on 2016-09-09.
 */
@ConverterInfo(type = MessageReceiver.class)
public class MessageReceiverConverter extends ConverterV8Object<MessageReceiver> {

    @Override
    public void setV8Object(V8Object v8Object, V8 v8, MessageReceiver messageReceiver) {
        v8Object.add("sendMessage", new V8Function(v8, (receiver, parameters) -> {
            messageReceiver.sendMessage(Converter.convertFromV8(Text.class, parameters.get(0)));
            return null;
        }));
    }

}
