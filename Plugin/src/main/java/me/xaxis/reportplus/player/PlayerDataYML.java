package me.xaxis.reportplus.player;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class PlayerDataYML {

    private static final String DATA_SECTION = "player-data";

    private final Path file;
    private final YamlConfiguration config;
    private final Logger logger;

    public PlayerDataYML(Path dataFolder, Logger logger) {
        file = dataFolder.resolve("player_data.yml");
        this.logger = logger;
        this.config = new YamlConfiguration();

    }

    public void load() throws IOException, InvalidConfigurationException {
        if (!Files.exists(file)) {
            Files.createFile(file);
        }
        config.load(file.toFile());

        if (!config.isConfigurationSection(DATA_SECTION)) {
            config.createSection(DATA_SECTION);
            save();
        }
    }

    private static final String PLAYER_NAME = "player-name";
    private static final String REPORT_ALERTS_TOGGLED = "reports-alerts-toggled";
    private static final String REPORT_COOLDOWN_UNTIL = "report-cooldown-until";

    public Map<UUID, PlayerData> loadPlayerData() {
        Map<UUID, PlayerData> playerDataMap = new HashMap<>();

        ConfigurationSection section = config.getConfigurationSection(DATA_SECTION);

        for (String keys : section.getKeys(false)) {
            if(!section.isConfigurationSection(keys)) {
                logger.warning("Value '" + keys + "' in " + section.getName() + " in player_data.yml is misconfigured. Please fix this. Skipping entry.");
                continue;
            }
            ConfigurationSection dataSection = section.getConfigurationSection(keys);
            PlayerData data;
            try {
                data = parsePlayerData(dataSection);
            } catch (MalformedPlayerDataException e) {
                logDataError(e.getMessage(), keys);
                continue;
            }

            playerDataMap.put(data.playerUUID(), data);
        }

        if(repaired) {
            try {
                save();
                repaired = false;
            } catch (IOException e) {
                logger.log(Level.WARNING, "Failed to save player_data.yml!", e);
            }
        }

        return playerDataMap;
    }

    private void logDataError(String value, String key) {
        logger.warning(value + " is either misconfigured or missing in " +
                key + " in player_data.yml. Please fix this. Skipping entry");
    }

    private boolean repaired = false;

    private PlayerData parsePlayerData(ConfigurationSection section) throws MalformedPlayerDataException {

        String playerUUIDString = section.getName();
        UUID playerUUID;
        try{
            playerUUID = UUID.fromString(playerUUIDString);
        } catch (IllegalArgumentException e) {
            throw new MalformedPlayerDataException(section.getName());
        }

        if(!section.isString(PLAYER_NAME)) {
            throw new MalformedPlayerDataException(PLAYER_NAME);
        }
        String playerName = section.getString(PLAYER_NAME);

        if(playerName.isBlank()) {
            throw new MalformedPlayerDataException(PLAYER_NAME);
        }

        if(!section.isBoolean(REPORT_ALERTS_TOGGLED)) {
            section.set(REPORT_ALERTS_TOGGLED, false);
            repaired = true;
        }
        boolean reportAlertsToggled = section.getBoolean(REPORT_ALERTS_TOGGLED);

        if(!section.isLong(REPORT_COOLDOWN_UNTIL)) {
            section.set(REPORT_COOLDOWN_UNTIL, 0);
            repaired = true;
        }
        long reportCooldownUntil = section.getLong(REPORT_COOLDOWN_UNTIL);

        return new PlayerData(playerUUID, playerName, reportAlertsToggled, reportCooldownUntil);
    }

    public void savePlayerData(PlayerData playerData) {
        ConfigurationSection section = config.getConfigurationSection(DATA_SECTION);
        ConfigurationSection playerSection;
        String playerUUID = playerData.playerUUID().toString();
        if(!section.isSet(playerData.playerUUID().toString())) {
            playerSection = section.createSection(playerUUID);
        } else {
            if(section.isConfigurationSection(playerUUID)) {
                playerSection = section.getConfigurationSection(playerUUID);
            } else {
                logger.warning("Failed to save " + playerData.playerName() + "'s data. Expected a configuration section, got something else. Please fix this.");
                return;
            }
        }
        playerSection.set(PLAYER_NAME, playerData.playerName());
        playerSection.set(REPORT_ALERTS_TOGGLED, playerData.reportAlertsToggled());
        playerSection.set(REPORT_COOLDOWN_UNTIL, playerData.reportCooldownUntil());
    }

    public void deletePlayerData(UUID uuid) {
        config.set(DATA_SECTION + "." + uuid.toString(), null);
    }

    public void save() throws IOException {
        config.save(file.toFile());
    }

    private static final class MalformedPlayerDataException extends Exception {
        private MalformedPlayerDataException(String message) {
            super(message);
        }
    }

}
