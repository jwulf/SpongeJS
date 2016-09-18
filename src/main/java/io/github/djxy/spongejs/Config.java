package io.github.djxy.spongejs;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Created by Samuel on 2016-09-18.
 */
public class Config {

    private final Path folder;
    private final Path path;
    private ConfigurationNode root;

    public Config(Path path) {
        this.path = path;
        this.folder = path.getParent();

        try {
            if(!path.toFile().exists())
                path.toFile().createNewFile();

            root = HoconConfigurationLoader.builder().setPath(path).build().load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File getStartingFile(){
        return folder.resolve(root.getNode("startingFile").getString("").replace("/", File.separator)).toFile();
    }

    public String getShutdownPortScript(){
        return root.getNode("scripts", "shutdownPort").getString("");
    }

    public int getPort(){
        return root.getNode("port").getInt(3000);
    }

}
