package me.xaxis.reportplus.reports;

import me.xaxis.reportplus.enums.ReportState;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class ReportYML {

    private static final String PLAYER_UUID = "player_UUID";
    private static final String PLAYER_NAME = "player_name";
    private static final String REPORTER_UUID = "reporter_UUID";
    private static final String REPORTER_NAME = "reporter_name";
    private static final String REPORT_TYPE = "report_type";
    private static final String TIMESTAMP = "timestamp";
    private static final String REPORT_STATE = "report_state";

    private final Path file;
    private final Logger logger;
    private final YamlConfiguration yml;

    public ReportYML(Path dataFolder, Logger logger) {
        this.file = dataFolder.resolve("Reports.yml");
        this.logger = logger;
        this.yml = new YamlConfiguration();
    }

    public void load() throws IOException, InvalidConfigurationException {
        if(!Files.exists(file)) Files.createFile(file);
        yml.load(file.toFile());
    }

    public List<Report> loadReports() {

        List<Report> reports = new ArrayList<>();

        for (String id : yml.getKeys(false)) {

            ConfigurationSection section =
                    yml.getConfigurationSection(id);

            if (section == null) {
                logger.warning(
                        "Skipping Reports.yml entry '" + id +
                                "': expected a report section."
                );
                continue;
            }

            try {
                reports.add(parseReport(id, section));
            } catch (MalformedReportException e) {
                logger.warning(
                        "Skipping report '" + id + "': " + e.getMessage()
                );
            }
        }

        if(repaired) {
            try {
                save();
                repaired = false;
            } catch (IOException e) {
                logger.log(Level.SEVERE,"Failed to save Reports.yml", e);
            }
        }

        return List.copyOf(reports);
    }

    private boolean repaired = false;

    private Report parseReport(
            String id,
            ConfigurationSection section
    ) throws MalformedReportException {

        UUID reportUUID = parseUUID(id, "report_UUID");

        UUID playerUUID =
                parseUUID(requiredString(section, PLAYER_UUID), PLAYER_UUID);

        UUID reporterUUID =
                parseUUID(requiredString(section, REPORTER_UUID), REPORTER_UUID);

        String playerName =
                requiredString(section, PLAYER_NAME);
        if(!section.isSet(REPORTER_NAME)) {
            section.set(REPORTER_NAME, "Unknown");
            repaired = true;
        }
        String reporterName =
                requiredString(section, REPORTER_NAME);


        String reportType =
                requiredString(section, REPORT_TYPE);

        long timestamp =
                requiredLong(section, TIMESTAMP);

        ReportState state =
                parseState(requiredString(section, REPORT_STATE));

        return new Report(
                playerUUID,
                reportUUID,
                reporterUUID,
                timestamp,
                reportType,
                state,
                playerName,
                reporterName
        );
    }

    private String requiredString(
            ConfigurationSection section,
            String key
    ) throws MalformedReportException {

        if (!section.isString(key)) {
            throw new MalformedReportException(
                    "'" + key + "' must be a string"
            );
        }

        String value = section.getString(key);

        if (value == null || value.isBlank()) {
            throw new MalformedReportException(
                    "'" + key + "' cannot be empty"
            );
        }

        return value;
    }

    private long requiredLong(
            ConfigurationSection section,
            String key
    ) throws MalformedReportException {

        if (!section.isLong(key)) {
            throw new MalformedReportException(
                    "'" + key + "' must be a long"
            );
        }

        return section.getLong(key);
    }

    private UUID parseUUID(
            String value,
            String field
    ) throws MalformedReportException {

        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            throw new MalformedReportException(
                    "'" + field + "' is not a valid UUID"
            );
        }
    }

    private ReportState parseState(
            String value
    ) throws MalformedReportException {

        try {
            return ReportState.valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new MalformedReportException(
                    "Unknown report state '" + value + "'"
            );
        }
    }

    public void saveReport(Report report) {

        String id = report.getReportUUID().toString();

        ConfigurationSection section =
                yml.getConfigurationSection(id);

        if (section == null) {
            yml.set(id, null);
            section = yml.createSection(id);
        }

        section.set(PLAYER_UUID, report.getPlayerUUID().toString());
        section.set(PLAYER_NAME, report.getPlayerName());

        section.set(REPORTER_UUID, report.getReporterUUID().toString());
        section.set(REPORTER_NAME, report.getReporterName());

        section.set(REPORT_TYPE, report.getReportTypeId());
        section.set(TIMESTAMP, report.getTimestamp());
        section.set(REPORT_STATE, report.getReportState().name());
    }

    public void deleteReport(UUID reportId) {
        yml.set(reportId.toString(), null);
    }

    public void save() throws IOException {
        yml.save(file.toFile());
    }

    private static final class MalformedReportException extends Exception {

        private MalformedReportException(String message) {
            super(message);
        }
    }
}