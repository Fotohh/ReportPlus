package me.xaxis.reportplus.reports;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ReportYML {

    private final File file;

    private final YamlConfiguration yml;

    public ReportYML(File dataFolder){
        file = new File(dataFolder, "reports.yml");
        yml = new YamlConfiguration();
    }

    public void load() throws java.io.IOException, org.bukkit.configuration.InvalidConfigurationException{
        if(!file.exists() && !file.createNewFile()) {
            throw new IOException("Failed to create reports.yml file in plugin data folder!");
        }

        yml.load(file);
    }

    public YamlConfiguration getFile(){
        return yml;
    }

    public void set(String path, Object object) {
        yml.set(path,object);
    }

    public void save() throws IOException {
        yml.save(file);
    }

}
