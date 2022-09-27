package me.xaxis.reportplus.reports;

import me.xaxis.reportplus.ReportPlus;
import me.xaxis.reportplus.enums.ReportType;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class Report{

    private final ReportPlus plugin;

    private final ConfigurationSection section;

    public Report(@NotNull ReportPlus plugin, @NotNull UUID playerUUID, @NotNull UUID reporter, @NotNull ReportType reportType) {

        this.plugin = plugin;

        section = plugin.getReportYML().getFile().createSection(playerUUID.toString());

        section.set("player_UUID", playerUUID.toString());
        section.set("timestamp", System.currentTimeMillis());
        section.set("reporter_UUID", reporter.toString());
        section.set("report_type", reportType.toString());

        ReportManager.addReport(this, playerUUID);

    }

    public UUID getPlayerUUID(){
        return UUID.fromString( section.getString("player_UUID") );
    }
    public Long getTimestamp(){
        return section.getLong("timestamp");
    }
    public UUID getReporterUUID(){
        return UUID.fromString( section.getString("reporter_UUID") );
    }
    public ReportType getReportType(){
        return ReportType.valueOf( section.getString("report_type") );
    }

    public Report(@NotNull ReportPlus plugin, @NotNull UUID uuid){

        this.plugin = plugin;

        section = plugin.getReportYML().getFile().getConfigurationSection(uuid.toString());

        ReportManager.addReport(this, uuid);

    }



}
