package me.xaxis.reportplus.commands;

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
import java.util.UUID;

public class Reports implements CommandExecutor {

    private final ReportManager reportManager;
    private final PlayerDataManager playerDataManager;
    private final LangConfig langConfig;

    public Reports(ReportManager reportManager, PlayerDataManager playerDataManager, LangConfig langConfig) {
        this.reportManager = reportManager;
        this.playerDataManager = playerDataManager;
        this.langConfig = langConfig;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (!(commandSender instanceof Player player)) {
            //todo message
            return true;
        }
        if (strings.length == 0) {
            if (!player.hasPermission(Perms.LIST_REPORTS.getPermission())) {
                //todo message no perms
                return true;
            }
            new ReportList(
                    reportManager,
                    langConfig
            ).openGUI(player);
            return true;
        }
        if (strings.length == 1) {
            if (strings[0].equalsIgnoreCase("toggle")) {
                if (!player.hasPermission(Perms.REPORT_ALERT.getPermission())) {
                    //todo message no perms
                    return true;
                }

                //todo functionality

                return true;

            }
            return true;
        }
        if (strings.length == 2) {
            if (strings[0].equalsIgnoreCase("delete")) {
                if (!player.hasPermission(Perms.LIST_REPORTS.getPermission())) {
                    //todo message no perms
                    return true;
                }
                String targetName = strings[1];
                UUID targetUUID = playerDataManager.getPlayerUUID(targetName);
                if (targetUUID == null) {
                    //todo message invalid player or no reports
                    return true;
                }

                List<Report> targetReports = reportManager.getReports(targetUUID);

                if (targetReports.isEmpty()) {
                    //todo message no reports
                    return true;
                }

                if(targetReports.size() == 1) {
                    reportManager.deleteReport(targetReports.getFirst().getReportUUID(), targetUUID);
                    //todo message report deleted
                    return true;
                }

                //todo handle multiple reports, probably with a gui
                return true;
            }

            if (strings[0].equalsIgnoreCase("resolve")) {

                if (!player.hasPermission(Perms.LIST_REPORTS.getPermission())) {
                    //todo message no perms
                    return true;
                }

                String targetName = strings[1];

                UUID targetUUID = playerDataManager.getPlayerUUID(targetName);

                if (targetUUID == null) {
                    //todo message invalid player or no reports
                    return true;
                }

                List<Report> reports = reportManager.getOpenReports(targetUUID);

                if(reports.isEmpty()) {
                    //todo message no reports
                    return true;
                }

                if(reports.size() == 1) {
                    reportManager.resolveReport(targetUUID, reports.getFirst().getReportUUID());
                    //todo message resolved report
                    return true;
                }

                //todo handle multiple reports, probably with a gui
                return true;
            }
        }

        return true;
    }
}
