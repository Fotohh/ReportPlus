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
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ReportSelection extends Utils implements InventoryHolder  {

    public static String GUI_TITLE = "Report Selection";

    public static Map<UUID, ReportSelection> reportSelectionSessions = new ConcurrentHashMap<>();

    private final Inventory i;
    private final UUID player;
    private final String title;
    private final Main plugin;
    private final int size;
    private final UUID uuid;
    private final UUID target;

    public int getSize() {
        return size;
    }

    public String getTitle() {
        return title;
    }

    public UUID getUuid() {
        return uuid;
    }

    public ReportSelection(Main plugin, Player player, UUID target){
        super(plugin);
        uuid = player.getUniqueId();
        this.target = target;
        this.player = player.getUniqueId();
        this.title = GUI_TITLE;
        size = 18;
        i = Bukkit.createInventory(null, size, Utils.chat(title));
        this.plugin = plugin;
        reportSelectionSessions.put(player.getUniqueId(), this);
    }

    public UUID getTarget() {
        return target;
    }

    public void openGUI(Player player){
        createItems();
        player.openInventory(getGUI());
    }

    public Inventory getGUI() {
        return i;
    }

    public void createItems() {
        for(String key : plugin.getConfig().getConfigurationSection("REPORT_TYPE").getKeys(false)){
            getGUI().addItem(parseReport(plugin.getConfig().getConfigurationSection("REPORT_TYPE").getConfigurationSection(key)));
        }

        getGUI().setItem(size - 1, new ItemUtils(Material.BARRIER)
            .setTitle("&cCancel", true)
            .lore("&7Click to close the inventory")
            .build()
        );
    }

    private String strip(String msg){
        return ChatColor.stripColor(msg);
    }

    public void reportAlert(String target, String reporter, String type, String timestamp){
        Bukkit.getOnlinePlayers().stream()
                .filter(player -> player.hasPermission(Perms.REPORT_ALERT.getPermission())
                        && plugin.getConfig().getBoolean("report-list.toggle." + player.getUniqueId()))
                .forEach(player -> message(player,Lang.REPORT_ALERT,target,reporter,type,timestamp));
    }

    private ItemStack parseReport(ConfigurationSection section){
        Material material = Material.getMaterial(section.getString("MATERIAL"));
        String displayName = section.getString("DISPLAY_NAME");
        ItemUtils item = new ItemUtils(material);
        return item.setTitle(displayName,true).lore("&7Left or right click", "&7to select this","&7report type").build();
    }

    @Override
    public @NotNull Inventory getInventory() {
        return getGUI();
    }
}
