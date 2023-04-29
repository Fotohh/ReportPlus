package me.xaxis.reportplus.reports;

import me.xaxis.reportplus.ReportPlus;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ReportYML {

    private final ReportPlus plugin;
    private File file;
    private YamlConfiguration yml;

    public ReportYML(ReportPlus plugin){
        this.plugin = plugin;
    }

    public void createFile() throws java.io.IOException, org.bukkit.configuration.InvalidConfigurationException{

        if(!plugin.getDataFolder().exists()){
            plugin.getDataFolder().mkdir();
        }

        file = new File(plugin.getDataFolder(), "Reports.yml");
        yml = new YamlConfiguration();

        if(!file.exists()) file.createNewFile();

        yml.load(file);

    }

    public YamlConfiguration getFile(){
        return yml;
    }

    public void set(String path, Object object) throws IOException {
        yml.set(path,object);
        save();
    }

    public Object get(String path){
        return yml.get(path);
    }

    public void save() throws IOException {
        yml.save(file);
    }

}
