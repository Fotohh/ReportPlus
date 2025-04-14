package me.xaxis.reportplus.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReportsTabCompleter implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
                                                @NotNull String alias, @NotNull String[] args) {

        if (!command.getName().equalsIgnoreCase("reports")) return Collections.emptyList();

        if (args.length == 1) {
            List<String> subCommands = new ArrayList<>();
            subCommands.add("toggle");
            subCommands.add("delete");
            subCommands.add("resolve");
            return subCommands.stream()
                    .filter(sub -> sub.startsWith(args[0].toLowerCase()))
                    .toList();
        }

        if (args.length == 2 && (args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("resolve"))) {
            return Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                    .toList();
        }

        return Collections.emptyList();
    }
}
