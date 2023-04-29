package me.xaxis.reportplus.gui;

import me.xaxis.reportplus.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public interface GUI extends Listener {

    default GUI registerListener(Main plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        return this;
    }

    default GUI openGUI(Player player){
        createItems();
        player.openInventory(getGUI());
        return this;
    }

    Inventory getGUI();

    void createItems();

    void onClick(InventoryClickEvent event);
}
