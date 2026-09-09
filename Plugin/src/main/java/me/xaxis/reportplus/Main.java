package me.xaxis.reportplus;

import me.xaxis.reportplus.commands.ReportCommand;
import me.xaxis.reportplus.commands.Reports;
import me.xaxis.reportplus.commands.ReportsTabCompleter;
import me.xaxis.reportplus.file.LangConfig;
import me.xaxis.reportplus.listeners.OnInventoryClick;
import me.xaxis.reportplus.reports.Report;
import me.xaxis.reportplus.reports.ReportYML;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;

public final class Main extends JavaPlugin {

    private static final int CONFIG_VERSION = 2;

    private ReportYML reportYML;
    private LangConfig langConfig;
    private Metrics metrics;

    @Override
    public void onEnable() {

        if(!getDataFolder().exists()) {
            if(!getDataFolder().mkdirs()){
                getLogger().severe("Encountered an unexpected issue creating plugin directory: " + getDataFolder().getAbsolutePath());
                Bukkit.getPluginManager().disablePlugin(this);
                return;
            }
        }
        saveDefaultConfig();
        if(!getConfig().isSet("config-version")){
            getLogger().severe("config.yml is missing config-version. Please regenerate the config file either by deleting the current one, or rename your current config.yml, regenerate the config, then copy the contents over.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        if(!getConfig().isInt("config-version")) {
            getLogger().severe("config-version isn't set correctly. Please regenerate the config file either by deleting the current one, or rename your current config.yml, regenerate the config, then copy the contents over.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        int configVersion = getConfig().getInt("config-version");
        if(configVersion < CONFIG_VERSION) {
            getLogger().severe("config.yml is out-of-date. Please regenerate the config file either by deleting the current one, or rename your current config.yml, regenerate the config, then copy the contents over.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }else if(configVersion > CONFIG_VERSION){
            getLogger().severe("config.yml is of newer version yet the plugin only supports the configuration version " + CONFIG_VERSION + ". Please regenerate the config file either by deleting the current one, or rename your current config.yml, regenerate the config, then copy the contents over.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        ReportYML initReportYML = new ReportYML(getDataFolder(), getLogger());
        try {
            initReportYML.load();

        } catch (IOException | InvalidConfigurationException e) {
            getLogger().log(Level.SEVERE, "Failed to create reports.yml!", e);
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        if(!initReportYML.schemaValidation()){
            getLogger().severe(
                    "reports.yml config-version is greater than current version! " +
                    "To avoid destroying any possible data the plugin will not load. " +
                    "To proceed, please rename your current reports.yml to something else. " +
                    "Then reload the server to regenerate the reports.yml " +
                    "This will allow you to preserve your current report data. "
            );
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        reportYML = initReportYML;
        langConfig = new LangConfig(this);
        registerReports();
        metrics = new Metrics(this, 20599);
        new ReportCommand(this);
        new Reports(this);
        new OnInventoryClick(this);
        getCommand("reports").setTabCompleter(new ReportsTabCompleter());
    }

    public LangConfig getLangConfig() {
        return langConfig;
    }

    @Override
    public void onDisable() {

        if(getReportYML() != null) {
            try {
                getReportYML().save();
            } catch (IOException e) {
                getLogger().log(Level.SEVERE, "Failed to save reports.yml", e);
            }
        }
        if(metrics != null) {
            metrics.shutdown();
        }

    }

    private void registerReports(){
        for(String value : reportYML.getFile().getKeys(false)){
            UUID uuid;
            try{
                uuid = UUID.fromString(value);
            } catch (IllegalArgumentException exception) {
                getLogger().warning("Found corrupt report entry: " + value + " | Please remove or delete this entry. For now the plugin will skip it.");
                continue;
            }
            new Report(this, uuid);
        }
    }

    public ReportYML getReportYML() {
        return reportYML;
    }

}
