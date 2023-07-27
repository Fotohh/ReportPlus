package me.xaxis.reportplus.reports;

import me.xaxis.reportplus.Main;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class ReportManager{

    private static final HashMap<UUID, Report> reportUUIDMap = new HashMap<>();

    public static HashMap<UUID, Report> getReportUUIDMap() {
        return reportUUIDMap;
    }

    public static Report getReport(UUID uuid){
        return reportUUIDMap.get(uuid);
    }

    public static void addReport(Report report, UUID uuid){
        reportUUIDMap.put(uuid, report);
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
