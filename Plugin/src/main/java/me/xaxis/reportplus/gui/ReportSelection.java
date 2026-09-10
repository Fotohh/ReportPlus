package me.xaxis.reportplus.gui;

import me.xaxis.reportplus.Main;
import me.xaxis.reportplus.enums.Lang;
import me.xaxis.reportplus.enums.Perms;
import me.xaxis.reportplus.enums.Placeholders;
import me.xaxis.reportplus.file.LangConfig;
import me.xaxis.reportplus.reports.Report;
import me.xaxis.reportplus.reports.ReportManager;
import me.xaxis.reportplus.reports.ReportType;
import me.xaxis.reportplus.reports.ReportTypeManager;
import me.xaxis.reportplus.utils.ItemUtils;
import me.xaxis.reportplus.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ReportSelection implements InventoryHolder  {

    private static final String GUI_TITLE = "Report Selection";

    private final ReportTypeManager reportTypeManager;
    private final ReportManager reportManager;
    private final LangConfig langConfig;

    private final Inventory i;
    private final UUID reporterUUID;
    private final String title;
    private final Main plugin;
    private final int size;
    private final UUID targetUUID;
    private final String targetName, reporterName;

    public int getSize() {
        return size;
    }

    public String getTitle() {
        return title;
    }

    public UUID getReporterUUID() {
        return reporterUUID;
    }

    public ReportSelection(
            Main plugin,
            UUID reporterUUID,
            UUID targetUUID,
            String targetName,
            String reporterName,
            LangConfig langConfig,
            ReportTypeManager reportTypeManager,
            ReportManager reportManager)
    {
        this.reportTypeManager = reportTypeManager;
        this.reportManager = reportManager;
        this.langConfig = langConfig;
        this.targetUUID = targetUUID;
        this.reporterUUID = reporterUUID;
        this.title = GUI_TITLE;
        this.targetName = targetName;
        this.reporterName = reporterName;
        size = 18;
        i = Bukkit.createInventory(this, size, Utils.chat(title));
        this.plugin = plugin;
    }

    public void openGUI(Player player){
        createItems();
        player.openInventory(getGUI());
    }

    public Inventory getGUI() {
        return i;
    }

    public void createItems() {
        for(ReportType type : reportTypeManager.getReportTypes()) {
            getGUI().addItem(new ItemUtils(type.material()).setTitle(type.displayName(), true).lore(type.lore()).build());
        }

        getGUI().setItem(size - 1, new ItemUtils(Material.BARRIER)
            .setTitle(langConfig.getString(Lang.GUI_SELECTION_ITEM_CANCEL), true)
            .lore(langConfig.getStringList(Lang.GUI_SELECTION_ITEM_CANCEL_LORE))
            .build()
        );
    }

    public void reportAlert(String target, String reporter, String type, String timestamp){
        Bukkit.getOnlinePlayers().stream()
                .filter(player -> player.hasPermission(Perms.REPORT_ALERT.getPermission())
                        && plugin.getConfig().getBoolean("report-list.toggle." + player.getUniqueId(), true))
                .forEach(player -> {
                    //todo report alert
                });
    }

    @Override
    public @NotNull Inventory getInventory() {
        return getGUI();
    }

    public void onClick(InventoryClickEvent event){

        if(!event.getInventory().equals(event.getWhoClicked().getOpenInventory().getTopInventory())) return;
        Player player = (Player) event.getWhoClicked();
        if(event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;
        if(!event.getCurrentItem().hasItemMeta()) return;
        ItemMeta itemMeta = event.getCurrentItem().getItemMeta();
        if(itemMeta == null) return;
        if(!itemMeta.hasDisplayName()) return;
        if(event.getRawSlot() == size - 1){
            player.closeInventory();
            return;
        }
        for(ReportType type : reportTypeManager.getReportTypes()) {
            if(event.getRawSlot() != type.slot()) continue;
            Report report = new Report(targetUUID, targetName, reporterUUID, reporterName, type.id());
            player.sendMessage(String.format(langConfig.getString(Lang.SUCCESSFUL_REPORT), targetName, type.id()));
            reportAlert(targetName, reporterName, type.id(), report.getTargetName());
            break;
        }

    }
}
