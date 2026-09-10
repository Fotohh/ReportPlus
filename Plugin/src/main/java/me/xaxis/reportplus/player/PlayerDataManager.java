package me.xaxis.reportplus.player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataManager {

    private final PlayerDataYML playerDataYML;

    public PlayerDataManager(PlayerDataYML playerDataYML) {
        this.playerDataYML = playerDataYML;
        playerDataMap = playerDataYML.loadPlayerData();
    }

    private final Map<UUID, PlayerData> playerDataMap;

    private final Map<String, UUID> playerNameIndex = new HashMap<>();

    public UUID getPlayerUUID(String playerName) {
        return playerNameIndex.get(playerName);
    }

    public PlayerData getPlayerData(UUID uuid) {
        return playerDataMap.get(uuid);
    }



}
