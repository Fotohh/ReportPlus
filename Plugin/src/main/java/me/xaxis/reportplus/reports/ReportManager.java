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

    private final Main plugin;

    public ReportManager(Main plugin){
        this.plugin = plugin;
    }

    public static Report getReport(UUID uuid){
        return reportUUIDMap.get(uuid);
    }

    public static void addReport(Report report, UUID uuid){
        reportUUIDMap.put(uuid, report);
    }

    public static boolean contains(UUID playerUUID){
        return reportUUIDMap.containsKey(playerUUID);
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

    public static @NotNull ArrayList<Report> getReports(){

        ArrayList<Report> r = new ArrayList<>();

        for(UUID key : reportUUIDMap.keySet()){
            r.add(reportUUIDMap.get(key));
        }

        return r;
    }

}
