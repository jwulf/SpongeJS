package io.github.djxy.spongejs;

import com.eclipsesource.v8.NodeJS;
import com.eclipsesource.v8.ReferenceHandler;
import com.eclipsesource.v8.V8Value;
import io.github.djxy.spongejs.converter.Converter;
import io.github.djxy.spongejs.module.Module;
import org.slf4j.Logger;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by samuelmarchildon-lavoie on 16-09-06.
 */
public final class Server {

    private Logger logger;
    private NodeJS nodeJS;
    private Thread thread;
    private Config config;
    private boolean stop = false;
    private final ArrayList<Module> modules = new ArrayList<>();
    private final ArrayList<V8Value> modulesValues = new ArrayList<>();
    private final ServerModuleReferenceHandler moduleHandler = new ServerModuleReferenceHandler();
    private final ServerReleaseReferenceHandler releaseHandler = new ServerReleaseReferenceHandler();

    protected Server(Logger logger, Config config) {
        this.logger = logger;
        this.config = config;
    }

    public void addModule(Module module){
        modules.add(module);
    }

    public synchronized boolean start(){
        if(thread != null) {
            logger.warn("NodeJS server already running.");
            return false;
        }

        logger.info("NodeJS server initialization started...");

        stop = false;
        nodeJS = NodeJS.createNodeJS();
        nodeJS.getRuntime().removeReferenceHandler(releaseHandler);
        nodeJS.getRuntime().addReferenceHandler(moduleHandler);

        logger.info("NodeJS server loading modules...");

        for(Module module : modules)
            module.initialize(nodeJS.getRuntime());

        logger.info("NodeJS server modules loaded.");

        nodeJS.getRuntime().removeReferenceHandler(moduleHandler);
        nodeJS.getRuntime().addReferenceHandler(releaseHandler);

        nodeJS.getRuntime().getLocker().release();

        thread = new Thread(() -> {
            nodeJS.getRuntime().getLocker().acquire();
            logger.info("NodeJS server started.");
            nodeJS.exec(config.getStartingFile());

            while(!stop && nodeJS.isRunning())
                nodeJS.handleMessage();

            clean();

            nodeJS.getRuntime().shutdownExecutors(true);
            nodeJS.getRuntime().terminateExecution();

            nodeJS.getRuntime().executeVoidScript(config.getShutdownPortScript());

            logger.info("Object not released: "+nodeJS.getRuntime().getObjectReferenceCount());

            nodeJS.release();
            logger.info("NodeJS server stopped.");
            thread = null;
        });

        thread.start();

        return true;
    }

    public void stop(){
        stop = true;
        new Thread(this::pingServer).start();
    }

    private void clean(){
        for(V8Value value : modulesValues)
            if(!value.isReleased())
                value.release();

        modulesValues.clear();
    }

    private void pingServer(){
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL("http://localhost:"+config.getPort()+"/").openConnection();
            connection.setConnectTimeout(100);
            connection.setReadTimeout(100);
            connection.getResponseCode();
            connection.disconnect();
        } catch (Exception e) {}
    }

    private class ServerModuleReferenceHandler implements ReferenceHandler {

        @Override
        public void v8HandleCreated(V8Value object) {
            modulesValues.add(object);
        }

        @Override
        public void v8HandleDisposed(V8Value object) {}

    }

    private class ServerReleaseReferenceHandler implements ReferenceHandler {

        @Override
        public void v8HandleCreated(V8Value object) {}

        @Override
        public void v8HandleDisposed(V8Value object) {
            Converter.releaseRegistredValues(object.getHandle());
        }

    }

}