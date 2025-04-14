package me.xaxis.reportplus.commands;

import me.xaxis.reportplus.Main;
import me.xaxis.reportplus.enums.Lang;
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
                            player.sendMessage(Utils.getP(Lang.TOGGLED_REPORT_OFF));
                        }else{
                            plugin.getConfig().set("report-list.toggle." + player.getUniqueId(), true);
                            plugin.saveConfig();
                            player.sendMessage(Utils.getP(Lang.TOGGLED_REPORT_ON));
                        }
                    }else{
                        player.sendMessage(Utils.getP(Lang.NO_PERMISSION));
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
                            player.sendMessage(Utils.getP(Lang.INVALID_PLAYER));
                            return true;
                        }
                        UUID uuid = target.getUniqueId();
                        if(ReportManager.containsReport(uuid)) {
                            ReportManager.removeReport(uuid, plugin);
                            player.sendMessage(Utils.getP(Lang.DELETED_REPORT));
                        }else{
                            player.sendMessage(Utils.getP(Lang.REPORT_NOT_FOUND));
                        }
                    }else{
                        player.sendMessage(Utils.getP(Lang.NO_PERMISSION));
                    }
                    return true;
                }else if(strings[0].equalsIgnoreCase("resolve")) {
                    Player player = (Player) commandSender;
                    if(player.hasPermission(Perms.LIST_REPORTS.getPermission())) {
                        String targetString = strings[1];
                        Player target = plugin.getServer().getPlayer(targetString);
                        if(target == null) {
                            player.sendMessage(Utils.getP(Lang.INVALID_PLAYER));
                            return true;
                        }
                        UUID uuid = target.getUniqueId();
                        if(ReportManager.containsReport(uuid)) {
                            Report report = ReportManager.getUnresolvedReportFromPlayerUUID(uuid);
                            if(report == null) {
                                player.sendMessage(Utils.getP(Lang.REPORT_NOT_FOUND));
                                return true;
                            }
                            if(report.getState() == ReportState.RESOLVED) {
                                player.sendMessage(Utils.getP(Lang.REPORT_ALREADY_RESOLVED));
                                return true;
                            }
                            try {
                                report.resolve();
                            } catch (IOException e) {
                                player.sendMessage(Utils.getP(Lang.REPORT_NOT_FOUND));
                                return true;
                            }
                            player.sendMessage(Utils.getP(Lang.SET_REPORT_AS_RESOLVED));
                        }else{
                            player.sendMessage(Utils.getP(Lang.REPORT_NOT_FOUND));
                        }
                    }else{
                        player.sendMessage(Utils.getP(Lang.NO_PERMISSION));
                    }
                }
            }
        }
        return true;
    }
}
