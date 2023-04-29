package me.xaxis.reportplus.enums;

import me.xaxis.reportplus.Main;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public enum ReportType {

    FLYING("REPORT_TYPE.FLYING.MATERIAL", "REPORT_TYPE.FLYING.MATERIAL.DISPLAY_NAME"),
    BHOP("REPORT_TYPE.BHOP.MATERIAL", "REPORT_TYPE.BHOP.MATERIAL.DISPLAY_NAME"),
    AUTOCLICKING("REPORT_TYPE.AUTOCLICKING.MATERIAL", "REPORT_TYPE.AUTOCLICKING.MATERIAL.DISPLAY_NAME"),
    VELOCITY("REPORT_TYPE.VELOCITY.MATERIAL","REPORT_TYPE.VELOCITY.MATERIAL.DISPLAY_NAME"),
    AIMBOT("REPORT_TYPE.AIMBOT.MATERIAL","REPORT_TYPE.AIMBOT.MATERIAL.DISPLAY_NAME"),
    KILLAURA("REPORT_TYPE.KILLAURA.MATERIAL","REPORT_TYPE.KILLAURA.MATERIAL.DISPLAY_NAME"),
    NUKER("REPORT_TYPE.NUKER.MATERIAL","REPORT_TYPE.NUKER.MATERIAL.DISPLAY_NAME");

    private final String material;
    private final String displayName;
    private static final Map<String, ReportType> lookup = new HashMap<>();

    static {
        for (ReportType type : ReportType.values()) {
            lookup.put(type.getMaterialPath(), type);
        }
    }

    ReportType(String material, String displayName) {
        this.material = material;
        this.displayName = displayName;
    }

    public String getMaterialPath() {
        return material;
    }
    public String getDisplayNamePath() {
        return displayName;
    }

    public Material getMaterial(Main reportPlus) {
        String materialName = reportPlus.getConfig().getString(getMaterialPath());
        return materialName != null ? Material.valueOf(materialName) : null;
    }
    public String getDisplayName(Main plugin){
        String displayName = plugin.getConfig().getString(getDisplayNamePath());
        return displayName;
    }

    public static ReportType fromConfigKey(String configKey) {
        return lookup.get(configKey);
    }

}
