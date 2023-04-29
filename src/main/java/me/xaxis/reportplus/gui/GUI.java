package me.xaxis.reportplus.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public interface GUI {

    default void openGUI(Player player){
        createItems();
        player.openInventory(getGUI());
    }

    Inventory getGUI();

    void createItems();

    void onClick(InventoryClickEvent event);
}
