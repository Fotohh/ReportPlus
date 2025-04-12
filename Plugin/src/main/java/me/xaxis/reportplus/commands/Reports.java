package me.xaxis.reportplus.commands;

import me.xaxis.reportplus.Main;
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

        if (isValid(commandSender, Perms.LIST_REPORTS)) {
            if(strings.length == 0) {
                Player player = (Player) commandSender;
                new ReportList(plugin, player).openGUI(player);
            }else if(strings.length == 1) {
                if(strings[0].equalsIgnoreCase("toggle")) {
                    Player player = (Player) commandSender;
                    if(player.hasPermission(Perms.REPORT_ALERT.getPermission())) {
                        if(plugin.getConfig().getBoolean("report-list.toggle." + player.getUniqueId())) {
                            plugin.getConfig().set("report-list.toggle." + player.getUniqueId(), false);
                            plugin.saveConfig();
                            player.sendMessage(chat("&a&lYou have toggled the report list off!"));
                        }else{
                            plugin.getConfig().set("report-list.toggle." + player.getUniqueId(), true);
                            plugin.saveConfig();
                            player.sendMessage(chat("&a&lYou have toggled the report list on!"));
                        }
                    }else{
                        player.sendMessage(chat("&c&lYou do not have permission to use this command!"));
                    }
                }
            }
        }

        return true;
    }
}
