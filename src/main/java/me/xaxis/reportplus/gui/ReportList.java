package me.xaxis.reportplus.gui;

import me.xaxis.reportplus.Main;
import me.xaxis.reportplus.reports.Report;
import me.xaxis.reportplus.reports.ReportManager;
import me.xaxis.reportplus.utils.ItemUtils;
import me.xaxis.reportplus.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReportList implements GUI {

    private final List<ItemStack> items = new ArrayList<>();
    private final int itemsPerPage;
    private int currentPage;
    private final Main plugin;
    private final String title;
    private final Inventory gui;

    public ReportList(String title, Main plugin) {
        this.title = title;
        this.plugin = plugin;
        this.itemsPerPage = 45;
        this.currentPage = 1;
        gui = Bukkit.createInventory(null, 54, Utils.chat(title));
        registerListener(plugin);
    }

    @Override
    public Inventory getGUI() {
        return gui;
    }

    @Override
    public void createItems() {

        for(Report report : ReportManager.getReports()){
            ItemUtils item = new ItemUtils(report.getReportType().getMaterial(plugin));
            Date date = new Date(report.getTimestamp());
            item.lore("&7Report Type: &6" + report.getReportType().toString(),
                            "&7Player: &6" + report.getPlayerName(),
                            "&7Reporter: &6"+ report.getTargetName(),
                            "&7Date: &6" + date,
                            "&7Report State: &6" + report.getState().name())
                    .setTitle("&a" + report.getPlayerName())
                    .build();
            items.add(item.i());
        }

        int startIndex = (currentPage - 1) * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, items.size());
        for (int i = startIndex; i < endIndex; i++) {
            gui.addItem(items.get(i));
        }
        gui.setItem(45, createPageButton("Previous Page", Material.ARROW, currentPage - 1));
        gui.setItem(49, createPageNumber());
        gui.setItem(53, createPageButton("Next Page", Material.ARROW, currentPage + 1));

    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {

        if (event.getClickedInventory() != getGUI()) return;

        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR || event.getCurrentItem().getItemMeta() == null) return;
        event.setCancelled(true);
        if (event.getRawSlot() == 45 && currentPage > 1) {
            currentPage--;
            event.getWhoClicked().openInventory(getGUI());
        } else if (event.getRawSlot() == 53 && currentPage < getTotalPages()) {
            currentPage++;
            event.getWhoClicked().openInventory(getGUI());
        } else if(event.getRawSlot() == 49) return;

        OfflinePlayer player = Bukkit.getPlayer(ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()));

        if (player == null) return;

        Report report = ReportManager.getReport(player.getUniqueId());

        new ReportListOptions(plugin, (Player) event.getWhoClicked(), report);

        event.setCancelled(true);

    }

    private ItemStack createPageButton(String name, Material material, int page) {
        ItemStack item = new ItemStack(material);
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

    private int getTotalPages() {
        return (int) Math.ceil((double) items.size() / itemsPerPage);
    }
}
