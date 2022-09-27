package me.xaxis.reportplus.gui;

import me.xaxis.reportplus.ReportPlus;
import me.xaxis.reportplus.reports.Report;
import me.xaxis.reportplus.reports.ReportManager;
import me.xaxis.reportplus.utils.ItemUtils;
import me.xaxis.reportplus.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

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

        for(Report report : ReportManager.getReports()){
            ItemUtils item = new ItemUtils(Material.ANVIL);

            Date date = new Date(report.getTimestamp());
            item.lore("&7Report Type: &6" + report.getReportType().toString(),
                    "&7Player: &6" + Bukkit.getPlayer(report.getPlayerUUID()).getName(),
                    "&7Reporter: &6"+ Bukkit.getPlayer(report.getReporterUUID()).getName(),
                    "&7Date: &6" + date);
            item.setTitle("&a" + report.getPlayerUUID());
        }

    }

}
