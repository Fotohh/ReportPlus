package me.xaxis.reportplus.gui;

import me.xaxis.reportplus.enums.Lang;
import me.xaxis.reportplus.file.LangConfig;
import me.xaxis.reportplus.reports.*;
import me.xaxis.reportplus.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ReportSelection implements InventoryHolder {

    private static final String GUI_TITLE = "Report Selection";
    private static final int GUI_SIZE = 18;

    private final ReportTypeManager reportTypeManager;
    private final ReportService reportService;
    private final LangConfig langConfig;
    private final Inventory inventory;
    private final String targetName, reporterName;
    private final UUID targetUUID, reporterUUID;

    public ReportSelection(
            LangConfig langConfig,
            ReportTypeManager reportTypeManager,
            ReportService reportService,
            String targetName,
            String reporterName,
            UUID targetUUID,
            UUID reporterUUID)
    {
        this.reportTypeManager = reportTypeManager;
        this.reportService = reportService;
        this.langConfig = langConfig;
        this.targetName = targetName;
        this.reporterName = reporterName;
        this.targetUUID = targetUUID;
        this.reporterUUID = reporterUUID;

        inventory = Bukkit.createInventory(this, GUI_SIZE, GUI_TITLE);
        createItems();
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    public void openGUI(Player player){
        if(!player.getUniqueId().equals(reporterUUID)) return;
        player.openInventory(inventory);
    }

    private void createItems() {
        for(ReportType type : reportTypeManager.getReportTypes()) {
            inventory.setItem(type.slot(), new ItemUtils(type.material()).setTitle(type.displayName(), true).lore(type.lore()).build());
        }

        inventory.setItem(GUI_SIZE - 1, new ItemUtils(Material.BARRIER)
            .setTitle(langConfig.getString(Lang.GUI_SELECTION_ITEM_CANCEL), true)
            .lore(langConfig.getStringList(Lang.GUI_SELECTION_ITEM_CANCEL_LORE))
            .build()
        );
    }

    public void onClick(InventoryClickEvent event){
        if(!(event.getWhoClicked() instanceof Player player)) return;
        if(event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;
        if(!player.getUniqueId().equals(reporterUUID)) return;
        if(event.getRawSlot() == GUI_SIZE - 1){
            player.closeInventory();
            return;
        }
        for(ReportType type : reportTypeManager.getReportTypes()) {
            if(event.getRawSlot() != type.slot()) continue;
            reportService.createReport(targetUUID, targetName, reporterUUID, reporterName, type);
            player.closeInventory();
            //todo send message
            //todo send alert
            break;
        }

    }
}
