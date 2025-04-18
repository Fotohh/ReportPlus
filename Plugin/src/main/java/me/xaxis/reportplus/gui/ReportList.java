package me.xaxis.reportplus.gui;

import com.github.fotohh.itemutil.ItemBuilder;
import me.xaxis.reportplus.Main;
import me.xaxis.reportplus.enums.Lang;
import me.xaxis.reportplus.enums.PH;
import me.xaxis.reportplus.enums.ReportState;
import me.xaxis.reportplus.reports.Report;
import me.xaxis.reportplus.reports.ReportManager;
import me.xaxis.reportplus.utils.ItemUtils;
import me.xaxis.reportplus.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ReportList implements InventoryHolder {

    private final List<ItemStack> items = new ArrayList<>();
    private final int itemsPerPage;
    private int currentPage;
    private final Main plugin;
    private final Inventory gui;

    public ReportList(Main plugin) {
        this.plugin = plugin;
        String title = Utils.get(Lang.REPORT_LIST_TITLE);
        this.itemsPerPage = 45;
        this.currentPage = 1;
        gui = Bukkit.createInventory(this, 54, Utils.chat(title));
    }

    public void openGUI(Player player){
        createItems();
        player.openInventory(getGUI());
    }

    public Inventory getGUI() {
        return gui;
    }

    public void createItems() {
        items.clear();

        ConfigurationSection section = plugin.getConfig().getConfigurationSection("REPORT_TYPE");

        if(section == null) return;

        for(Report report : ReportManager.getReportUUIDMap().values()){
            if(!showAll) {
                if (report.getState() == ReportState.RESOLVED && filterResolved) continue;
                if (report.getState() == ReportState.OPEN && !filterResolved) continue;
            }
            Player target = Bukkit.getPlayer(report.getPlayerUUID());
            if(target == null) continue;
            PlayerProfile profile = target.getPlayerProfile();
            ItemUtils item = new ItemUtils(Material.PLAYER_HEAD);
            Date date = new Date(report.getTimestamp());
            /*item.lore("&7Report Type: &6" + report.getReportType(),
                            "&7Player: &6" + report.getPlayerName(),
                            "&7Reporter: &6"+ report.getTargetName(),
                            "&7Date: &6" + date,
                            "&7Report State: &6" + report.getState().name())*/
            String[] list = Utils.getSL(Lang.REPORT_LIST_ITEM_PLAYER_LORE, Map.of(
                    PH.REPORT_TYPE.toString(), report.getReportType(),
                    PH.REPORTER.toString(), report.getPlayerName(),
                    PH.REPORTED.toString(), report.getTargetName(),
                    PH.TIMESTAMP.toString(), date.toString(),
                    PH.REPORT_STATE.toString(), report.getState().toString()));
            item.lore(list)
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
        createItems();
        pagination();
    }

    private boolean filterResolved = false;

    private void pagination() {
        int counter = 0;
        int startIndex = (currentPage - 1) * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, items.size());
        for (int i = startIndex; i < endIndex; i++) {
            gui.setItem(counter, items.get(i));
            counter++;
        }
        gui.setItem(45, createPageButton(Utils.get(Lang.GUI_LIST_ITEM_PREVIOUS_PAGE)));


        ItemBuilder filterResolvedItem = new ItemBuilder(Material.BOOK).withTitle(Utils.get(Lang.REPORT_LIST_ITEM_FILTER_OUT_RESOLVED));
        ItemMeta filterResolvedMeta = filterResolvedItem.getItemMeta();
        filterResolvedMeta.setEnchantmentGlintOverride(filterResolved);
        filterResolvedItem.setItemMeta(filterResolvedMeta);

        filterResolvedItem.withLore(" ");
        gui.setItem(46, filterResolvedItem.build());

        ItemBuilder filterUnresolvedItem = new ItemBuilder(Material.BOOK).withTitle(Utils.get(Lang.REPORT_LIST_ITEM_FILTER_OUT_OPEN));
        ItemMeta filterUnresolvedMeta = filterUnresolvedItem.getItemMeta();
        if(!filterResolved) filterUnresolvedMeta.setEnchantmentGlintOverride(true);
        filterUnresolvedItem.setItemMeta(filterUnresolvedMeta);

        filterUnresolvedItem.withLore(" ");
        gui.setItem(47, filterUnresolvedItem.build());

        ItemBuilder allReportsItem = new ItemBuilder(Material.BOOK).withTitle(Utils.get(Lang.REPORT_LIST_ITEM_SHOW_ALL)).withLore(" ").build();
        gui.setItem(48, allReportsItem);

        gui.setItem(49, createPageNumber());
        gui.setItem(53, createPageButton(Utils.get(Lang.REPORT_LIST_ITEM_NEXT_PAGE)));
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
        meta.setDisplayName(Utils.get(Lang.GUI_LIST_ITEM_CURRENT_PAGE, Map.of(PH.CURRENT_PAGE.toString(), String.valueOf(currentPage), PH.TOTAL_PAGES.toString(), String.valueOf(getTotalPages()))));
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

    private boolean showAll = true;


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
                showAll = false;
                currentPage = 1;
                updateGUI();
            }
            case 47 -> {
                //filter unresolved reports
                filterResolved = false;
                showAll = false;
                currentPage = 1;
                updateGUI();
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
            case 48 -> {
                //show all reports
                showAll = true;
                currentPage = 1;
                updateGUI();
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
