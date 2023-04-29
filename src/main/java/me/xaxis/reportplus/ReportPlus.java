package me.xaxis.reportplus;

import me.xaxis.reportplus.commands.ReportCommand;
import me.xaxis.reportplus.commands.Reports;
import me.xaxis.reportplus.file.LangConfig;
import me.xaxis.reportplus.listener.OnClick;
import me.xaxis.reportplus.reports.Report;
import me.xaxis.reportplus.reports.ReportYML;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.UUID;

public class ReportPlus extends JavaPlugin {

    ReportYML reportYML = new ReportYML(this);
    LangConfig langConfig;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        langConfig = new LangConfig(this);
        new OnClick(this);
        new ReportCommand(this);
        new Reports(this);
        try {
            getReportYML().createFile();
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
        registerReports();
    }

    public LangConfig getLangConfig() {
        return langConfig;
    }

    @Override
    public void onDisable() {



    }

    private void registerReports(){
        for(String value : getReportYML().getFile().getKeys(false)){
            new Report(this, UUID.fromString(value));
        }
    }

    public ReportYML getReportYML() {
        return reportYML;
    }

}
