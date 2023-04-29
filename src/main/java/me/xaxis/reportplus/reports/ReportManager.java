package me.xaxis.reportplus.reports;

import me.xaxis.reportplus.Main;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class ReportManager{

    private static final HashMap<UUID, Report> reports = new HashMap<>();

    private final Main plugin;

    public ReportManager(Main plugin){
        this.plugin = plugin;
    }

    public static Report getReport(UUID playerUUID){
        return reports.get(playerUUID);
    }

    public static void addReport(Report report, UUID uuid){
        reports.put(uuid, report);
    }

    public static boolean contains(UUID playerUUID){
        return reports.containsKey(playerUUID);
    }

    public static void deleteReport(UUID uuid, Main plugin) {
        reports.remove(uuid);
        try {
            plugin.getReportYML().set(uuid.toString(), null);
            plugin.getReportYML().save();
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete entry",e);
        }
    }

    public static @NotNull ArrayList<Report> getReports(){

        ArrayList<Report> r = new ArrayList<>();

        for(UUID key : reports.keySet()){
            r.add(reports.get(key));
        }

        return r;
    }

}
