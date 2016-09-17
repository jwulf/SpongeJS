package io.github.djxy.spongejs.module.modules;

import com.eclipsesource.v8.V8;
import io.github.djxy.spongejs.converter.Converter;
import io.github.djxy.spongejs.module.Module;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;

/**
 * Created by Samuel on 2016-09-09.
 */
public class ConsoleModule implements Module {

    /**
     * Only to edit the javascript console
     * @param serverRuntime
     */
    @Override
    public void initialize(V8 serverRuntime) {
        Converter.editV8Object(serverRuntime.getObject("console"), CommandSource.class, Sponge.getServer().getConsole());
    }

}
