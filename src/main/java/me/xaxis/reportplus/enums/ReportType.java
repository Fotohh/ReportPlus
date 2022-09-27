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
                return Material.valueOf( reportPlus.getConfig().getString("REPORT_TYPE.BHOP.MATERIAL"));
            }
            case NUKER -> {
                return Material.valueOf( reportPlus.getConfig().getString("REPORT_TYPE.NUKER.MATERIAL"));
            }
            case AIMBOT -> {
                return Material.valueOf( reportPlus.getConfig().getString("REPORT_TYPE.AIMBOT.MATERIAL"));
            }
            case FLYING -> {
                return Material.valueOf( reportPlus.getConfig().getString("REPORT_TYPE.FLYING.MATERIAL"));
            }
            case KILLAURA -> {
                return Material.valueOf( reportPlus.getConfig().getString("REPORT_TYPE.KILLAURA.MATERIAL"));
            }
            case VELOCITY -> {
                return Material.valueOf( reportPlus.getConfig().getString("REPORT_TYPE.VELOCITY.MATERIAL"));
            }
            case AUTOCLICKING -> {
                return Material.valueOf( reportPlus.getConfig().getString("REPORT_TYPE.AUTOCLICKING.MATERIAL"));
            }
            case default ->{
                return null;
            }
        }
    }

}
