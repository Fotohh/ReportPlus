package me.xaxis.reportplus.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public abstract interface GUI {

    void openGUI(Player player);

    Inventory getGUI();

    void createItems();

    public abstract void close();
}
