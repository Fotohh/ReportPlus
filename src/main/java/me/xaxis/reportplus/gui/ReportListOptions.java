package me.xaxis.reportplus.gui;

import me.xaxis.reportplus.Main;
import me.xaxis.reportplus.file.LangConfig;
import me.xaxis.reportplus.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class ReportListOptions implements GUI{

    private final Inventory inventory;
    private final Main plugin;
    private final Player player;
    private final LangConfig lang;

    public ReportListOptions(Main plugin, Player player){
        this.plugin = plugin;
        this.player = player;
        this.lang = plugin.getLangConfig();
        inventory = Bukkit.createInventory(null, 6*9, "");
        registerListener(plugin).openGUI(player);
    }

    @Override
    public Inventory getGUI() {
        return inventory;
    }

    @Override
    public void createItems() {
        //barrier close, red conc delete, black conc resolved, arrow back,
        ItemUtils barrier = new ItemUtils(Material.BARRIER);
        barrier.setTitle;
        ItemUtils redConcrete = new ItemUtils(Material.RED_CONCRETE);
        ItemUtils blackConcrete = new ItemUtils(Material.BLACK_CONCRETE);
        ItemUtils arrow = new ItemUtils(Material.ARROW);
        
        

    }

    @Override
    @EventHandler
    public void onClick(InventoryClickEvent event) {

    }
}
