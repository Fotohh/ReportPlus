package me.xaxis.reportplus.reports;

import me.xaxis.reportplus.enums.ReportState;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

public class ReportYML {

    private static final int SCHEMA_VERSION = 1;

    private final File file;
    private final YamlConfiguration yml;
    private final Logger logger;

    public ReportYML(File dataFolder, Logger logger){
        file = new File(dataFolder, "Reports.yml");
        yml = new YamlConfiguration();
        this.logger = logger;
    }

    public void load() throws InvalidConfigurationException, IOException {

        if (!file.exists()) {
            if(file.createNewFile()){
                yml.load(file);
                yml.set("config-version", SCHEMA_VERSION);
                save();
                return;
            }
        }

        yml.load(file);
    }

    public boolean schemaValidation() {
        if(yml.isSet("config-version")) {
            if(yml.isInt("config-version")) {
                int version = yml.getInt("config-version");
                return version == SCHEMA_VERSION;
            }
        }
        return false;
    }

    private static final Map<String, String> entries = Map.of(
            "player-name", "string",
            "reporter-name", "string",
            "report-type", "string",
            "timestamp", "long",
            "report-state", "reportState",
            "player-uuid", "uuid",
            "reporter-uuid", "uuid"
    );

    private void logInvalidType(String type, String entry, String section) {
        logger.warning("Found entry: " + entry + " with an invalid type. Expected type: " + type + " but found something else in section: " + section);
    }

    private boolean areEntriesValid(ConfigurationSection section) {
        String sectionName = section.getName();
        for(String entry : entries.keySet()) {
            if(!section.isSet(entry)) {
                logger.warning("Found no "+entry+" entry in report: " + section.getName());
                return false;
            }
            if(section.get(entry) == null) {
                logger.warning("Found no value in entry: "+entry+" in report: " + section.getName());
                return false;
            }
            String entryType = entries.get(entry);
            switch (entryType){
                case "string" -> {
                    if(!section.isString(entry)){
                        logInvalidType(entryType, entry, sectionName);
                        return false;
                    }
                }
                case "long" -> {
                    if(!section.isLong(entry)) {
                        logInvalidType(entryType, entry, sectionName);
                        return false;
                    }
                }
                case "reportState" -> {
                    if(!section.isString(entry)){
                        logInvalidType(entryType, entry, sectionName);
                        return false;
                    }
                    try {
                        ReportState.valueOf(section.getString(entry));
                    } catch (IllegalArgumentException e) {
                        logInvalidType(entryType, entry, sectionName);
                        return false;
                    }
                }
                case "uuid" -> {
                    if(!section.isString(entry)){
                        logInvalidType(entryType, entry, sectionName);
                        return false;
                    }
                    try {
                        UUID.fromString(section.getString(entry));
                    } catch (IllegalArgumentException e) {
                        logInvalidType(entryType, entry, sectionName);
                        return false;
                    }
                }
                default -> { return false; }
            }
        }
        return true;
    }

    public List<Report> loadReports() {
        List<Report> reports = new ArrayList<>();
        for(String reportEntry : yml.getKeys(false)) {
            if(!yml.isConfigurationSection(reportEntry)) continue;
            ConfigurationSection reportSection = yml.getConfigurationSection(reportEntry);
            if(reportSection == null) continue;
            if(!areEntriesValid(reportSection)) continue;
            String playerName = reportSection.getString("player-name");
            String reporterName = reportSection.getString("reporter-name");
            String reportType = reportSection.getString("report-type");
            long timestamp = reportSection.getLong("timestamp");
            ReportState reportState = ReportState.valueOf(reportSection.getString("report-state"));
            UUID playerUUID = UUID.fromString(reportSection.getString("player-uuid"));
            UUID reporterUUID = UUID.fromString(reportSection.getString("reporter-uuid"));
            UUID reportUUID;
            try {
                reportUUID = UUID.fromString(reportEntry);
            } catch (IllegalArgumentException e) {
                logger.warning("Report UUID: " + reportEntry + " is unable to be parsed as a uuid.");
                continue;
            }

            reports.add(new Report(
                    playerUUID,
                    reportUUID,
                    reporterUUID,
                    timestamp,
                    reportType,
                    reportState,
                    playerName,
                    reporterName
            ));
        }

        return reports;
    }

    public void saveReport(Report report) {
        ConfigurationSection reportSection;
        String reportUUID = report.getReportUUID().toString();
        if(yml.isConfigurationSection(reportUUID)) reportSection = yml.getConfigurationSection(reportUUID);
        else reportSection = yml.createSection(report.getReportUUID().toString());
        reportSection.set("reporter-name", report.getReporterName());
        reportSection.set("player-name", report.getPlayerName());
        reportSection.set("report-type", report.getReportTypeId());
        reportSection.set("player-uuid", report.getPlayerUUID().toString());
        reportSection.set("reporter-uuid", report.getReporterUUID().toString());
        reportSection.set("timestamp", report.getTimestamp());
        reportSection.set("report-state", report.getReportState().name());
    }

    public void deleteReport(UUID id) {
        yml.set(id.toString(), null);
    }

    public void save() throws IOException {
        yml.save(file);
    }

}
