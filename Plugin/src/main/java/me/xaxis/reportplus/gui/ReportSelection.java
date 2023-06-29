package me.xaxis.reportplus.gui;

import io.github.xaxisplayz.reportplus.api.ReportPlus;
import me.xaxis.reportplus.Main;
import me.xaxis.reportplus.enums.Lang;
import me.xaxis.reportplus.enums.Perms;
import me.xaxis.reportplus.enums.ReportType;
import me.xaxis.reportplus.reports.Report;
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

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

public class ReportSelection extends Utils implements GUI{

    private final int size = 9;

    private final Inventory i;
    private final Player player;

    private final Main plugin;

    public ReportSelection(Main plugin, String title, Player player){
        super(plugin);
        this.player = player;
        i = Bukkit.createInventory(null, size, Utils.chat(title));
        this.plugin = plugin;
        createItems();
        registerListener(plugin).openGUI(player);
    }

    @Override
    public Inventory getGUI() {
        return i;
    }

    @Override
    public void createItems() {
        for(ReportType type : ReportType.values()){
            ItemUtils item = new ItemUtils(type.getMaterial(plugin));
            item.setTitle("&a" + type.getDisplayName(plugin)).build();
            getGUI().addItem(item.i());
        }
    }

    @Override
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        String title = event.getView().getTitle();

        if(event.getClickedInventory() != getGUI()) return;

        if(!player.equals(this.player)) return;

        if(event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) {
            return;
        }

        OfflinePlayer target = Bukkit.getPlayer(UUID.fromString(ChatColor.stripColor(title)));

        if(!isTargetValid(target)) return;

        for(ReportType type : ReportType.values()){
            if(type.getMaterial(plugin) == event.getCurrentItem().getType()) {
                try {
                    Report report = new Report(plugin, target.getUniqueId(), player.getUniqueId(), type);
                    player.closeInventory();
                    message(player, Lang.SUCCESSFUL_REPORT, target.getName(), type.name());
                    if(ReportPlus.useDefaultAlert()) {
                        reportAlert(target.getName(), player.getName(), type.name(), new Date(report.getTimestamp()).toString());
                    }
                } catch (IOException e) {
                    throw new RuntimeException("Unable to register report.", e);
                }

            }
        }
        event.setCancelled(true);
    }

    private void reportAlert(String target, String reporter, String type, String timestamp){
        Bukkit.getOnlinePlayers().stream()
                .filter(player -> player.hasPermission(Perms.REPORT_ALERT.getPermission()))
                .forEach(player -> message(player,Lang.REPORT_ALERT,target,reporter,type,timestamp));
    }

}
