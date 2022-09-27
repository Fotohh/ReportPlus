package me.xaxis.reportplus;

import me.xaxis.reportplus.reports.ReportYML;
import org.bukkit.plugin.java.JavaPlugin;

public class ReportPlus extends JavaPlugin {

    ReportYML reportYML = new ReportYML(this);

    @Override
    public void onEnable() {



            saveDefaultConfig();
            getReportYML().createFile();
    }

    @Override
    public void onDisable() {



    }

    private void registerReports(){

        for(String value : getReportYML().getFile().getKeys(false)){

        }

    }

    public ReportYML getReportYML() {
        return reportYML;
    }

}
