package me.xaxis.reportplus.gui;

import me.xaxis.reportplus.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class ReportListOptions implements Listener, implements GUI{

    private final Inventory inventory;
    private final Main plugin;
    private final Player player;

    public ReportListOptions(Main plugin, Player player){
        this.plugin = plugin;
        this.player = player;
        inventory = Bukkit.createInventory(null, 6*9, "");
        registerListener(plugin);
        openGUI(player);
    }

    @Override
    public Inventory getGUI() {
        return inventory;
    }

    @Override
    public void createItems() {
        //barrier close, red conc delete, black conc resolved, arrow back,

    }

    @Override
    public void onClick(InventoryClickEvent event) {

    }
}
