package me.xaxis.reportplus.enums;

import me.xaxis.reportplus.ReportPlus;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public enum ReportType {

    FLYING("REPORT_TYPE.FLYING.MATERIAL"),
    BHOP("REPORT_TYPE.BHOP.MATERIAL"),
    AUTOCLICKING("REPORT_TYPE.AUTOCLICKING.MATERIAL"),
    VELOCITY("REPORT_TYPE.VELOCITY.MATERIAL"),
    AIMBOT("REPORT_TYPE.AIMBOT.MATERIAL"),
    KILLAURA("REPORT_TYPE.KILLAURA.MATERIAL"),
    NUKER("REPORT_TYPE.NUKER.MATERIAL");

    private final String configKey;
    private static final Map<String, ReportType> lookup = new HashMap<>();

    static {
        for (ReportType type : ReportType.values()) {
            lookup.put(type.getConfigKey(), type);
        }
    }

    ReportType(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigKey() {
        return configKey;
    }

    public Material getMaterial(ReportPlus reportPlus) {
        String materialName = reportPlus.getConfig().getString(configKey);
        return materialName != null ? Material.valueOf(materialName) : null;
    }

    public static ReportType fromConfigKey(String configKey) {
        return lookup.get(configKey);
    }

}
