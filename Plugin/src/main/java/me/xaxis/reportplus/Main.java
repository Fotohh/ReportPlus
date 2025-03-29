package me.xaxis.reportplus;

import me.xaxis.reportplus.commands.ReportCommand;
import me.xaxis.reportplus.commands.Reports;
import me.xaxis.reportplus.file.LangConfig;
import me.xaxis.reportplus.listeners.OnInventoryClick;
import me.xaxis.reportplus.reports.Report;
import me.xaxis.reportplus.reports.ReportYML;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.UUID;

public final class Main extends JavaPlugin {

    private final ReportYML reportYML = new ReportYML(this);
    private final LangConfig langConfig = new LangConfig(this);
    private final Metrics metrics = new Metrics(this, 20599);

    @Override
    public void onEnable() {
        
        saveDefaultConfig();
        getDataFolder().mkdirs();
        new ReportCommand(this);
        new Reports(this);
        new OnInventoryClick(this);
        try {
            reportYML.createFile();
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

        try {
            getReportYML().save();
        } catch (IOException e) {
            throw new RuntimeException("Failed to save Reports.yml",e);
        }

        metrics.shutdown();

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
