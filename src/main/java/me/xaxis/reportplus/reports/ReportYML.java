package me.xaxis.reportplus.reports;

import me.xaxis.reportplus.ReportPlus;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ReportYML {

    private final ReportPlus plugin;
    private File file;
    private YamlConfiguration yml;

    public ReportYML(ReportPlus plugin){
        this.plugin = plugin;
    }

    public void createFile(){

        if(!plugin.getDataFolder().exists()){
            plugin.getDataFolder().mkdir();
        }

        file = new File(plugin.getDataFolder(), "Reports.yml");
        yml = new YamlConfiguration();

        if(!file.exists()) file.mkdir();

        try {
            yml.load(file);
        }catch (Exception ignored){
            Bukkit.getServer().getConsoleSender().sendMessage("Failed to create Reports.yml");
        }

    }

    public YamlConfiguration getFile(){
        return yml;
    }

    public void save() {
        try {
            yml.save(file);
        }catch (Exception ignored){
            Bukkit.getServer().getConsoleSender().sendMessage("Failed to save Reports.yml");
        }
    }

}
