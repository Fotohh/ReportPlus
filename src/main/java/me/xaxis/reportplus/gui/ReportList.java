package me.xaxis.reportplus.gui;

import me.xaxis.reportplus.Main;
import me.xaxis.reportplus.reports.Report;
import me.xaxis.reportplus.reports.ReportManager;
import me.xaxis.reportplus.utils.ItemUtils;
import me.xaxis.reportplus.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.Date;
import java.util.UUID;

public class ReportList implements GUI{

    private final Inventory i;
    private final Main plugin;

    public ReportList(Main plugin, String title){
        i = Bukkit.createInventory(null, 6*9, Utils.chat(title));
        this.plugin = plugin;
        registerListener(plugin);
    }

    @Override
    public Inventory getGUI() {
        return i;
    }

    @Override
    public void createItems() {

        for(Report report : ReportManager.getReports()){
            ItemUtils item = new ItemUtils(report.getReportType().getMaterial(plugin));
            Date date = new Date(report.getTimestamp());

            item.lore("&7Report Type: &6" + report.getReportType().toString(),
                    "&7Player: &6" + report.getPlayerName(),
                    "&7Reporter: &6"+ report.getTargetName(),
                    "&7Date: &6" + date)
                    .setTitle("&a" + report.getPlayerUUID())
                    .build();
            getGUI().addItem(item.i());
        }

    }

    @Override
    @EventHandler
    public void onClick(InventoryClickEvent event) {

        if(event.getClickedInventory() == null) return;

        if(!event.getClickedInventory().equals(getGUI())) return;

        if(event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR || event.getCurrentItem().getItemMeta() == null) return;

        OfflinePlayer player = Bukkit.getPlayer(UUID.fromString(event.getCurrentItem().getItemMeta().getDisplayName()));

        if(player == null) return;

        Report report = ReportManager.getReport(player.getUniqueId());

        new ReportListOptions(plugin, (Player) event.getWhoClicked(), report);

        event.setCancelled(true);
    }

}
