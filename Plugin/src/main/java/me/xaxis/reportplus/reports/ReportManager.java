package me.xaxis.reportplus.reports;

import jline.internal.Nullable;
import me.xaxis.reportplus.Main;
import me.xaxis.reportplus.enums.ReportState;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class ReportManager{

    private static final HashMap<UUID, Report> reportUUIDMap = new HashMap<>();

    public static HashMap<UUID, Report> getReportUUIDMap() {
        return reportUUIDMap;
    }

    public static Report getReport(UUID uuid){
        return reportUUIDMap.get(uuid);
    }

    public static boolean containsReport(UUID playerUUID){
        for(Report report : reportUUIDMap.values()){
            if(report.getPlayerUUID().equals(playerUUID)){
                return true;
            }
        }
        return false;
    }

    @Nullable
    public static Report getUnresolvedReportFromPlayerUUID(UUID playerUUID) {
        Optional<Report> report = reportUUIDMap.values().stream().filter(r -> r.getPlayerUUID().equals(playerUUID) && r.getState() != ReportState.RESOLVED).findFirst();
        return report.orElse(null);
    }

    public static void addReport(Report report, UUID uuid){
        reportUUIDMap.put(uuid, report);
    }

    public static void removeReport(UUID playerUUID, Main plugin) {
        Optional<Report> report =  reportUUIDMap.values().stream().filter(r -> r.getPlayerUUID().equals(playerUUID)).findFirst();
        report.ifPresent(value -> deleteReport(value.getReportUUID(), plugin));
    }

    public static void deleteReport(UUID uuid, Main plugin) {
        reportUUIDMap.remove(uuid);
        try {
            plugin.getReportYML().set(uuid.toString(), null);
            plugin.getReportYML().save();
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete entry",e);
        }
    }
}
