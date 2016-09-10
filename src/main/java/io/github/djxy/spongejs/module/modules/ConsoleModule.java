package io.github.djxy.spongejs.module.modules;

import com.eclipsesource.v8.V8;
import io.github.djxy.spongejs.converters.CommandSourceConverter;
import io.github.djxy.spongejs.module.Module;
import org.spongepowered.api.Sponge;

/**
 * Created by Samuel on 2016-09-09.
 */
public class ConsoleModule implements Module {

    /**
     * Only to edit the javascript console
     * @param serverRuntime
     */
    @Override
    public void initilize(V8 serverRuntime) {
        new CommandSourceConverter().setV8Object(serverRuntime.getObject("console"), serverRuntime, Sponge.getServer().getConsole());
    }

}
