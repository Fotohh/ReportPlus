package me.xaxis.reportplus.reports;

import me.xaxis.reportplus.Main;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ReportYML {

    private final Main plugin;
    private File file;
    private YamlConfiguration yml;

    public ReportYML(Main plugin){
        this.plugin = plugin;
    }

    public void createFile() throws java.io.IOException, org.bukkit.configuration.InvalidConfigurationException{

        if(!plugin.getDataFolder().exists()){
            if(plugin.getDataFolder().mkdir()) {
                throw new IOException("Failed to create plugin data folder!");
            }
        }

        file = new File(plugin.getDataFolder(), "Reports.yml");
        yml = new YamlConfiguration();

        if(!file.exists() && !file.createNewFile()) {
            throw new IOException("Failed to create Reports.yml file in plugin data folder!");
        }

        yml.load(file);

    }

    public YamlConfiguration getFile(){
        return yml;
    }

    public void set(String path, Object object) throws IOException {
        yml.set(path,object);
        save();
    }

    public void save() throws IOException {
        yml.save(file);
    }

}
