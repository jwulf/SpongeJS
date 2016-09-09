package io.github.djxy.spongejs;

import com.eclipsesource.v8.V8;
import io.github.djxy.spongejs.module.modules.EconomyModule;
import io.github.djxy.spongejs.util.LibraryLoader;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.economy.EconomyTransactionEvent;
import org.spongepowered.api.event.game.state.GameAboutToStartServerEvent;
import org.spongepowered.api.event.game.state.GameConstructionEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.text.Text;

import java.lang.reflect.Field;
import java.nio.file.FileSystems;

/**
 * Created by Samuel on 2016-09-07.
 */
@Plugin(id = "spongejs", name = "SpongeJS", version = "1.0", authors = {"Djxy"})
public class SpongeJS {

    private Server server;
    private SpongeJSService service;

    @Listener
    public void onGameConstructionEvent(GameConstructionEvent event){
        initV8();

        server = new Server(FileSystems.getDefault().getPath("nodeJS", "bin", "www"));
        service  = new SpongeJSService(server);

        Sponge.getServiceManager().setProvider(this, SpongeJSService.class, service);
    }

    @Listener
    public void onGameAboutToStartServerEvent(GameAboutToStartServerEvent event){
        server.addModule(new EconomyModule());
        server.init();
        server.getRuntime().add("serverCause", "SpongeJS");
        server.start();
    }

    @Listener
    public void onTransaction(EconomyTransactionEvent event){
        if(event.getTransactionResult().getAccount() instanceof UniqueAccount){
            Sponge.getServer().getPlayer(((UniqueAccount) event.getTransactionResult().getAccount()).getUniqueId()).get().sendMessage(Text.of("You have received "+event.getTransactionResult().getAmount().doubleValue()+event.getTransactionResult().getCurrency().getSymbol()));
        }
    }

    private static void initV8(){
        try {
            Field field = V8.class.getDeclaredField("nativeLibraryLoaded");
            field.setAccessible(true);
            field.set(null, true);
            field.setAccessible(false);
            LibraryLoader.loadLibraryFromJar();
        } catch (Exception e){e.printStackTrace();}
    }

}
