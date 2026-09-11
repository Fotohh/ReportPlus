package me.xaxis.reportplus.gui;

import me.xaxis.reportplus.enums.Lang;
import me.xaxis.reportplus.enums.Placeholders;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public final class ReportList implements InventoryHolder {

    private static final int GUI_SIZE = 54;
    private static final int REPORTS_PER_PAGE = 45;

    private static final int PREVIOUS_PAGE_SLOT = 45;
    private static final int OPEN_FILTER_SLOT = 46;
    private static final int RESOLVED_FILTER_SLOT = 47;
    private static final int ALL_FILTER_SLOT = 48;
    private static final int PAGE_NUMBER_SLOT = 49;
    private static final int NEXT_PAGE_SLOT = 53;

    private final ReportManager reportManager;
    private final LangConfig langConfig;
    private final Inventory inventory;

    /*
     * null = show reports from every player
     */
    private final UUID targetUUIDFilter;

    /*
     * true = this list is specifically for resolving reports,
     * so only OPEN reports can ever be shown.
     */
    private final boolean openOnly;

    private List<Report> displayedReports = List.of();

    private int currentPage = 1;
    private Filter filter = Filter.ALL;

    /*
     * Normal /reports list.
     */
    public ReportList(
            ReportManager reportManager,
            LangConfig langConfig
    ) {
        this(
                reportManager,
                langConfig,
                null,
                false
        );
    }

    /*
     * Filtered report list.
     */
    public ReportList(
            ReportManager reportManager,
            LangConfig langConfig,
            UUID targetUUIDFilter,
            boolean openOnly
    ) {
        this.reportManager = reportManager;
        this.langConfig = langConfig;
        this.targetUUIDFilter = targetUUIDFilter;
        this.openOnly = openOnly;

        if (openOnly) {
            filter = Filter.OPEN;
        }

        inventory = Bukkit.createInventory(
                this,
                GUI_SIZE,
                langConfig.getString(Lang.REPORT_LIST_TITLE)
        );
    }

    public void openGUI(Player player) {
        refreshReports();
        render();
        player.openInventory(inventory);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    private void refreshReports() {
        displayedReports = reportManager.getAllReports()
                .stream()

                // If a target UUID was provided, only show their reports.
                .filter(report ->
                        targetUUIDFilter == null
                                || report.getTargetUUID().equals(targetUUIDFilter)
                )

                // Resolve mode is permanently limited to open reports.
                .filter(report -> {
                    if (openOnly) {
                        return report.getReportState() == ReportState.OPEN;
                    }

                    return matchesFilter(report);
                })

                // Newest reports first.
                .sorted(
                        Comparator
                                .comparingLong(Report::getTimestamp)
                                .reversed()
                )

                .toList();

        int totalPages = getTotalPages();

        if (currentPage > totalPages) {
            currentPage = totalPages;
        }

        if (currentPage < 1) {
            currentPage = 1;
        }
    }

    private boolean matchesFilter(Report report) {
        return switch (filter) {
            case ALL -> true;
            case OPEN ->
                    report.getReportState() == ReportState.OPEN;
            case RESOLVED ->
                    report.getReportState() == ReportState.RESOLVED;
        };
    }

    private void render() {
        inventory.clear();

        int startIndex =
                (currentPage - 1) * REPORTS_PER_PAGE;

        int endIndex = Math.min(
                startIndex + REPORTS_PER_PAGE,
                displayedReports.size()
        );

        int slot = 0;

        for (int index = startIndex; index < endIndex; index++) {
            Report report = displayedReports.get(index);

            inventory.setItem(
                    slot,
                    createReportItem(report)
            );

            slot++;
        }

        createControls();
    }

    private ItemStack createReportItem(Report report) {
        Date date = new Date(report.getTimestamp());

        List<String> lore = langConfig
                .getStringList(
                        Lang.REPORT_LIST_ITEM_PLAYER_LORE
                )
                .stream()
                .map(line ->
                        replaceReportPlaceholders(
                                line,
                                report,
                                date
                        )
                )
                .toList();

        return new ItemUtils(Material.PLAYER_HEAD)
                .setTitle(
                        report.getTargetName(),
                        true
                )
                .lore(lore)
                .build();
    }

    private String replaceReportPlaceholders(
            String line,
            Report report,
            Date date
    ) {
        return line
                .replace(
                        Placeholders.REPORT_TYPE.toString(),
                        report.getReportTypeId()
                )
                .replace(
                        Placeholders.REPORTER.toString(),
                        report.getReporterName()
                )
                .replace(
                        Placeholders.REPORTED.toString(),
                        report.getTargetName()
                )
                .replace(
                        Placeholders.TIMESTAMP.toString(),
                        date.toString()
                )
                .replace(
                        Placeholders.REPORT_STATE.toString(),
                        report.getReportState().name()
                );
    }

    private void createControls() {
        inventory.setItem(
                PREVIOUS_PAGE_SLOT,
                createButton(
                        Material.ARROW,
                        langConfig.getString(
                                Lang.GUI_LIST_ITEM_PREVIOUS_PAGE
                        )
                )
        );

        /*
         * Resolve mode is locked to OPEN reports,
         * so filter buttons don't make sense there.
         */
        if (!openOnly) {
            inventory.setItem(
                    OPEN_FILTER_SLOT,
                    createFilterButton(
                            langConfig.getString(
                                    Lang.REPORT_LIST_ITEM_FILTER_OUT_RESOLVED
                            ),
                            filter == Filter.OPEN
                    )
            );

            inventory.setItem(
                    RESOLVED_FILTER_SLOT,
                    createFilterButton(
                            langConfig.getString(
                                    Lang.REPORT_LIST_ITEM_FILTER_OUT_OPEN
                            ),
                            filter == Filter.RESOLVED
                    )
            );

            inventory.setItem(
                    ALL_FILTER_SLOT,
                    createFilterButton(
                            langConfig.getString(
                                    Lang.REPORT_LIST_ITEM_SHOW_ALL
                            ),
                            filter == Filter.ALL
                    )
            );
        }

        inventory.setItem(
                PAGE_NUMBER_SLOT,
                createPageNumber()
        );

        inventory.setItem(
                NEXT_PAGE_SLOT,
                createButton(
                        Material.ARROW,
                        langConfig.getString(
                                Lang.REPORT_LIST_ITEM_NEXT_PAGE
                        )
                )
        );
    }

    private ItemStack createButton(
            Material material,
            String title
    ) {
        return new ItemUtils(material)
                .setTitle(title, true)
                .build();
    }

    private ItemStack createFilterButton(
            String title,
            boolean active
    ) {
        ItemStack item =
                new ItemUtils(Material.BOOK)
                        .setTitle(title, true)
                        .build();

        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setEnchantmentGlintOverride(active);
            item.setItemMeta(meta);
        }

        return item;
    }

    private ItemStack createPageNumber() {
        String title = langConfig
                .getString(
                        Lang.GUI_LIST_ITEM_CURRENT_PAGE
                )
                .replace(
                        Placeholders.CURRENT_PAGE.toString(),
                        String.valueOf(currentPage)
                )
                .replace(
                        Placeholders.TOTAL_PAGES.toString(),
                        String.valueOf(getTotalPages())
                );

        return new ItemUtils(Material.PAPER)
                .setTitle(title, true)
                .build();
    }

    private int getTotalPages() {
        return Math.max(
                1,
                (int) Math.ceil(
                        (double) displayedReports.size()
                                / REPORTS_PER_PAGE
                )
        );
    }

    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        int slot = event.getRawSlot();

        switch (slot) {

            case PREVIOUS_PAGE_SLOT -> {
                if (currentPage > 1) {
                    currentPage--;
                    render();
                }

                return;
            }

            case NEXT_PAGE_SLOT -> {
                if (currentPage < getTotalPages()) {
                    currentPage++;
                    render();
                }

                return;
            }

            case PAGE_NUMBER_SLOT -> {
                return;
            }

            case OPEN_FILTER_SLOT -> {
                if (openOnly) {
                    return;
                }

                filter = Filter.OPEN;
                currentPage = 1;

                refreshReports();
                render();

                return;
            }

            case RESOLVED_FILTER_SLOT -> {
                if (openOnly) {
                    return;
                }

                filter = Filter.RESOLVED;
                currentPage = 1;

                refreshReports();
                render();

                return;
            }

            case ALL_FILTER_SLOT -> {
                if (openOnly) {
                    return;
                }

                filter = Filter.ALL;
                currentPage = 1;

                refreshReports();
                render();

                return;
            }
        }

        if (slot < 0 || slot >= REPORTS_PER_PAGE) {
            return;
        }

        int reportIndex =
                ((currentPage - 1) * REPORTS_PER_PAGE)
                        + slot;

        if (reportIndex >= displayedReports.size()) {
            return;
        }

        Report report =
                displayedReports.get(reportIndex);

        new ReportOptions(
                reportManager,
                langConfig,
                player.getUniqueId(),
                report,
                this
        ).openGUI(player);
    }

    private enum Filter {
        ALL,
        OPEN,
        RESOLVED
    }
}