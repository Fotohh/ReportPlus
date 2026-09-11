package me.xaxis.reportplus;

import me.xaxis.reportplus.commands.ReportCommand;
import me.xaxis.reportplus.commands.Reports;
import me.xaxis.reportplus.commands.ReportsTabCompleter;
import me.xaxis.reportplus.file.LangConfig;
import me.xaxis.reportplus.listener.EventsListener;
import me.xaxis.reportplus.player.PlayerDataManager;
import me.xaxis.reportplus.player.PlayerDataYML;
import me.xaxis.reportplus.reports.*;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public final class Main extends JavaPlugin {

    private static final int CONFIG_VERSION = 2;

    private ReportYML reportYML;
    private ReportManager reportManager;
    private ReportTypeManager reportTypeManager;
    private PlayerDataManager playerDataManager;
    private PlayerDataYML playerDataYML;
    private LangConfig langConfig;
    private Metrics metrics;
    private ReportService reportService;

    public LangConfig getLangConfig() {
        return langConfig;
    }

    public ReportTypeManager getReportTypeManager() {
        return reportTypeManager;
    }

    public ReportManager getReportManager() {
        return reportManager;
    }

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

        LangConfig initLangConfig = new LangConfig(
                getDataFolder().toPath(),
                getLogger()
        );

        try {
            initLangConfig.init();
        } catch (IOException | InvalidConfigurationException e) {
            getLogger().log(
                    Level.SEVERE,
                    "Failed to initialize Lang.yml!",
                    e
            );

            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        langConfig = initLangConfig;

        ReportTypeManager initReportTypeManager = new ReportTypeManager(getConfig(), getLogger());

        if(!initReportTypeManager.init()){
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        if(!initReportTypeManager.indexReportTypes()) {
            getLogger().severe("No report types were indexed. Please make sure you add at least one report type then restart the server.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        ReportYML initReportYML = new ReportYML(getDataFolder().toPath(), getLogger());
        try {
            initReportYML.load();
        } catch (IOException | InvalidConfigurationException e) {
            getLogger().log(Level.SEVERE, "Failed to initialize Reports.yml!", e);
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        PlayerDataYML initPlayerDataYML = new PlayerDataYML(getDataFolder().toPath(), getLogger());
        try {
            initPlayerDataYML.load();
        } catch (IOException | InvalidConfigurationException e) {
            getLogger().log(Level.SEVERE, "Failed to initialize player_data.yml", e);
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        playerDataYML = initPlayerDataYML;

        reportTypeManager = initReportTypeManager;
        reportYML = initReportYML;
        reportManager = new ReportManager(reportYML);
        List<Report> reports  = reportYML.loadReports();
        playerDataManager = new PlayerDataManager(playerDataYML, getLogger());
        playerDataManager.migrateFromReports(reports);
        reportManager.indexReports(reports);
        for (var player : Bukkit.getOnlinePlayers()) {
            UUID uuid = player.getUniqueId();
            String name = player.getName();

            if (!playerDataManager.hasPlayerData(uuid)) {
                playerDataManager.addPlayer(uuid, name);
            } else {
                playerDataManager.indexPlayer(uuid, name);
            }
        }
        metrics = new Metrics(this, 20599);
        ReportAlertService reportAlertService =
                new ReportAlertService(
                        playerDataManager,
                        langConfig
                );
        long reportCooldownMillis =
                getConfig().getLong(
                        "report-cooldown-seconds",
                        30
                ) * 1000L;
        reportService = new ReportService(reportManager, reportTypeManager, playerDataManager, reportAlertService, reportCooldownMillis);
        getServer().getPluginManager().registerEvents(new EventsListener(playerDataManager), this);
        getCommand("report").setExecutor(new ReportCommand(reportTypeManager, reportService, langConfig));
        getCommand("reports").setExecutor(new Reports(reportManager, playerDataManager, langConfig));
        getCommand("reports").setTabCompleter(
                new ReportsTabCompleter(playerDataManager)
        );
    }

    @Override
    public void onDisable() {

        if(reportYML != null) {
            try {
                reportYML.save();
            } catch (IOException e) {
                getLogger().log(Level.SEVERE, "Failed to save Reports.yml", e);
            }
        }
        if(metrics != null) {
            metrics.shutdown();
        }

        if(playerDataYML != null) {
            try {
                playerDataYML.save();
            } catch (IOException e) {
                getLogger().log(Level.SEVERE, "Failed to save player_data.yml", e);
            }
        }

        if (langConfig != null) {
            try {
                langConfig.save();
            } catch (IOException e) {
                getLogger().log(
                        Level.SEVERE,
                        "Failed to save Lang.yml!",
                        e
                );
            }
        }

    }

}
