package me.xaxis.reportplus.reports;

import me.xaxis.reportplus.enums.ReportState;

import java.util.UUID;

public class Report {

    private final UUID playerUUID;
    private final UUID reporterUUID;
    private final String reportTypeId;
    private final long timestamp;
    private ReportState reportState;
    private final UUID reportUUID;
    private final String playerName;
    private final String reporterName;

    public Report(UUID playerUUID, String playerName, UUID reporterUUID, String reporterName, String reportTypeId) {
        this(
            playerUUID,
            UUID.randomUUID(),
            reporterUUID,
            System.currentTimeMillis(),
            reportTypeId,
            ReportState.OPEN,
            playerName,
            reporterName
        );
    }

    public Report(
            UUID playerUUID,
            UUID reportUUID,
            UUID reporterUUID,
            long timestamp,
            String reportTypeId,
            ReportState reportState,
            String playerName,
            String reporterName)
    {
        this.reportState = reportState;
        this.timestamp = timestamp;
        this.reportTypeId = reportTypeId;
        this.reportUUID = reportUUID;
        this.playerUUID = playerUUID;
        this.reporterUUID = reporterUUID;
        this.playerName = playerName;
        this.reporterName = reporterName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getReporterName() {
        return reporterName;
    }

    public void resolve() {
        reportState = ReportState.RESOLVED;
    }

    public UUID getReportUUID() {
        return reportUUID;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public ReportState getReportState() {
        return reportState;
    }

    public String getReportTypeId() {
        return reportTypeId;
    }

    public UUID getReporterUUID() {
        return reporterUUID;
    }
}
