package me.xaxis.reportplus.listeners;

import me.xaxis.reportplus.Main;
import me.xaxis.reportplus.gui.ReportList;
import me.xaxis.reportplus.gui.ReportOptions;
import me.xaxis.reportplus.gui.ReportSelection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class OnInventoryClick implements Listener {

    private final Main plugin;

    public OnInventoryClick(Main plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void InventoryClick(InventoryClickEvent event){

        if(event.getInventory().getHolder() instanceof ReportList list){
            event.setCancelled(true);
            list.onClick(event);
        }

        if(event.getInventory().getHolder() instanceof ReportOptions options){
            event.setCancelled(true);
            options.onClick(event);
        }

        if(event.getInventory().getHolder() instanceof ReportSelection selection){
            event.setCancelled(true);
            selection.onClick(event);
        }

    }

}
