package me.xaxis.reportplus.file;

import me.xaxis.reportplus.enums.Lang;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public final class LangConfig {

    private static final String FILE_NAME = "Lang.yml";

    private final Path file;
    private final Logger logger;
    private final YamlConfiguration config;

    public LangConfig(Path dataFolder, Logger logger) {
        this.file = dataFolder.resolve(FILE_NAME);
        this.logger = logger;
        this.config = new YamlConfiguration();
    }

    public void init() throws IOException, InvalidConfigurationException {
        if (!Files.exists(file)) {
            Files.createFile(file);
            logger.info("Created " + FILE_NAME + ".");
        }

        config.load(file.toFile());

        boolean changed = false;

        for (Lang lang : Lang.values()) {
            if (config.contains(lang.getPath())) {
                continue;
            }

            config.set(lang.getPath(), lang.getDefaultValue());
            changed = true;
        }

        if (changed) {
            save();
        }
    }

    public void save() throws IOException {
        config.save(file.toFile());
    }

    public void reload() throws IOException, InvalidConfigurationException {
        config.load(file.toFile());
    }

    public String getString(Lang lang, Object... placeholders) {
        String message = config.getString(lang.getPath());

        if (message == null) {
            message = lang.getDefaultValue().toString();
        }

        if (placeholders != null && placeholders.length > 0) {
            message = String.format(message, placeholders);
        }

        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public List<String> getStringList(Lang lang) {
        if (config.isList(lang.getPath())) {
            return config.getStringList(lang.getPath());
        }

        Object defaultValue = lang.getDefaultValue();

        if (!(defaultValue instanceof List<?> defaultList)) {
            return new ArrayList<>();
        }

        List<String> strings = new ArrayList<>();

        for (Object value : defaultList) {
            if (value instanceof String string) {
                strings.add(string);
            }
        }

        return strings;
    }
}