package io.github.djxy.spongejs;

import com.eclipsesource.v8.V8;
import com.google.inject.Inject;
import io.github.djxy.spongejs.converters.Converter;
import io.github.djxy.spongejs.module.modules.CommandModule;
import io.github.djxy.spongejs.module.modules.ConsoleModule;
import io.github.djxy.spongejs.module.modules.EconomyModule;
import io.github.djxy.spongejs.module.modules.ServerModule;
import io.github.djxy.spongejs.util.LibraryLoader;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameAboutToStartServerEvent;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.plugin.Plugin;

import java.lang.reflect.Field;
import java.nio.file.FileSystems;
import java.nio.file.Path;

/**
 * Created by Samuel on 2016-09-07.
 */
@Plugin(id = "spongejs", name = "SpongeJS", version = "1.0", authors = {"Djxy"})
public class SpongeJS {

    private Server server;
    private SpongeJSService service;
    private Path serverPath;

    @Inject
    private Logger logger;

    @Listener
    public void onGamePostInitializationEvent(GamePostInitializationEvent event){
        init();

        serverPath = FileSystems.getDefault().getPath("nodeJS", "bin", "www");

        if(serverPath.toFile().exists())
            logger.info("NodeJS server found. Can start it.");
        else
            logger.error("NodeJS server not found. Make sure to have " + serverPath.toAbsolutePath()+".");

        if(!serverPath.toFile().exists())
            return;

        server = new Server(serverPath);
        service  = new SpongeJSService(server);

        Sponge.getServiceManager().setProvider(this, SpongeJSService.class, service);
    }

    @Listener
    public void onGameAboutToStartServerEvent(GameAboutToStartServerEvent event){
        if(!serverPath.toFile().exists())
            return;
        
        server.addModule(new EconomyModule());
        server.addModule(new CommandModule());
        server.addModule(new ConsoleModule());
        server.addModule(new ServerModule());
        server.init();
        server.getRuntime().add("serverCause", "SpongeJS");
        server.start();
    }

    private static void init(){
        try {
            Field field = V8.class.getDeclaredField("nativeLibraryLoaded");
            field.setAccessible(true);
            field.set(null, true);
            field.setAccessible(false);
            LibraryLoader.loadLibraryFromJar();
        } catch (Exception e){e.printStackTrace();}

        Converter.init();
    }

}
