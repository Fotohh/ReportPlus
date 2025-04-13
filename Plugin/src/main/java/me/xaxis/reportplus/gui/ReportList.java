package me.xaxis.reportplus.gui;

import com.github.fotohh.itemutil.EnchantmentBuilder;
import com.github.fotohh.itemutil.ItemBuilder;
import me.xaxis.reportplus.Main;
import me.xaxis.reportplus.reports.Report;
import me.xaxis.reportplus.reports.ReportManager;
import me.xaxis.reportplus.utils.ItemUtils;
import me.xaxis.reportplus.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ReportList implements InventoryHolder {

    public static String GUI_TITLE = "Reports List";

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
            Player target = Bukkit.getPlayer(report.getPlayerUUID());
            if(target == null) continue;
            PlayerProfile profile = target.getPlayerProfile();
            ItemUtils item = new ItemUtils(Material.PLAYER_HEAD);
            Date date = new Date(report.getTimestamp());
            item.lore("&7Report Type: &6" + report.getReportType(),
                            "&7Player: &6" + report.getPlayerName(),
                            "&7Reporter: &6"+ report.getTargetName(),
                            "&7Date: &6" + date,
                            "&7Report State: &6" + report.getState().name())
                    .setTitle(report.getReportUUID().toString(), false)
                    .build();
            SkullMeta meta = (SkullMeta) item.getItemMeta();
            meta.setOwnerProfile(profile);
            meta.setOwningPlayer(Bukkit.getOfflinePlayer(target.getUniqueId()));
            item.setItemMeta(meta);
            items.add(item);
        }
        pagination();
    }

    public void updateGUI() {
        gui.clear();
        pagination();
    }

    private boolean filterResolved = false, filterUnresolved = false;

    private void pagination() {
        int startIndex = (currentPage - 1) * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, items.size());
        for (int i = startIndex; i < endIndex; i++) {
            ItemStack item = items.get(i);
            if(item.getItemMeta().getLore().get(4).contains("RESOLVED") && filterResolved) continue;
            if(item.getItemMeta().getLore().get(4).contains("OPEN") && filterUnresolved) continue;
            gui.addItem(items.get(i));
        }
        gui.setItem(45, createPageButton("Previous Page"));

        if(filterUnresolved) filterUnresolved = false;
        if(filterResolved) filterResolved = false;

        ItemBuilder filterResolvedItem = new ItemBuilder(Material.BOOK).withTitle("Filter Resolved Reports");
        if(filterResolved) {
            filterResolvedItem.addEnchantment(new EnchantmentBuilder(Enchantment.KNOCKBACK, 1, true));
        }
        filterResolvedItem.withLore(" ");
        gui.setItem(46, filterResolvedItem.build());

        ItemBuilder filterUnresolvedItem = new ItemBuilder(Material.BOOK).withTitle("Filter Unresolved Reports");
        if(filterUnresolved) {
            filterUnresolvedItem.addEnchantment(new EnchantmentBuilder(Enchantment.KNOCKBACK, 1, true));
        }
        filterUnresolvedItem.withLore(" ");
        gui.setItem(47, filterUnresolvedItem.build());

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

    @Override
    public @NotNull Inventory getInventory() {
        return gui;
    }


    public void onClick(InventoryClickEvent event){

        Player player = (Player) event.getWhoClicked();

        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR || event.getCurrentItem().getItemMeta() == null) return;
        event.setCancelled(true);

        switch (event.getRawSlot()) {
            case 45 -> {
                if(currentPage > 1) {
                    currentPage--;
                    updateGUI();
                }
                return;
            }
            case 46 -> {
                //filter resolved reports
                filterResolved = true;
                currentPage = 1;
                pagination();
            }
            case 47 -> {
                //filter unresolved reports
                filterUnresolved = true;
                currentPage = 1;
                pagination();
            }
            case 49 -> {
                return;
            }
            case 53 -> {
                if(currentPage < getTotalPages()) {
                    currentPage++;
                    updateGUI();
                }
                return;
            }
        }

        UUID uuid;

        try {
            uuid = UUID.fromString(event.getCurrentItem().getItemMeta().getDisplayName());
        }catch (Exception e){
            return;
        }

        Report report = ReportManager.getReport(uuid);

        new ReportOptions(plugin, (Player) event.getWhoClicked(), report).openGUI(player);
    }
}
