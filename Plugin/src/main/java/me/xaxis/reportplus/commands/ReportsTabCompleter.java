package me.xaxis.reportplus.commands;

import me.xaxis.reportplus.player.PlayerDataManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

public final class ReportsTabCompleter implements TabCompleter {

    private static final List<String> SUB_COMMANDS = List.of(
            "toggle",
            "delete",
            "resolve"
    );

    private final PlayerDataManager playerDataManager;

    public ReportsTabCompleter(PlayerDataManager playerDataManager) {
        this.playerDataManager = playerDataManager;
    }

    @Override
    public @NotNull List<String> onTabComplete(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String alias,
            @NotNull String[] args
    ) {

        if (args.length == 1) {
            String input = args[0].toLowerCase(Locale.ROOT);

            return SUB_COMMANDS.stream()
                    .filter(subCommand -> subCommand.startsWith(input))
                    .toList();
        }

        if (args.length == 2
                && (args[0].equalsIgnoreCase("delete")
                || args[0].equalsIgnoreCase("resolve"))) {

            String input = args[1].toLowerCase(Locale.ROOT);

            return playerDataManager.getPlayerNames()
                    .stream()
                    .filter(name ->
                            name.toLowerCase(Locale.ROOT).startsWith(input)
                    )
                    .toList();
        }

        return List.of();
    }
}