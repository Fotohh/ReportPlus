package me.xaxis.reportplus.commands;

import me.xaxis.reportplus.enums.Perms;
import me.xaxis.reportplus.file.LangConfig;
import me.xaxis.reportplus.gui.ReportSelection;
import me.xaxis.reportplus.reports.ReportService;
import me.xaxis.reportplus.reports.ReportTypeManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ReportCommand implements CommandExecutor {

    private final ReportTypeManager reportTypeManager;
    private final ReportService reportService;
    private final LangConfig langConfig;

    public ReportCommand(ReportTypeManager reportTypeManager, ReportService reportService, LangConfig langConfig) {
        this.reportService = reportService;
        this.reportTypeManager = reportTypeManager;
        this.langConfig = langConfig;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String string, @NotNull String[] args) {

        if(!(sender instanceof Player player)) {
            //todo message sender not player
            return true;
        }
        if(!player.hasPermission(Perms.PLAYER_REPORT.getPermission())){
            //todo message no perms
            return true;
        }
        if (args.length == 1) {

            String targetName = args[0];

            Player target = Bukkit.getPlayer(targetName);

            if(target == null) {
                //todo message target doesn't exist
                return true;
            }

            targetName = target.getName();

            UUID targetUUID = target.getUniqueId();

            if(targetUUID.equals(player.getUniqueId())){
                //todo message can't report self
                return true;
            }
            ReportSelection selection = new ReportSelection(langConfig, reportTypeManager, reportService, targetName, player.getName(), targetUUID, player.getUniqueId());

            selection.openGUI(player);

        } else {
            //todo message invalid usage
        }
        return true;
    }
}
