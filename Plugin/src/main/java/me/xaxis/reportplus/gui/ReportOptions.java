package me.xaxis.reportplus.gui;

import me.xaxis.reportplus.Main;
import me.xaxis.reportplus.enums.Lang;
import me.xaxis.reportplus.reports.Report;
import me.xaxis.reportplus.reports.ReportManager;
import me.xaxis.reportplus.utils.ItemUtils;
import me.xaxis.reportplus.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ReportOptions implements InventoryHolder {

    private final Inventory inventory;
    private final Main plugin;
    private final UUID player;
    private final Report report;

    public ReportOptions(Main plugin, Player player, Report report){
        this.plugin = plugin;
        this.player = player.getUniqueId();
        this.report =report;
        inventory = Bukkit.createInventory(this, 6*9, Utils.get(Lang.GUI_OPTIONS_TITLE, plugin));
    }

    public Report getReport() {
        return report;
    }

    public UUID getPlayer() {
        return player;
    }

    public void openGUI(Player player) {
        createItems();
        player.openInventory(getGUI());
    }

    public Inventory getGUI() {
        return inventory;
    }

    public void createItems() {
        ItemStack barrier = new ItemUtils(Material.BARRIER)
                .setTitle(Utils.get(Lang.GUI_OPTIONS_CLOSE, plugin), true)
                .build();
        ItemStack redConcrete = new ItemUtils(Material.RED_CONCRETE)
                .setTitle(Utils.get(Lang.GUI_OPTIONS_DELETE, plugin), true)
                .build();
        ItemStack blackConcrete = new ItemUtils(Material.BLACK_CONCRETE)
                .setTitle(Utils.get(Lang.GUI_OPTIONS_RESOLVE, plugin), true)
                .build();
        ItemStack arrow = new ItemUtils(Material.ARROW)
                .setTitle(Utils.get(Lang.GUI_ITEM_GO_BACK, plugin), true)
                .build();
        getGUI().setItem(12, redConcrete);
        getGUI().setItem(13, blackConcrete);
        getGUI().setItem(45, barrier);
        getGUI().setItem(53, arrow);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    public void onClick(InventoryClickEvent event){

        Player player = (Player) event.getWhoClicked();

        if(event.getCurrentItem() == null) return;

        switch (event.getCurrentItem().getType()) {
            case AIR -> {
            }
            case BARRIER -> player.closeInventory();
            case ARROW -> {
                player.closeInventory();
                new ReportList(plugin).openGUI(player);
            }
            case RED_CONCRETE -> {
                player.closeInventory();
                ReportManager.deleteReport(getReport().getReportUUID(), plugin);
                new ReportList(plugin).openGUI(player);
                player.sendMessage(Utils.getP(Lang.REMOVED_REPORT, plugin));
            }
            case BLACK_CONCRETE -> {
                player.closeInventory();
                try {
                    getReport().resolve();
                } catch (IOException e) {
                    throw new RuntimeException("Failed to save config",e);
                }
                new ReportList(plugin).openGUI(player);
                player.sendMessage(Utils.getP(Lang.SET_REPORT_AS_RESOLVED, plugin));

            }
        }
    }

}
