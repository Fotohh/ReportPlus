package me.xaxis.reportplus.utils;

import org.bukkit.ChatColor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class Utils {
    @Contract("_ -> new")
    public static @NotNull String chat(String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
