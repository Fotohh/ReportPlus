package me.xaxis.reportplus.gui;

import me.xaxis.reportplus.ReportPlus;
import me.xaxis.reportplus.enums.ReportType;
import me.xaxis.reportplus.utils.ItemUtils;
import me.xaxis.reportplus.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class ReportSelection implements GUI{

    private final int size = 9;

    private final Inventory i;

    private final ReportPlus plugin;

    public ReportSelection(ReportPlus plugin, String title){
        i = Bukkit.createInventory(null, size, Utils.chat(title));
        this.plugin = plugin;
    }

    @Override
    public Inventory getGUI() {
        return i;
    }

    @Override
    public void createItems() {
        for(ReportType type : ReportType.values()){
            ItemUtils item = new ItemUtils(type.getMaterial(plugin));
            item.setTitle("&a" + type);
            getGUI().addItem(item.i());
        }
    }

    @Override
    public void onClick(InventoryClickEvent event) {

    }
}
