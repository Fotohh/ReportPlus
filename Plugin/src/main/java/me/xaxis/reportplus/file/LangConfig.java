package me.xaxis.reportplus.file;

import me.xaxis.reportplus.Main;
import me.xaxis.reportplus.enums.Lang;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class LangConfig {
    private final Main plugin;
    private final String fileName;
    private final File configFile;
    private FileConfiguration fileConfiguration;

    public LangConfig(Main plugin) {
        this.plugin = plugin;
        this.fileName = "Lang.yml";
        this.configFile = new File(plugin.getDataFolder(), fileName);

        createFile();
    }

    public void createFile() {
        try {
            if (!configFile.exists()) {
                if(!configFile.createNewFile()){
                    plugin.getLogger().severe("Could not create " + fileName + " file.");
                    return;
                }
                plugin.getLogger().info("Created " + fileName + " file.");
            }

            fileConfiguration = new YamlConfiguration();
            fileConfiguration.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            plugin.getLogger().severe("Could not load " + fileName + " file.");
            e.printStackTrace();
            return;
        }

        boolean hasChanged = false;
        for (Lang messagePath : Lang.values()) {
            if (!fileConfiguration.contains(messagePath.getPath())) {
                fileConfiguration.set(messagePath.getPath(), messagePath.getDefaultValue());
                hasChanged = true;
            }
        }

        if (hasChanged) {
            saveConfig();
        }
    }

    public FileConfiguration getConfig() {
        return fileConfiguration;
    }

    public void saveConfig() {
        try {
            getConfig().save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reloadConfig() {
        fileConfiguration = YamlConfiguration.loadConfiguration(configFile);
    }

    public String getString(Lang path, Object... placeholders) {
        FileConfiguration config = getConfig();
        String message = config.getString(path.getPath(), path.getDefaultValue().toString());

        if (placeholders != null && placeholders.length > 0) {
            message = String.format(message, placeholders);
        }

        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
