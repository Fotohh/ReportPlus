package me.xaxis.reportplus.gui;

import me.xaxis.reportplus.ReportPlus;
import me.xaxis.reportplus.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public class ReportList implements GUI{

    private final Inventory i;

    public ReportList(ReportPlus reportPlus, String title){
        i = Bukkit.createInventory(null, 6*9, Utils.chat(title));
    }

    @Override
    public void openGUI(@NotNull Player player) {
        player.openInventory(i);
    }

    @Override
    public Inventory getGUI() {
        return i;
    }

    @Override
    public void createItems() {

    }
}
