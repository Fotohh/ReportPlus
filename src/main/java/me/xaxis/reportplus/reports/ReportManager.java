package me.xaxis.reportplus.reports;

import me.xaxis.reportplus.ReportPlus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class ReportManager{

    private static final HashMap<UUID, Report> reports = new HashMap<>();

    private final ReportPlus plugin;

    public ReportManager(ReportPlus plugin){
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

    public static void deleteReport(UUID uuid){
        reports.remove(uuid);
    }

    public static @NotNull ArrayList<Report> getReports(){

        ArrayList<Report> r = new ArrayList<>();

        for(UUID key : reports.keySet()){
            r.add(reports.get(key));
        }

        return r;
    }

}
