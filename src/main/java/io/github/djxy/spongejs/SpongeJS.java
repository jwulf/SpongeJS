package io.github.djxy.spongejs;

import com.eclipsesource.v8.V8;
import com.google.inject.Inject;
import io.github.djxy.spongejs.converter.Converter;
import io.github.djxy.spongejs.module.modules.ConsoleModule;
import io.github.djxy.spongejs.module.modules.EconomyModule;
import io.github.djxy.spongejs.module.modules.PermissionModule;
import io.github.djxy.spongejs.util.LibraryLoader;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameAboutToStartServerEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStoppedEvent;
import org.spongepowered.api.plugin.Plugin;

import java.lang.reflect.Field;
import java.nio.file.Path;

/**
 * Created by Samuel on 2016-09-07.
 */
@Plugin(id = "spongejs", name = "SpongeJS", version = "1.0", authors = {"Djxy"})
public class SpongeJS {

    private Server server;
    private SpongeJSService service;

    @Inject
    private Logger logger;
    @Inject
    @DefaultConfig(sharedRoot = false)
    private Path configPath;
    private Config config;

    @Listener
    public void onGamePreInitializationEvent(GamePreInitializationEvent event){
        config = new Config(configPath);

        if(!config.getStartingFile().exists()) {
            logger.error("NodeJS server not found. Make sure to have " + config.getStartingFile().getAbsolutePath());
            return;
        }

        logger.info("NodeJS server found.");

        init();

        server = new Server(logger, config);
        service  = new SpongeJSService(server);

        Sponge.getServiceManager().setProvider(this, SpongeJSService.class, service);
    }

    @Listener
    public void onGameAboutToStartServerEvent(GameAboutToStartServerEvent event){
        if(!config.getStartingFile().exists())
            return;

        server.addModule(new PermissionModule());
        server.addModule(new EconomyModule());
        server.addModule(new ConsoleModule());
        server.start();

        Sponge.getCommandManager().register(this,
                CommandSpec
                        .builder()
                        .child(CommandSpec.builder().executor((commandSource, commandContext) -> {
                            server.start();
                            return CommandResult.success();
                        }).build(), "start")
                        .child(CommandSpec.builder().executor((commandSource, commandContext) -> {
                            server.stop();
                            return CommandResult.success();
                        }).build(), "stop")
                        .build()
                , "spongejs");
    }

    @Listener
    public void onGameStoppedEvent(GameStoppedEvent event){
        if(server != null)
            server.stop();
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
