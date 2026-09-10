package me.xaxis.reportplus.reports;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;
import java.util.logging.Logger;

public class ReportTypeManager {

    private static final String MATERIAL = "material";
    private static final String DISPLAY_NAME = "display-name";
    private static final String LORE = "lore";
    private static final String SLOT = "slot";

    private final FileConfiguration config;
    private final Logger logger;
    private final Map<String, ReportType> reportTypes = new HashMap<>();

    public ReportTypeManager(FileConfiguration config, Logger logger) {
        this.config = config;
        this.logger = logger;
    }

    public boolean init() {
        if(!config.isConfigurationSection("report-types")){
            logger.severe("Failed to find report-types in config.yml.");
            return false;
        }
        return true;
    }

    public List<ReportType> getReportTypes() {
        return reportTypes.values().stream().toList();
    }

    public ReportType getReportType(String type) {
        return reportTypes.get(type);
    }

    public boolean indexReportTypes() {
        Set<Integer> slots = new HashSet<>();
        ConfigurationSection section = config.getConfigurationSection("report-types");
        for(String keys : section.getKeys(false)) {
            if(!section.isConfigurationSection(keys)) {
                logger.warning("Found malformed entry: '" + keys + "' in report-types in config.yml. Please remove or fix this. Skipping the entry.");
                continue;
            }
            ConfigurationSection reportType = section.getConfigurationSection(keys);
            if(reportType == null || reportType.getKeys(false).isEmpty()) {
                logger.warning("Section '" + keys + "' has no entries in report-types in config.yml. Please remove or fix this. Skipping the entry.");
                continue;
            }
            if(!reportType.isString(MATERIAL)) {
                logger.warning("Entry type '" + MATERIAL + "' in "+ keys +" in report-types in config.yml is either not set or is not a string. Please fix this. Skipping the entry.");
                continue;
            }
            String materialString = reportType.getString(MATERIAL);
            Material material = Material.getMaterial(materialString);
            if(material == null) {
                logger.warning("Material '" + materialString + "' is not a valid material type in " + keys + " in report-types in config.yml. Please fix this. Skipping the entry.");
                continue;
            }

            if(!reportType.isString(DISPLAY_NAME)) {
                logger.warning("Entry type '" + DISPLAY_NAME + "' in "+ keys +" in report-types in config.yml is either not set or is not a string. Please fix this. Skipping the entry.");
                continue;
            }
            String displayName = reportType.getString(DISPLAY_NAME);

            if(!reportType.isSet(LORE) || !reportType.isList(LORE)) {
                logger.warning("Entry type '" + LORE + "' in "+ keys +" in report-types in config.yml is either not set or is not a valid string list. Please fix this. Skipping the entry.");
                continue;
            }

            List<String> lore = reportType.getStringList(LORE);

            if(!reportType.isInt(SLOT)) {
                logger.warning("Entry type'" + SLOT + "' in " + keys + " in report-types in config.yml is either not set or is not a valid int. Please fix this. Skipping the entry.");
                continue;
            }

            int slot = reportType.getInt(SLOT);

            if(slots.contains(slot)) {
                logger.warning("Entry type'" + SLOT + "' in " + keys + " contains duplicated slot '" + slot + "' Please fix this. Skipping the entry.");
                continue;
            }

            if(slot >= 17 || slot < 0) {
                logger.warning("Incorrect slot bounds for " + keys + ". Expected bounds 0 <= x < 17, instead got " + slot + ". Please fix this. Skipping the entry.");
                continue;
            }

            slots.add(slot);

            reportTypes.put(reportType.getName(), new ReportType(reportType.getName(), material, displayName, lore, slot));
        }

        return !reportTypes.isEmpty();
    }
}
