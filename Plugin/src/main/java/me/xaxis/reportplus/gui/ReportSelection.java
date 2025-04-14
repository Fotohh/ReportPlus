package me.xaxis.reportplus.gui;

import com.github.fotohh.itemutil.ItemBuilder;
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
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ReportSelection extends Utils implements InventoryHolder  {

    public static String GUI_TITLE = "Report Selection";

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
        i = Bukkit.createInventory(this, size, Utils.chat(title));
        this.plugin = plugin;
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
            .setTitle(Utils.get(Lang.GUI_SELECTION_ITEM_CANCEL), true)
            .lore(Utils.get(Lang.GUI_SELECTION_ITEM_CANCEL_LORE))
            .build()
        );
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
        List<String> lore = section.getStringList("LORE").stream().map(Utils::chat).toList();
        return new ItemBuilder(material).withTitle(displayName).withLore(lore.toArray(new String[0])).build();
    }

    @Override
    public @NotNull Inventory getInventory() {
        return getGUI();
    }

    public void onClick(InventoryClickEvent event){

        Player player = (Player) event.getWhoClicked();
        if(event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;
        Material material = event.getCurrentItem().getType();

        OfflinePlayer target = Bukkit.getPlayer(getTarget());

        if(target == null) return;

        if(event.getRawSlot() == size - 1){
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
                message(player, Lang.SUCCESSFUL_REPORT, target.getName(), s);
                reportAlert(target.getName(), player.getName(),s, new Date(report.getTimestamp()).toString());
            } catch (IOException e) {
                throw new RuntimeException("Unable to register report.", e);
            }
        }
    }
}
