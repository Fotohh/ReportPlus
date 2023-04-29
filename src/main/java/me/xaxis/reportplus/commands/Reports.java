package me.xaxis.reportplus.commands;

import me.xaxis.reportplus.Main;
import me.xaxis.reportplus.enums.Lang;
import me.xaxis.reportplus.enums.Perms;
import me.xaxis.reportplus.gui.ReportList;
import me.xaxis.reportplus.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Reports extends Utils implements CommandExecutor {

    private final Main plugin;

    public Reports(Main plugin) {
        super(plugin);
        this.plugin = plugin;
        plugin.getCommand("reports").setExecutor(this);
    }


    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if(isValid(commandSender,Perms.LIST_REPORTS)){
            Player player = (Player) commandSender;
            new ReportList(plugin.getLangConfig().getString(Lang.REPORT_LIST_GUI_TITLE), plugin).openGUI(player);
        }

        return true;
    }
}
