package me.xaxis.reportplus.reports;

import me.xaxis.reportplus.enums.Lang;
import me.xaxis.reportplus.enums.Perms;
import me.xaxis.reportplus.file.LangConfig;
import me.xaxis.reportplus.player.PlayerData;
import me.xaxis.reportplus.player.PlayerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Date;

public final class ReportAlertService {

    private final PlayerDataManager playerDataManager;
    private final LangConfig langConfig;

    public ReportAlertService(
            PlayerDataManager playerDataManager,
            LangConfig langConfig
    ) {
        this.playerDataManager = playerDataManager;
        this.langConfig = langConfig;
    }

    public void sendAlert(Report report) {

        String message = langConfig.getString(
                Lang.REPORT_ALERT,
                report.getTargetName(),
                report.getReporterName(),
                report.getReportTypeId(),
                new Date(report.getTimestamp()).toString()
        );

        for (Player player : Bukkit.getOnlinePlayers()) {

            if (!player.hasPermission(
                    Perms.REPORT_ALERT.getPermission()
            )) {
                continue;
            }

            PlayerData data =
                    playerDataManager.getPlayerData(
                            player.getUniqueId()
                    );

            if (data != null && !data.reportAlertsToggled()) {
                continue;
            }

            player.sendMessage(message);
        }
    }
}