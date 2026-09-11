package me.xaxis.reportplus.reports;

import me.xaxis.reportplus.player.PlayerData;
import me.xaxis.reportplus.player.PlayerDataManager;

import java.util.UUID;

public final class ReportService {

    private final ReportManager reportManager;
    private final ReportTypeManager reportTypeManager;
    private final PlayerDataManager playerDataManager;
    private final ReportAlertService reportAlertService;

    private final long reportCooldownMillis;

    public ReportService(
            ReportManager reportManager,
            ReportTypeManager reportTypeManager,
            PlayerDataManager playerDataManager,
            ReportAlertService reportAlertService,
            long reportCooldownMillis
    ) {
        this.reportManager = reportManager;
        this.reportTypeManager = reportTypeManager;
        this.playerDataManager = playerDataManager;
        this.reportAlertService = reportAlertService;
        this.reportCooldownMillis = reportCooldownMillis;
    }

    public CreateResult createReport(
            UUID targetUUID,
            String targetName,
            UUID reporterUUID,
            String reporterName,
            String reportTypeId
    ) {

        if (targetUUID.equals(reporterUUID)) {
            return new CreateResult(
                    CreateStatus.SELF_REPORT,
                    null,
                    0
            );
        }

        ReportType reportType = reportTypeManager.getReportType(reportTypeId);

        if (reportType == null) {
            return new CreateResult(
                    CreateStatus.INVALID_REPORT_TYPE,
                    null,
                    0
            );
        }

        PlayerData reporterData =
                playerDataManager.getPlayerData(reporterUUID);

        if (reporterData == null) {
            return new CreateResult(
                    CreateStatus.PLAYER_DATA_MISSING,
                    null,
                    0
            );
        }

        long now = System.currentTimeMillis();

        if (reporterData.reportCooldownUntil() > now) {
            long remainingMillis =
                    reporterData.reportCooldownUntil() - now;

            long remainingSeconds =
                    (long) Math.ceil(remainingMillis / 1000.0);

            return new CreateResult(
                    CreateStatus.COOLDOWN,
                    null,
                    remainingSeconds
            );
        }

        Report report = new Report(
                targetUUID,
                targetName,
                reporterUUID,
                reporterName,
                reportTypeId
        );

        reportManager.addReport(report);

        playerDataManager.setReportCooldownUntil(
                reporterUUID,
                now + reportCooldownMillis
        );

        reportAlertService.sendAlert(report);

        return new CreateResult(
                CreateStatus.SUCCESS,
                report,
                0
        );
    }

    public enum CreateStatus {
        SUCCESS,
        SELF_REPORT,
        INVALID_REPORT_TYPE,
        PLAYER_DATA_MISSING,
        COOLDOWN
    }

    public record CreateResult(
            CreateStatus status,
            Report report,
            long cooldownSeconds
    ) {
    }
}