package me.xaxis.reportplus.utils;

import org.bukkit.ChatColor;

public final class Utils {

    private Utils() {}

    public static String chat(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}
