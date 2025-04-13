package me.xaxis.reportplus.listeners;

import me.xaxis.reportplus.Main;
import me.xaxis.reportplus.gui.ReportList;
import me.xaxis.reportplus.gui.ReportOptions;
import me.xaxis.reportplus.gui.ReportSelection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class OnInventoryClick implements Listener {

    public OnInventoryClick(Main plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
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

}
