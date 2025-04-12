package me.xaxis.reportplus.listeners;

import me.xaxis.reportplus.Main;
import me.xaxis.reportplus.enums.Lang;
import me.xaxis.reportplus.gui.ReportList;
import me.xaxis.reportplus.gui.ReportOptions;
import me.xaxis.reportplus.gui.ReportSelection;
import me.xaxis.reportplus.reports.Report;
import me.xaxis.reportplus.reports.ReportManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

public class OnInventoryClick implements Listener {

    private final Main plugin;

    public OnInventoryClick(Main plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    private String strip(String msg){
        return ChatColor.stripColor(msg);
    }

    @EventHandler
    public void InventoryClick(InventoryClickEvent event){

        String title = event.getView().getTitle();


        if(event.getInventory().getHolder() instanceof ReportList list){
            event.setCancelled(true);

        }


        if(title.equalsIgnoreCase(strip(ReportSelection.GUI_TITLE))){
            event.setCancelled(true);
            handleReportSelection(event);
        }else if(title.equalsIgnoreCase(strip(ReportOptions.GUI_TITLE))){
            event.setCancelled(true);
            handleReportOptions(event);
        }else if(title.equalsIgnoreCase(strip(ReportList.GUI_TITLE))){
            event.setCancelled(true);
            handleReportList(event);
        }

    }

    @EventHandler
    public void OnInventoryClose(InventoryCloseEvent event){
        String title = event.getView().getTitle();
        Player player = (Player) event.getPlayer();

        if(title.equalsIgnoreCase(strip(ReportSelection.GUI_TITLE))){
            ReportSelection.reportSelectionSessions.remove(player.getUniqueId());
        }else if(title.equalsIgnoreCase(strip(ReportOptions.GUI_TITLE))){
            ReportOptions.optionSessions.remove(player.getUniqueId());
        }else if(title.equalsIgnoreCase(strip(ReportList.GUI_TITLE))){
            ReportList.reportListSessions.remove(player.getUniqueId());
        }
    }

    private void handleReportSelection(InventoryClickEvent event){
        ReportSelection selection = ReportSelection.reportSelectionSessions.get(event.getWhoClicked().getUniqueId());
        if(selection == null){
            event.getWhoClicked().closeInventory();
            return;
        }

        Player player = (Player) event.getWhoClicked();
        if(event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;
        Material material = event.getCurrentItem().getType();

        OfflinePlayer target = Bukkit.getPlayer(selection.getTarget());

        if(target == null) return;

        if(event.getRawSlot() == selection.getSize() - 1){
            player.closeInventory();
        }

        ConfigurationSection sec = plugin.getConfig().getConfigurationSection("REPORT_TYPE");
        for(String s : sec.getKeys(false)){
            ConfigurationSection a = sec.getConfigurationSection(s);
            Material m = Material.getMaterial(a.getString("MATERIAL"));
            if(material != m) continue;
            try {
                Report report = new Report(plugin, target.getUniqueId(), target.getName(), player.getUniqueId(), s);
                player.closeInventory();
                selection.message(player, Lang.SUCCESSFUL_REPORT, target.getName(), s);
                selection.reportAlert(target.getName(), player.getName(),s, new Date(report.getTimestamp()).toString());
            } catch (IOException e) {
                throw new RuntimeException("Unable to register report.", e);
            }
        }
    }

    private void handleReportList(InventoryClickEvent event){

        //add the ability to select different GUI's... Resolved, Unresolved GUI's. Archiving GUI's, replacing each item with player heads
        Player player = (Player) event.getWhoClicked();

        ReportList list = ReportList.reportListSessions.get(player.getUniqueId());

        if(list == null){
            player.closeInventory();
            return;
        }

        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR || event.getCurrentItem().getItemMeta() == null) return;
        event.setCancelled(true);
        if (event.getRawSlot() == 45) { // prev page
            if(list.getCurrentPage() > 1) {
                list.setCurrentPage(list.getCurrentPage() - 1);
                list.updateGUI();
            }
            return;
        } else if (event.getRawSlot() == 53) { // next page
            if(list.getCurrentPage() < list.getTotalPages()) {
                list.setCurrentPage(list.getCurrentPage() + 1);
                list.updateGUI();
            }
            return;
        } else if(event.getRawSlot() == 49) return; // page number

        UUID uuid;

        try {
            uuid = UUID.fromString(event.getCurrentItem().getItemMeta().getDisplayName());
        }catch (Exception e){
            return;
        }

        Report report = ReportManager.getReport(uuid);

        new ReportOptions(plugin, (Player) event.getWhoClicked(), report).openGUI(player);
    }

    private void handleReportOptions(InventoryClickEvent event){

        //adding custom notes to the player, toggle resolved? (dont know if i did that), more information listed
        //maybe ban player thru the GUI with custom time and reason?? might be overstepping bounds..
        Player player = (Player) event.getWhoClicked();
        ReportOptions options = ReportOptions.optionSessions.get(player.getUniqueId());
        if(options == null){
            player.closeInventory();
            return;
        }

        if(event.getCurrentItem() == null) return;

        if(event.getCurrentItem().equals(options.getBarrier())){
            player.closeInventory();
        } else if (event.getCurrentItem().equals(options.getRedConcrete())) {
            player.closeInventory();
            ReportManager.deleteReport(options.getReport().getReportUUID(), plugin);
            new ReportList(plugin, player).openGUI(player);
            player.sendMessage(ChatColor.RED + "You have removed the report from the list.");
        } else if (event.getCurrentItem().equals(options.getBlackConcrete())) {
            player.closeInventory();
            try {
                options.getReport().resolve();
            } catch (IOException e) {
                throw new RuntimeException("Failed to save config",e);
            }
            new ReportList(plugin, player).openGUI(player);
            player.sendMessage(ChatColor.GREEN + "You have set the report as resolved.");
        } else if (event.getCurrentItem().equals(options.getArrow())) {
            player.closeInventory();
            new ReportList(plugin, player).openGUI(player);
        }

        event.setCancelled(true);
    }
}
