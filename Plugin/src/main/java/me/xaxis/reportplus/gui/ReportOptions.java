package me.xaxis.reportplus.gui;

import me.xaxis.reportplus.enums.Lang;
import me.xaxis.reportplus.enums.ReportState;
import me.xaxis.reportplus.file.LangConfig;
import me.xaxis.reportplus.reports.Report;
import me.xaxis.reportplus.reports.ReportManager;
import me.xaxis.reportplus.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public final class ReportOptions implements InventoryHolder {

    private static final int GUI_SIZE = 54;

    private static final int DELETE_SLOT = 12;
    private static final int RESOLVE_SLOT = 13;
    private static final int CLOSE_SLOT = 45;
    private static final int BACK_SLOT = 53;

    private final ReportManager reportManager;
    private final LangConfig langConfig;
    private final UUID viewerUUID;
    private final Report report;
    private final Inventory inventory;

    public ReportOptions(
            ReportManager reportManager,
            LangConfig langConfig,
            UUID viewerUUID,
            Report report
    ) {
        this.reportManager = reportManager;
        this.langConfig = langConfig;
        this.viewerUUID = viewerUUID;
        this.report = report;

        inventory = Bukkit.createInventory(
                this,
                GUI_SIZE,
                langConfig.getString(Lang.GUI_OPTIONS_TITLE)
        );

        createItems();
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    public void openGUI(Player player) {
        if (!player.getUniqueId().equals(viewerUUID)) {
            return;
        }

        player.openInventory(inventory);
    }

    private void createItems() {
        inventory.setItem(
                DELETE_SLOT,
                new ItemUtils(Material.RED_CONCRETE)
                        .setTitle(
                                langConfig.getString(Lang.GUI_OPTIONS_DELETE),
                                true
                        )
                        .build()
        );

        if (report.getReportState() == ReportState.OPEN) {
            inventory.setItem(
                    RESOLVE_SLOT,
                    new ItemUtils(Material.BLACK_CONCRETE)
                            .setTitle(
                                    langConfig.getString(Lang.GUI_OPTIONS_RESOLVE),
                                    true
                            )
                            .build()
            );
        }

        inventory.setItem(
                CLOSE_SLOT,
                new ItemUtils(Material.BARRIER)
                        .setTitle(
                                langConfig.getString(Lang.GUI_OPTIONS_CLOSE),
                                true
                        )
                        .build()
        );

        inventory.setItem(
                BACK_SLOT,
                new ItemUtils(Material.ARROW)
                        .setTitle(
                                langConfig.getString(Lang.GUI_ITEM_GO_BACK),
                                true
                        )
                        .build()
        );
    }

    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        if (!player.getUniqueId().equals(viewerUUID)) {
            return;
        }

        switch (event.getRawSlot()) {
            case CLOSE_SLOT -> player.closeInventory();

            case BACK_SLOT -> openReportList(player);

            case DELETE_SLOT -> {
                boolean deleted = reportManager.deleteReport(
                        report.getReportUUID(),
                        report.getTargetUUID()
                );

                if (!deleted) {
                    return;
                }

                player.sendMessage(
                        langConfig.getString(Lang.REMOVED_REPORT)
                );

                openReportList(player);
            }

            case RESOLVE_SLOT -> {
                if (report.getReportState() != ReportState.OPEN) {
                    return;
                }

                boolean resolved = reportManager.resolveReport(
                        report.getTargetUUID(),
                        report.getReportUUID()
                );

                if (!resolved) {
                    return;
                }

                player.sendMessage(
                        langConfig.getString(Lang.SET_REPORT_AS_RESOLVED)
                );

                openReportList(player);
            }
        }
    }

    private void openReportList(Player player) {
        new ReportList(
                reportManager,
                langConfig
        ).openGUI(player);
    }
}