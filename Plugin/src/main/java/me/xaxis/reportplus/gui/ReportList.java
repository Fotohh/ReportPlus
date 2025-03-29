package me.xaxis.reportplus.gui;

import me.xaxis.reportplus.Main;
import me.xaxis.reportplus.reports.Report;
import me.xaxis.reportplus.reports.ReportManager;
import me.xaxis.reportplus.utils.ItemUtils;
import me.xaxis.reportplus.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ReportList {

    public static String GUI_TITLE = "Reports List";

    public static Map<UUID, ReportList> reportListSessions = new ConcurrentHashMap<>();

    private final List<ItemStack> items = new ArrayList<>();
    private final String title;
    private final int itemsPerPage;
    private int currentPage;
    private final Main plugin;
    private final Inventory gui;
    private final UUID uuid;

    public ReportList(Main plugin, Player player) {
        uuid = player.getUniqueId();
        this.plugin = plugin;
        this.title = GUI_TITLE;
        this.itemsPerPage = 45;
        this.currentPage = 1;
        gui = Bukkit.createInventory(null, 54, Utils.chat(title));
        reportListSessions.put(uuid, this);
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public String getTitle() {
        return title;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void openGUI(Player player){
        createItems();
        player.openInventory(getGUI());
    }

    public Inventory getGUI() {
        return gui;
    }

    public void createItems() {

        ConfigurationSection section = plugin.getConfig().getConfigurationSection("REPORT_TYPE");

        if(section == null) return;

        for(Report report : ReportManager.getReportUUIDMap().values()){
            ConfigurationSection reportSection = section.getConfigurationSection(report.getReportType());
            if(reportSection == null) continue;
            String mat = reportSection.getString("MATERIAL");
            if(mat == null) continue;
            Material material = Material.getMaterial(mat);
            ItemUtils item = new ItemUtils(material);
            Date date = new Date(report.getTimestamp());
            item.lore("&7Report Type: &6" + report.getReportType(),
                            "&7Player: &6" + report.getPlayerName(),
                            "&7Reporter: &6"+ report.getTargetName(),
                            "&7Date: &6" + date,
                            "&7Report State: &6" + report.getState().name())
                    .setTitle(report.getReportUUID().toString(), false)
                    .build();
            items.add(item.i());
        }

        pagination();

    }

    public void updateGUI() {
        gui.clear();
        pagination();
    }

    private void pagination() {
        int startIndex = (currentPage - 1) * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, items.size());
        for (int i = startIndex; i < endIndex; i++) {
            gui.addItem(items.get(i));
        }
        gui.setItem(45, createPageButton("Previous Page"));
        gui.setItem(49, createPageNumber());
        gui.setItem(53, createPageButton("Next Page"));
    }

    private ItemStack createPageButton(String name) {
        ItemStack item = new ItemStack(Material.ARROW);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Utils.chat(name));
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack createPageNumber() {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Utils.chat("&7Page &6" + currentPage + "/" + getTotalPages()));
        item.setItemMeta(meta);
        return item;
    }

    public int getTotalPages() {
        return (int) Math.ceil((double) items.size() / itemsPerPage);
    }
}
