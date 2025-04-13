package me.xaxis.reportplus.commands;

import me.xaxis.reportplus.Main;
import me.xaxis.reportplus.enums.Perms;
import me.xaxis.reportplus.enums.ReportState;
import me.xaxis.reportplus.gui.ReportList;
import me.xaxis.reportplus.reports.Report;
import me.xaxis.reportplus.reports.ReportManager;
import me.xaxis.reportplus.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.UUID;

public class Reports extends Utils implements CommandExecutor {

    private final Main plugin;

    public Reports(Main plugin) {
        super(plugin);
        this.plugin = plugin;
        plugin.getCommand("reports").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (isValid(commandSender)) {
            if(strings.length == 0) {
                if(commandSender.hasPermission(Perms.LIST_REPORTS.getPermission())) {
                    Player player = (Player) commandSender;
                    new ReportList(plugin).openGUI(player);
                    return true;
                }
            }else if(strings.length == 1) {
                if(strings[0].equalsIgnoreCase("toggle")) {
                    Player player = (Player) commandSender;
                    if(player.hasPermission(Perms.REPORT_ALERT.getPermission())) {
                        if(plugin.getConfig().getBoolean("report-list.toggle." + player.getUniqueId())) {
                            plugin.getConfig().set("report-list.toggle." + player.getUniqueId(), false);
                            plugin.saveConfig();
                            player.sendMessage(chat("&a&lYou have toggled report alerts off!"));
                        }else{
                            plugin.getConfig().set("report-list.toggle." + player.getUniqueId(), true);
                            plugin.saveConfig();
                            player.sendMessage(chat("&a&lYou have toggled report alerts on!"));
                        }
                    }else{
                        player.sendMessage(chat("&c&lYou do not have permission to use this command!"));
                    }
                    return true;
                }
            }else if(strings.length == 2) {
                if(strings[0].equalsIgnoreCase("delete")) {
                    Player player = (Player) commandSender;
                    if(player.hasPermission(Perms.LIST_REPORTS.getPermission())) {
                        String targetString = strings[1];
                        Player target = plugin.getServer().getPlayer(targetString);
                        if(target == null) {
                            player.sendMessage(chat("&c&lThat player is not online or doesn't exist!"));
                            return true;
                        }
                        UUID uuid = target.getUniqueId();
                        if(ReportManager.containsReport(uuid)) {
                            ReportManager.removeReport(uuid, plugin);
                            player.sendMessage(chat("&a&lYou have deleted the report!"));
                        }else{
                            player.sendMessage(chat("&c&lThat report does not exist!"));
                        }
                    }else{
                        player.sendMessage(chat("&c&lYou do not have permission to use this command!"));
                    }
                    return true;
                }else if(strings[0].equalsIgnoreCase("resolve")) {
                    Player player = (Player) commandSender;
                    if(player.hasPermission(Perms.LIST_REPORTS.getPermission())) {
                        String targetString = strings[1];
                        Player target = plugin.getServer().getPlayer(targetString);
                        if(target == null) {
                            player.sendMessage(chat("&c&lThat player is not online or doesn't exist!"));
                            return true;
                        }
                        UUID uuid = target.getUniqueId();
                        if(ReportManager.containsReport(uuid)) {
                            Report report = ReportManager.getUnresolvedReportFromPlayerUUID(uuid);
                            if(report == null) {
                                player.sendMessage(chat("&c&lThat report does not exist!"));
                                return true;
                            }
                            if(report.getState() == ReportState.RESOLVED) {
                                player.sendMessage(chat("&c&lThat report has already been resolved!"));
                                return true;
                            }
                            try {
                                report.resolve();
                            } catch (IOException e) {
                                player.sendMessage(chat("&c&lThat report does not exist!"));
                                return true;
                            }
                            player.sendMessage(chat("&a&lYou have resolved the report!"));
                        }else{
                            player.sendMessage(chat("&c&lThat report does not exist!"));
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
