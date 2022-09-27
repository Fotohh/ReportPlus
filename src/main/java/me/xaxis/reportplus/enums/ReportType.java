package me.xaxis.reportplus.enums;

import me.xaxis.reportplus.ReportPlus;
import org.bukkit.Material;

public enum ReportType {

    FLYING,
    BHOP,
    AUTOCLICKING,
    VELOCITY,
    AIMBOT,
    KILLAURA,
    NUKER;

    public Material m(ReportPlus reportPlus){

        switch (this){
            case BHOP -> {
                return Material.valueOf( reportPlus.getConfig().getString("REPORT_MATERIAL_TYPE.BHOP"));
            }
            case NUKER -> {
                return Material.valueOf( reportPlus.getConfig().getString("REPORT_MATERIAL_TYPE.NUKER"));
            }
            case AIMBOT -> {
                return Material.valueOf( reportPlus.getConfig().getString("REPORT_MATERIAL_TYPE.AIMBOT"));
            }
            case FLYING -> {
                return Material.valueOf( reportPlus.getConfig().getString("REPORT_MATERIAL_TYPE.FLYING"));
            }
            case KILLAURA -> {
                return Material.valueOf( reportPlus.getConfig().getString("REPORT_MATERIAL_TYPE.KILLAURA"));
            }
            case VELOCITY -> {
                return Material.valueOf( reportPlus.getConfig().getString("REPORT_MATERIAL_TYPE.VELOCITY"));
            }
            case AUTOCLICKING -> {
                return Material.valueOf( reportPlus.getConfig().getString("REPORT_MATERIAL_TYPE.AUTOCLICKING"));
            }
            case default ->{
                return null;
            }
        }
    }

}
