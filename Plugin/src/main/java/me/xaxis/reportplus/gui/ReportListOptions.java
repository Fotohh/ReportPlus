package me.xaxis.reportplus.gui;

import me.xaxis.reportplus.Main;
import me.xaxis.reportplus.file.LangConfig;
import me.xaxis.reportplus.reports.Report;
import me.xaxis.reportplus.reports.ReportManager;
import me.xaxis.reportplus.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;

public class ReportListOptions implements GUI{

    private final Inventory inventory;
    private final Main plugin;
    private final Player player;
    private final Report report;

    public ReportListOptions(Main plugin, Player player, Report report){
        this.plugin = plugin;
        this.player = player;
        this.report =report;
        inventory = Bukkit.createInventory(null, 6*9, "");
        registerListener(plugin).openGUI(player);
    }

    @Override
    public Inventory getGUI() {
        return inventory;
    }

    private ItemStack barrier,redConcrete,blackConcrete,arrow;

    @Override
    public void createItems() {
        //barrier close, red conc delete, black conc resolved, arrow back,
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
    @EventHandler
    public void onClick(InventoryClickEvent event) {

        Player p = (Player) event.getWhoClicked();

        if(!p.getUniqueId().equals(this.player.getUniqueId())) return;

        if(!p.equals(player)) return;
        if(event.getCurrentItem() == null) return;

        if(event.getCurrentItem().equals(barrier)){
            p.closeInventory();
        } else if (event.getCurrentItem().equals(redConcrete)) {
            p.closeInventory();
            ReportManager.deleteReport(report.getReportUUID(), plugin);
            GUI i = new ReportList("&aReport List", plugin);
            i.openGUI(p);
            HandlerList.unregisterAll(this);
        } else if (event.getCurrentItem().equals(blackConcrete)) {
            p.closeInventory();
            try {
                report.resolve();
            } catch (IOException e) {
                throw new RuntimeException("Failed to save config",e);
            }
            GUI i = new ReportList("&aReport List", plugin);
            i.openGUI(p);
            HandlerList.unregisterAll(this);
        } else if (event.getCurrentItem().equals(arrow)) {
            p.closeInventory();
            GUI i = new ReportList("&aReport List", plugin);
            i.openGUI(p);
            HandlerList.unregisterAll(this);
        }

        event.setCancelled(true);
    }
}
