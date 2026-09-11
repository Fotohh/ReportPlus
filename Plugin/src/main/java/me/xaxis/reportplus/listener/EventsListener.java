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

public class EventsListener implements Listener {

    private final PlayerDataManager data;

    public EventsListener(PlayerDataManager playerDataManager) {
        data = playerDataManager;
    }

    @EventHandler
    public void InventoryClick(InventoryClickEvent event){

        if(event.getInventory().getHolder() instanceof ReportList list){
            list.onClick(event);
            event.setCancelled(true);
        }

        if(event.getInventory().getHolder() instanceof ReportOptions options){
            options.onClick(event);
            event.setCancelled(true);
        }

        if(event.getInventory().getHolder() instanceof ReportSelection selection){
            selection.onClick(event);
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();
        UUID playerUUID = player.getUniqueId();
        if(!data.hasPlayerData(playerUUID)) {
            data.addPlayer(playerUUID, playerName);
        } else {
            data.indexPlayer(playerUUID, playerName);
        }
    }

}
