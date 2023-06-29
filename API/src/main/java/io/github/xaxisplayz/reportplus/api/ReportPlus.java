package io.github.xaxisplayz.reportplus.api;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class ReportPlus implements Listener {

    private final Plugin plugin;

    public ReportPlus(JavaPlugin plugin){
        this.plugin = plugin;
    }
}
