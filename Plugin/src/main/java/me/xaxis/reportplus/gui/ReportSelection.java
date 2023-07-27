package me.xaxis.reportplus.gui;

import me.xaxis.reportplus.Main;
import me.xaxis.reportplus.enums.Lang;
import me.xaxis.reportplus.enums.Perms;
import me.xaxis.reportplus.reports.Report;
import me.xaxis.reportplus.utils.ItemUtils;
import me.xaxis.reportplus.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

public class ReportSelection extends Utils implements GUI{

    private final Inventory i;
    private final Player player;
    private String title;
    private final Main plugin;

    public ReportSelection(Main plugin, String title, Player player){
        super(plugin);
        this.player = player;
        this.title = title;
        int size = 18;
        i = Bukkit.createInventory(null, size, Utils.chat(title));
        this.plugin = plugin;
        registerListener(plugin);
    }

    @Override
    public Inventory getGUI() {
        return i;
    }

    @Override
    public void createItems() {
        for(String key : plugin.getConfig().getConfigurationSection("REPORT_TYPE").getKeys(false)){
            getGUI().addItem(parseReport(plugin.getConfig().getConfigurationSection("REPORT_TYPE").getConfigurationSection(key)));
        }
    }

    private String strip(String msg){
        return ChatColor.stripColor(msg);
    }

    @Override
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        String title = event.getView().getTitle();
        if(event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;
        Material material = event.getCurrentItem().getType();

        if(!player.getUniqueId().equals(this.player.getUniqueId())) return;

        if(!player.equals(this.player)) return;

        if(event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) {
            return;
        }

        OfflinePlayer target = Bukkit.getPlayer(UUID.fromString(ChatColor.stripColor(title)));

        if(!isTargetValid(target)) return;
        ConfigurationSection sec = plugin.getConfig().getConfigurationSection("REPORT_TYPE");
        for(String s : sec.getKeys(false)){
            ConfigurationSection a = sec.getConfigurationSection(s);
            Material m = Material.getMaterial(a.getString("MATERIAL"));
            if(material != m) continue;

            try {
                Report report = new Report(plugin, target.getUniqueId(), target.getName(), player.getUniqueId(), s);
                player.closeInventory();
                message(player, Lang.SUCCESSFUL_REPORT, target.getName(), s);
                reportAlert(target.getName(), player.getName(),s, new Date(report.getTimestamp()).toString());
            } catch (IOException e) {
                throw new RuntimeException("Unable to register report.", e);
            }

        }


        //set report
        event.setCancelled(true);
    }

    private void reportAlert(String target, String reporter, String type, String timestamp){
        Bukkit.getOnlinePlayers().stream()
                .filter(player -> player.hasPermission(Perms.REPORT_ALERT.getPermission()))
                .forEach(player -> message(player,Lang.REPORT_ALERT,target,reporter,type,timestamp));
    }

    private ItemStack parseReport(ConfigurationSection section){
        Material material = Material.getMaterial(section.getString("MATERIAL"));
        String displayName = section.getString("DISPLAY_NAME");
        ItemUtils item = new ItemUtils(material);
        return item.setTitle(displayName,true).lore("&7Left or right click", "&7to select this","&7report type").build();

    }

}
