package io.github.xaxisplayz.reportplus.api;

import me.xaxis.reportplus.Main;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class ReportPlus {

    private final Plugin plugin;

    /**
     *Instantiation of API
     * @param plugin The current plugin accessing the API
     */
    public ReportPlus(JavaPlugin plugin){
        this.plugin = plugin;
        if(plugin.getServer().getPluginManager().getPlugin("ReportPlus") == null){
            plugin.getLogger().severe("ReportPlus was not found! Please add ReportPlus to use "+plugin.getName());
        }
    }

    public Main getPlugin(){
        return (Main) plugin.getServer().getPluginManager().getPlugin("ReportPlus");
    }


}
