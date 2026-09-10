package me.xaxis.reportplus.reports;

import java.util.UUID;

public class ReportService {

    private final ReportManager reportManager;

    public ReportService(ReportManager reportManager) {
        this.reportManager = reportManager;
    }

    public Report createReport(
            UUID targetUUID,
            String targetName,
            UUID reporterUUID,
            String reporterName,
            ReportType reportType
    ) {

        Report report = new Report(targetUUID, targetName, reporterUUID, reporterName, reportType.id());
        reportManager.addReport(report);

        return report;
    }

}
