package me.xaxis.reportplus;

import lombok.Getter;
import me.xaxis.reportplus.managers.ReportYML;
import org.bukkit.plugin.java.JavaPlugin;

public class ReportPlus extends JavaPlugin {

    @Getter
    ReportYML reportYML = new ReportYML(this);

    @Override
    public void onEnable() {


    }

    @Override
    public void onDisable() {



    }
}
