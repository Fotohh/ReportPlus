package me.xaxis.reportplus.player;

import java.util.UUID;

public record PlayerData(UUID playerUUID, String playerName, boolean reportAlertsToggled, long reportCooldownUntil) {
}
