package me.xaxis.reportplus.managers;

import lombok.SneakyThrows;
import me.xaxis.reportplus.ReportPlus;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ReportYML {

    private final ReportPlus plugin;
    private File file;
    private YamlConfiguration yml;

    public ReportYML(ReportPlus plugin){
        this.plugin = plugin;
    }

    @SneakyThrows
    public void createFile(){

        if(!plugin.getDataFolder().exists()){
            plugin.getDataFolder().mkdir();
        }

        file = new File(plugin.getDataFolder(), "Reports.yml");
        yml = new YamlConfiguration();

        if(!file.exists()) file.mkdir();

        yml.load(file);

    }

    public YamlConfiguration getFile(){
        return yml;
    }

    @SneakyThrows
    public void save(){
        yml.save(file);
    }

}
