package me.xaxis.reportplus.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public interface GUI {

    void openGUI(Player player);

    Inventory getGUI();

    void createItems();
}
