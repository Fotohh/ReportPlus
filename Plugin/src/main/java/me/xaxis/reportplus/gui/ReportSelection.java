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

    private final Main plugin;

    public ReportSelection(Main plugin, String title, Player player){
        super(plugin);
        this.player = player;
        int size = 18;
        i = Bukkit.createInventory(null, size, Utils.chat(title));
        this.plugin = plugin;
        registerListener(plugin).openGUI(player);
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

    @Override
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        String title = event.getView().getTitle();
        if(event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;
        Material material = event.getCurrentItem().getType();

        if(event.getClickedInventory() != getGUI()) return;

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
                Report report = new Report(plugin, target.getUniqueId(), player.getUniqueId(), s);
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
        return item.setTitle(displayName).lore("&7Left or right click", "&7to select this","&7report type").build();
        //    FLYING("REPORT_TYPE.FLYING.MATERIAL", "REPORT_TYPE.FLYING.MATERIAL.DISPLAY_NAME"),
        //    BHOP("REPORT_TYPE.BHOP.MATERIAL", "REPORT_TYPE.BHOP.MATERIAL.DISPLAY_NAME"),
        //    AUTOCLICKING("REPORT_TYPE.AUTOCLICKING.MATERIAL", "REPORT_TYPE.AUTOCLICKING.MATERIAL.DISPLAY_NAME"),
        //    VELOCITY("REPORT_TYPE.VELOCITY.MATERIAL","REPORT_TYPE.VELOCITY.MATERIAL.DISPLAY_NAME"),
        //    AIMBOT("REPORT_TYPE.AIMBOT.MATERIAL","REPORT_TYPE.AIMBOT.MATERIAL.DISPLAY_NAME"),
        //    KILLAURA("REPORT_TYPE.KILLAURA.MATERIAL","REPORT_TYPE.KILLAURA.MATERIAL.DISPLAY_NAME"),
        //    NUKER("REPORT_TYPE.NUKER.MATERIAL","REPORT_TYPE.NUKER.MATERIAL.DISPLAY_NAME");

    }

}
