package io.github.djxy.spongejs;

import com.eclipsesource.v8.NodeJS;
import com.eclipsesource.v8.V8;
import io.github.djxy.spongejs.module.Module;

import java.nio.file.Path;
import java.util.ArrayList;

/**
 * Created by samuelmarchildon-lavoie on 16-09-06.
 */
public final class Server {

    private NodeJS nodeJS;
    private Path startingFile;
    private Thread thread;
    private final ArrayList<Module> modules;

    protected Server(Path startingFile) {
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

        for(Module module : modules)
            module.initialize(getRuntime());
    }

    public synchronized void start(){
        if(thread != null)
            return;

        nodeJS.getRuntime().getLocker().release();

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                nodeJS.getRuntime().getLocker().acquire();
                nodeJS.exec(startingFile.toFile());

                while(nodeJS.isRunning())
                    nodeJS.handleMessage();
            }
        });

        thread.start();
    }

    public void stop(){
        thread.interrupt();
    }

}