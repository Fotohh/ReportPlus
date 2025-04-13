package me.xaxis.reportplus.commands;

import me.xaxis.reportplus.Main;
import me.xaxis.reportplus.enums.Perms;
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
                    if(target.getUniqueId().equals(player.getUniqueId())){
                        player.sendMessage(chat("&c&lYou cannot report yourself!"));
                        return true;
                    }
                    ReportSelection reportSelection = new ReportSelection(plugin, player, target.getUniqueId());
                    reportSelection.openGUI(player);
                }

            } else {
                player.sendMessage(chat("&c&lUsage: /report <target>"));
            }
            return true;
        }
        return true;
    }
}
