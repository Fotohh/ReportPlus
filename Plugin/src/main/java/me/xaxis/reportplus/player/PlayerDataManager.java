package me.xaxis.reportplus.player;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlayerDataManager {

    private final PlayerDataYML playerDataYML;
    private final Logger logger;

    public PlayerDataManager(PlayerDataYML playerDataYML, Logger logger) {
        this.playerDataYML = playerDataYML;
        this.logger = logger;
        playerDataMap = new HashMap<>(playerDataYML.loadPlayerData());
        buildPlayerIndex();
    }

    private final Map<UUID, PlayerData> playerDataMap;

    private final Map<String, UUID> playerNameIndex = new HashMap<>();

    public UUID getPlayerUUID(String playerName) {
        return playerNameIndex.get(playerName.toLowerCase(Locale.ROOT));
    }

    private void buildPlayerIndex() {
        for (var entry : playerDataMap.entrySet()) {
            if (playerNameIndex.containsKey(entry.getValue().playerName().toLowerCase(Locale.ROOT))) {
                logger.warning("Found duplicate entry '" + entry.getValue().playerName().toLowerCase(Locale.ROOT)
                        + "' in player name index. Skipping it.");
                continue;
            }
            playerNameIndex.put(entry.getValue().playerName().toLowerCase(Locale.ROOT), entry.getKey());
        }
    }

    public void removePlayerData(UUID uuid) {
        playerDataYML.deletePlayerData(uuid);
        try {
            playerDataYML.save();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to save player_data.yml!", e);
            return;
        }
        PlayerData data = playerDataMap.remove(uuid);
        if (data == null) return;
        playerNameIndex.remove(data.playerName().toLowerCase(Locale.ROOT), uuid);
    }

    public PlayerData getPlayerData(UUID uuid) {
        return playerDataMap.get(uuid);
    }

    public boolean hasPlayerData(UUID uuid) {
        return playerDataMap.containsKey(uuid);
    }

    public void indexPlayer(UUID playerUUID, String playerName) {
        String original = playerName;
        playerName = playerName.toLowerCase(Locale.ROOT);

        PlayerData data = getPlayerData(playerUUID);

        if (data == null) {
            addPlayer(playerUUID, original);
            return;
        }

        if (playerNameIndex.containsKey(playerName) && !playerNameIndex.get(playerName).equals(data.playerUUID())) {
            logger.warning("Found duplicate entry '" + playerName + "' skipping indexing the player.");
            return;
        }

        if (original.equals(data.playerName())) return;

        PlayerData newData = new PlayerData(playerUUID, original, data.reportAlertsToggled(), data.reportCooldownUntil());
        playerDataYML.savePlayerData(newData);
        try {
            playerDataYML.save();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Unable to save player_data.yml!", e);
            return;
        }
        playerNameIndex.remove(data.playerName().toLowerCase(Locale.ROOT), data.playerUUID());
        playerNameIndex.put(playerName, playerUUID);
        playerDataMap.put(data.playerUUID(), newData);
    }

    public boolean addPlayer(UUID playerUUID, String playerName) {
        String original = playerName;
        playerName = playerName.toLowerCase(Locale.ROOT);

        if (playerDataMap.containsKey(playerUUID)) {
            return false;
        }

        if (playerNameIndex.containsKey(playerName)) {
            return false;
        }

        PlayerData data = new PlayerData(playerUUID, original, true, 0);

        playerDataYML.savePlayerData(data);

        try {
            playerDataYML.save();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Unable to save player_data.yml!", e);
            return false;
        }

        playerDataMap.put(playerUUID, data);
        playerNameIndex.put(playerName, playerUUID);

        return true;
    }
}
