package me.xaxis.reportplus.reports;

import me.xaxis.reportplus.enums.ReportState;

import java.util.UUID;

public class Report {

    private final UUID targetUUID;
    private final UUID reporterUUID;
    private final String reportTypeId;
    private final long timestamp;
    private ReportState reportState;
    private final UUID reportUUID;
    private final String targetName;
    private final String reporterName;

    public Report(UUID targetUUID, String targetName, UUID reporterUUID, String reporterName, String reportTypeId) {
        this(
            targetUUID,
            UUID.randomUUID(),
            reporterUUID,
            System.currentTimeMillis(),
            reportTypeId,
            ReportState.OPEN,
            targetName,
            reporterName
        );
    }

    public Report(
            UUID targetUUID,
            UUID reportUUID,
            UUID reporterUUID,
            long timestamp,
            String reportTypeId,
            ReportState reportState,
            String targetName,
            String reporterName)
    {
        this.reportState = reportState;
        this.timestamp = timestamp;
        this.reportTypeId = reportTypeId;
        this.reportUUID = reportUUID;
        this.targetUUID = targetUUID;
        this.reporterUUID = reporterUUID;
        this.targetName = targetName;
        this.reporterName = reporterName;
    }

    public String getTargetName() {
        return targetName;
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

    public UUID getTargetUUID() {
        return targetUUID;
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
