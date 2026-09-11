package me.xaxis.reportplus.reports;

import me.xaxis.reportplus.enums.ReportState;

import java.util.*;

public class ReportManager{

    private final ReportYML reportYML;

    public ReportManager(ReportYML reportYML) {
        this.reportYML = reportYML;
    }

    private final Map<UUID, Map<UUID, Report>> reports = new HashMap<>();

    public void indexReports(List<Report> reports) {
        for(Report report : reports) {
            indexReport(report);
        }
    }

    public List<Report> getReports(UUID playerUUID){
        return reports.getOrDefault(playerUUID, Map.of()).values().stream().toList();
    }

    public List<Report> getOpenReports(UUID playerUUID) {
        List<Report> reports = getReports(playerUUID);
        return reports.stream().filter(report -> report.getReportState() == ReportState.OPEN).toList();
    }

    public List<Report> getAllReports() {
        return reports.values()
                .stream()
                .flatMap(playerReports -> playerReports.values().stream())
                .toList();
    }

    public boolean resolveReport(UUID playerUUID, UUID reportUUID) {
        if(!reports.containsKey(playerUUID)) {
            return false;
        }
        var map = reports.get(playerUUID);
        if(!map.containsKey(reportUUID)) {
            return false;
        }
        Report report = map.get(reportUUID);
        report.resolve();
        reportYML.saveReport(report);
        return true;
    }

    public void addReport(Report report){
        indexReport(report);
        reportYML.saveReport(report);
    }

    private void indexReport(Report report) {
        reports.computeIfAbsent(report.getTargetUUID(),
        _ -> new HashMap<>()).put(report.getReportUUID(), report);
    }

    private boolean removeReport(UUID playerUUID, UUID reportUUID) {
        if(!reports.containsKey(playerUUID)) return false;
        var map = reports.get(playerUUID);
        if (map.remove(reportUUID) == null) return false;
        if(map.isEmpty()) reports.remove(playerUUID);
        return true;
    }

    public boolean deleteReport(UUID playerUUID, UUID reportUUID) {
        if(!removeReport(playerUUID, reportUUID)) {
            return false;
        }
        reportYML.deleteReport(reportUUID);
        return true;
    }
}
