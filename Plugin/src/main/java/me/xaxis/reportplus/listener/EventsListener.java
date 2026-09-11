package me.xaxis.reportplus.listener;

import me.xaxis.reportplus.gui.ReportList;
import me.xaxis.reportplus.gui.ReportOptions;
import me.xaxis.reportplus.gui.ReportSelection;
import me.xaxis.reportplus.player.PlayerDataManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public final class EventsListener implements Listener {

    private final PlayerDataManager playerDataManager;

    public EventsListener(PlayerDataManager playerDataManager) {
        this.playerDataManager = playerDataManager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        if (event.getInventory().getHolder() instanceof ReportList list) {
            event.setCancelled(true);
            list.onClick(event);

        } else if (event.getInventory().getHolder() instanceof ReportOptions options) {
            event.setCancelled(true);
            options.onClick(event);

        } else if (event.getInventory().getHolder() instanceof ReportSelection selection) {
            event.setCancelled(true);
            selection.onClick(event);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        UUID playerUUID = player.getUniqueId();
        String playerName = player.getName();

        if (!playerDataManager.hasPlayerData(playerUUID)) {
            playerDataManager.addPlayer(playerUUID, playerName);
            return;
        }

        playerDataManager.indexPlayer(playerUUID, playerName);
    }
}