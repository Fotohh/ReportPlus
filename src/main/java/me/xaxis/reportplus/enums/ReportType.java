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
                return
            }
            case NUKER -> {

            }
            case AIMBOT -> {

            }
            case FLYING -> {

            }
            case KILLAURA -> {

            }
            case VELOCITY -> {

            }
            case AUTOCLICKING -> {

            }
            case default ->{

            }
        }
    }

}
