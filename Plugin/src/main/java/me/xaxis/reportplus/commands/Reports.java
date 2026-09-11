package me.xaxis.reportplus.commands;

import me.xaxis.reportplus.enums.Lang;
import me.xaxis.reportplus.enums.Perms;
import me.xaxis.reportplus.file.LangConfig;
import me.xaxis.reportplus.gui.ReportList;
import me.xaxis.reportplus.player.PlayerDataManager;
import me.xaxis.reportplus.reports.Report;
import me.xaxis.reportplus.reports.ReportManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public final class Reports implements CommandExecutor {

    private final ReportManager reportManager;
    private final PlayerDataManager playerDataManager;
    private final LangConfig langConfig;

    public Reports(
            ReportManager reportManager,
            PlayerDataManager playerDataManager,
            LangConfig langConfig
    ) {
        this.reportManager = reportManager;
        this.playerDataManager = playerDataManager;
        this.langConfig = langConfig;
    }

    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String[] args
    ) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage(
                    langConfig.getString(Lang.SENDER_NOT_PLAYER)
            );
            return true;
        }

        if (args.length == 0) {
            openReportList(player);
            return true;
        }

        if (args.length == 1) {
            handleSingleArgument(player, args[0]);
            return true;
        }

        if (args.length == 2) {
            handleTwoArguments(
                    player,
                    args[0],
                    args[1]
            );
            return true;
        }

        player.sendMessage(
                langConfig.getString(Lang.INVALID_REPORTS_USAGE)
        );

        return true;
    }

    private void openReportList(Player player) {

        if (!player.hasPermission(Perms.LIST_REPORTS.getPermission())) {
            player.sendMessage(
                    langConfig.getString(Lang.NO_PERMISSION)
            );
            return;
        }

        new ReportList(
                reportManager,
                langConfig
        ).openGUI(player);
    }

    private void handleSingleArgument(
            Player player,
            String argument
    ) {

        if (!argument.equalsIgnoreCase("toggle")) {
            player.sendMessage(
                    langConfig.getString(Lang.INVALID_REPORTS_USAGE)
            );
            return;
        }

        if (!player.hasPermission(Perms.REPORT_ALERT.getPermission())) {
            player.sendMessage(
                    langConfig.getString(Lang.NO_PERMISSION)
            );
            return;
        }

        Optional<Boolean> enabled =
                playerDataManager.toggleReportAlerts(
                        player.getUniqueId()
                );

        if (enabled.isEmpty()) {
            player.sendMessage(
                    langConfig.getString(Lang.REPORT_FAILED)
            );
            return;
        }

        if (enabled.get()) {
            player.sendMessage(
                    langConfig.getString(
                            Lang.TOGGLED_REPORT_ON
                    )
            );
        } else {
            player.sendMessage(
                    langConfig.getString(
                            Lang.TOGGLED_REPORT_OFF
                    )
            );
        }
    }

    private void handleTwoArguments(
            Player player,
            String subCommand,
            String targetName
    ) {

        if (subCommand.equalsIgnoreCase("delete")) {
            handleDelete(player, targetName);
            return;
        }

        if (subCommand.equalsIgnoreCase("resolve")) {
            handleResolve(player, targetName);
            return;
        }

        player.sendMessage(
                langConfig.getString(Lang.INVALID_REPORTS_USAGE)
        );
    }

    private void handleDelete(
            Player player,
            String targetName
    ) {

        if (!player.hasPermission(Perms.LIST_REPORTS.getPermission())) {
            player.sendMessage(
                    langConfig.getString(Lang.NO_PERMISSION)
            );
            return;
        }

        UUID targetUUID =
                playerDataManager.getPlayerUUID(targetName);

        if (targetUUID == null) {
            player.sendMessage(
                    langConfig.getString(Lang.INVALID_PLAYER)
            );
            return;
        }

        List<Report> reports =
                reportManager.getReports(targetUUID);

        if (reports.isEmpty()) {
            player.sendMessage(
                    langConfig.getString(Lang.REPORT_NOT_FOUND)
            );
            return;
        }

        if (reports.size() > 1) {
            new ReportList(
                    reportManager,
                    langConfig,
                    targetUUID,
                    false
            ).openGUI(player);

            return;
        }

        Report report = reports.getFirst();

        boolean deleted = reportManager.deleteReport(
                report.getReportUUID(),
                targetUUID
        );

        if (!deleted) {
            player.sendMessage(
                    langConfig.getString(Lang.REPORT_NOT_FOUND)
            );
            return;
        }

        player.sendMessage(
                langConfig.getString(Lang.DELETED_REPORT)
        );
    }

    private void handleResolve(
            Player player,
            String targetName
    ) {

        if (!player.hasPermission(Perms.LIST_REPORTS.getPermission())) {
            player.sendMessage(
                    langConfig.getString(Lang.NO_PERMISSION)
            );
            return;
        }

        UUID targetUUID =
                playerDataManager.getPlayerUUID(targetName);

        if (targetUUID == null) {
            player.sendMessage(
                    langConfig.getString(Lang.INVALID_PLAYER)
            );
            return;
        }

        List<Report> reports =
                reportManager.getOpenReports(targetUUID);

        if (reports.isEmpty()) {
            player.sendMessage(
                    langConfig.getString(Lang.REPORT_NOT_FOUND)
            );
            return;
        }

        if (reports.size() > 1) {
            new ReportList(
                    reportManager,
                    langConfig,
                    targetUUID,
                    true
            ).openGUI(player);

            return;
        }

        Report report = reports.getFirst();

        boolean resolved = reportManager.resolveReport(
                targetUUID,
                report.getReportUUID()
        );

        if (!resolved) {
            player.sendMessage(
                    langConfig.getString(Lang.REPORT_NOT_FOUND)
            );
            return;
        }

        player.sendMessage(
                langConfig.getString(
                        Lang.SET_REPORT_AS_RESOLVED
                )
        );
    }
}