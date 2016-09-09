package io.github.djxy.spongejs;

import io.github.djxy.spongejs.module.Module;

/**
 * Created by Samuel on 2016-09-07.
 */
public class SpongeJSService {

    private final Server server;

    public SpongeJSService(Server server) {
        this.server = server;
    }

    public void addModule(Module module){
        server.addModule(module);
    }

}
