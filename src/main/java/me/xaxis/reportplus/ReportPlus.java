package me.xaxis.reportplus;

import me.xaxis.reportplus.reports.Report;
import me.xaxis.reportplus.reports.ReportYML;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class ReportPlus extends JavaPlugin {

    ReportYML reportYML = new ReportYML(this);

    @Override
    public void onEnable() {



            saveDefaultConfig();
            getReportYML().createFile();
            registerReports();
    }

    @Override
    public void onDisable() {



    }

    private void registerReports(){
        for(String value : getReportYML().getFile().getKeys(false)){
            Report report = new Report(this, UUID.fromString(value));
        }
    }

    public ReportYML getReportYML() {
        return reportYML;
    }

}
