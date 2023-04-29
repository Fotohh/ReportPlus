package me.xaxis.reportplus.listener;

import me.xaxis.reportplus.Main;
import me.xaxis.reportplus.enums.Lang;
import me.xaxis.reportplus.enums.Perms;
import me.xaxis.reportplus.enums.ReportType;
import me.xaxis.reportplus.reports.Report;
import me.xaxis.reportplus.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

public class OnClick extends Utils implements Listener {

    private final Main plugin;

    public OnClick(Main plugin) {
        super(plugin);
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();
        String title = event.getView().getTitle();

        OfflinePlayer target = Bukkit.getPlayer(UUID.fromString(title));

        if(target == null || event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) {
            return;
        }

        for(ReportType type : ReportType.values()){
            if(type.getMaterial(plugin) == event.getCurrentItem().getType()) {
                try {
                    Report report = new Report(plugin, target.getUniqueId(), player.getUniqueId(), type);
                    player.closeInventory();
                    message(player, Lang.SUCCESSFUL_REPORT, target.getName(), type.name());
                    reportAlert(target.getName(), player.getName(), type.name(), new Date(report.getTimestamp()).toString());
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
