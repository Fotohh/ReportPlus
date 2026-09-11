package me.xaxis.reportplus.commands;

import me.xaxis.reportplus.enums.Lang;
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

public final class ReportCommand implements CommandExecutor {

    private final ReportTypeManager reportTypeManager;
    private final ReportService reportService;
    private final LangConfig langConfig;

    public ReportCommand(
            ReportTypeManager reportTypeManager,
            ReportService reportService,
            LangConfig langConfig
    ) {
        this.reportTypeManager = reportTypeManager;
        this.reportService = reportService;
        this.langConfig = langConfig;
    }

    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String[] args
    ) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage(
                    langConfig.getString(Lang.SENDER_NOT_PLAYER)
            );
            return true;
        }

        if (!player.hasPermission(Perms.PLAYER_REPORT.getPermission())) {
            player.sendMessage(
                    langConfig.getString(Lang.NO_PERMISSION)
            );
            return true;
        }

        if (args.length != 1) {
            player.sendMessage(
                    langConfig.getString(Lang.INVALID_REPORT_USAGE)
            );
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);

        if (target == null) {
            player.sendMessage(
                    langConfig.getString(Lang.INVALID_PLAYER)
            );
            return true;
        }

        UUID targetUUID = target.getUniqueId();

        if (targetUUID.equals(player.getUniqueId())) {
            player.sendMessage(
                    langConfig.getString(Lang.CANNOT_REPORT_SELF)
            );
            return true;
        }

        ReportSelection selection = new ReportSelection(
                langConfig,
                reportTypeManager,
                reportService,
                target.getName(),
                player.getName(),
                targetUUID,
                player.getUniqueId()
        );

        selection.openGUI(player);

        return true;
    }
}