package me.xaxis.reportplus.commands;

import me.xaxis.reportplus.ReportPlus;
import me.xaxis.reportplus.enums.ReportType;
import me.xaxis.reportplus.managers.Report;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ReportCommand implements CommandExecutor {

    private final ReportPlus plugin;

    public ReportCommand(ReportPlus plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String string, @NotNull String[] args) {

        if(sender instanceof Player player){

            if(player.hasPermission("blacklistedFromReports")) {

                if(args.length == 1){

                    // /report <player_name>

                    String s = args[0];

                    Player target = Bukkit.getPlayer(s);

                    if(target!= null && target.isOnline()){

                        Report report = new Report(plugin, target.getUniqueId(), player.getUniqueId(), ReportType.AUTOCLICKING);

                    }else{
                        //error
                    }

                }else{
                    //error
                }

            }else{
                //error
            }

        }else{
            //error
        }

        return true;
    }
}
