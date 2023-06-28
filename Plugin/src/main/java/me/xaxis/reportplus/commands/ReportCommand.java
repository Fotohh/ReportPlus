package me.xaxis.reportplus.commands;

import me.xaxis.reportplus.Main;
import me.xaxis.reportplus.enums.Lang;
import me.xaxis.reportplus.enums.Perms;
import me.xaxis.reportplus.gui.GUI;
import me.xaxis.reportplus.gui.ReportSelection;
import me.xaxis.reportplus.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ReportCommand extends Utils implements CommandExecutor {

    private final Main plugin;

    public ReportCommand(Main plugin){
        super(plugin);
        this.plugin = plugin;
        plugin.getCommand("report").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String string, @NotNull String[] args) {

        if (isValid(sender, Perms.PLAYER_REPORT)) {

            Player player = (Player) sender;

            if (args.length == 1) {

                String s = args[0];

                OfflinePlayer target = Bukkit.getPlayer(s);

                if (isTargetValid(player, target)) {
                    GUI reportSelection = new ReportSelection(plugin, target.getUniqueId().toString(), player);
                    reportSelection.openGUI(player);
                }

                return true;

            } else {
                message(player, Lang.NOT_ENOUGH_ARGS);
                return true;
            }
        }
        return true;
    }
}
