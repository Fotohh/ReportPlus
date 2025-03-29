package me.xaxis.reportplus.reports;

import me.xaxis.reportplus.Main;
import me.xaxis.reportplus.enums.ReportState;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

public class Report{

    private final ConfigurationSection section;
    private final Main plugin;

    public UUID getReportUUID() {
        String reportUUID = section.getString("report_uuid");
        return UUID.fromString(reportUUID);
    }

    public Report(Main plugin, @NotNull UUID playerUUID, String playerName, @NotNull UUID reporter, @NotNull String reportType) throws IOException {

        this.plugin = plugin;

        UUID uuid = UUID.randomUUID();

        section = plugin.getReportYML().getFile().createSection(uuid.toString());
        section.set("report_uuid", uuid.toString());
        section.set("player_name", playerName);
        section.set("player_UUID", playerUUID.toString());
        section.set("timestamp", System.currentTimeMillis());
        section.set("reporter_UUID", reporter.toString());
        section.set("report_type", reportType);
        section.set("report_state", ReportState.OPEN.name());

        plugin.getReportYML().save();
        ReportManager.addReport(this, uuid);
    }

    public UUID getPlayerUUID(){
        return UUID.fromString( section.getString("player_UUID") );
    }
    public String getPlayerName(){
        return section.getString("player_name");
    }
    public ReportState getState() {
        return ReportState.valueOf(section.getString("report_state"));
    }
    public void setState(ReportState state) throws IOException {
        section.set("report_state", state.name());
        plugin.getReportYML().save();
    }
    public void resolve() throws IOException {
        setState(ReportState.RESOLVED);
    }
    public String getTargetName(){
        return Bukkit.getPlayer(UUID.fromString( section.getString("reporter_UUID") )).getName();
    }
    public Long getTimestamp(){
        return section.getLong("timestamp");
    }
    public UUID getReporterUUID(){
        return UUID.fromString( section.getString("reporter_UUID") );
    }
    public String getReportType(){
        return section.getString("report_type" );
    }
    public Report(@NotNull Main plugin, @NotNull UUID uuid){
        this.plugin = plugin;
        section = plugin.getReportYML().getFile().getConfigurationSection(uuid.toString());
        ReportManager.addReport(this, uuid);
    }
}
