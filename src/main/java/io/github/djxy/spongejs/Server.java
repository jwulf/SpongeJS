package io.github.djxy.spongejs;

import com.eclipsesource.v8.*;
import io.github.djxy.spongejs.module.Module;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created by samuelmarchildon-lavoie on 16-09-06.
 */
public final class Server {

    private Logger logger;
    private NodeJS nodeJS;
    private Path startingFile;
    private Thread thread;
    private final ArrayList<Module> modules;
    private boolean stop = false;
    private final ArrayList<V8Value> modulesValues = new ArrayList<>();
    private final ServerReferenceHandler handler = new ServerReferenceHandler();

    protected Server(Logger logger, Path startingFile) {
        this.startingFile = startingFile;
        this.modules = new ArrayList<>();
    }

    public V8 getRuntime(){
        return nodeJS.getRuntime();
    }

    public void addModule(Module module){
        modules.add(module);
    }

    public synchronized void init(){
        if(nodeJS != null)
            return;

        nodeJS = NodeJS.createNodeJS();
        nodeJS.getRuntime().addReferenceHandler(handler);

        for(Module module : modules)
            module.initialize(getRuntime());

        nodeJS.getRuntime().removeReferenceHandler(handler);
    }

    public synchronized void start(){
        if(thread != null)
            return;

        nodeJS.getRuntime().getLocker().release();

        thread = new Thread(() -> {
            nodeJS.getRuntime().getLocker().acquire();
            nodeJS.exec(startingFile.toFile());

            while(!stop && nodeJS.isRunning())
                nodeJS.handleMessage();

            clean();

            System.out.println(nodeJS.getRuntime().getObjectReferenceCount());

            nodeJS.release();
        });

        thread.start();
    }

    public void clean(){
        for(V8Value value : modulesValues)
            if(!value.isReleased())
                value.release();

        modulesValues.clear();
    }

    public void stop(){
        stop = true;
        pingServer();
    }

    private void pingServer(){
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL("http://localhost:3000/").openConnection();
            connection.setConnectTimeout(500);
            connection.setReadTimeout(500);
            connection.getResponseCode();
            connection.disconnect();
        } catch (Exception e) {}
    }

    private class ServerReferenceHandler implements ReferenceHandler {

        @Override
        public void v8HandleCreated(V8Value object) {
            modulesValues.add(object);
        }

        @Override
        public void v8HandleDisposed(V8Value object) {}

    }

}