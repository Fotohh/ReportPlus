package me.xaxis.reportplus.gui;

import me.xaxis.reportplus.Main;
import me.xaxis.reportplus.reports.Report;
import me.xaxis.reportplus.reports.ReportManager;
import me.xaxis.reportplus.utils.ItemUtils;
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

    public static String GUI_TITLE = "Report Options";

    private final Inventory inventory;
    private final Main plugin;
    private final UUID player;
    private final Report report;
    private final UUID uuid;

    public ReportOptions(Main plugin, Player player, Report report){
        uuid = player.getUniqueId();
        this.plugin = plugin;
        this.player = player.getUniqueId();
        this.report =report;
        inventory = Bukkit.createInventory(null, 6*9, GUI_TITLE);
    }

    public UUID getUuid() {
        return uuid;
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

    public ItemStack getBarrier() {
        return barrier;
    }

    public ItemStack getArrow() {
        return arrow;
    }

    public ItemStack getBlackConcrete() {
        return blackConcrete;
    }

    public ItemStack getRedConcrete() {
        return redConcrete;
    }

    private ItemStack barrier,redConcrete,blackConcrete,arrow;

    public void createItems() {
        barrier = new ItemUtils(Material.BARRIER)
                .setTitle("&cClose Inventory",true)
                .build();
        redConcrete = new ItemUtils(Material.RED_CONCRETE)
                .setTitle("&cDelete Report",true)
                .build();
        blackConcrete = new ItemUtils(Material.BLACK_CONCRETE)
                .setTitle("&aSet as resolved",true)
                .build();
        arrow = new ItemUtils(Material.ARROW)
                .setTitle("&7Go back",true)
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

        //adding custom notes to the player, toggle resolved? (dont know if i did that), more information listed
        //maybe ban player thru the GUI with custom time and reason?? might be overstepping bounds..
        Player player = (Player) event.getWhoClicked();

        if(event.getCurrentItem() == null) return;

        if(event.getCurrentItem().equals(getBarrier())){
            player.closeInventory();
        } else if (event.getCurrentItem().equals(getRedConcrete())) {
            player.closeInventory();
            ReportManager.deleteReport(getReport().getReportUUID(), plugin);
            new ReportList(plugin, player).openGUI(player);
            player.sendMessage(ChatColor.RED + "You have removed the report from the list.");
        } else if (event.getCurrentItem().equals(getBlackConcrete())) {
            player.closeInventory();
            try {
                getReport().resolve();
            } catch (IOException e) {
                throw new RuntimeException("Failed to save config",e);
            }
            new ReportList(plugin, player).openGUI(player);
            player.sendMessage(ChatColor.GREEN + "You have set the report as resolved.");
        } else if (event.getCurrentItem().equals(getArrow())) {
            player.closeInventory();
            new ReportList(plugin, player).openGUI(player);
        }

        event.setCancelled(true);
    }
}
