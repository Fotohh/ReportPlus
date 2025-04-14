package me.xaxis.reportplus.utils;

import me.xaxis.reportplus.Main;
import me.xaxis.reportplus.enums.Lang;
import me.xaxis.reportplus.enums.Perms;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Utils {

    private final Main plugin;

    public Utils(Main plugin) {
        this.plugin = plugin;
    }


    @Contract("_ -> new")
    public static @NotNull String chat(String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    /**
     * Send message with prefix
     * @param player the target to send the message to
     * @param path the message path from the lang config
     */
    public void message(Player player, Lang path){
        player.sendMessage(plugin.getLangConfig().getString(Lang.PREFIX)+plugin.getLangConfig().getString(path));
    }

    /**
     * Get a message from the lang config with prefix
     * @param path lang
     * @param plugin the plugin
     * @param placeholders the placeholders you want to set
     * @return the message
     */
    public static String getP(Lang path, Main plugin, Object... placeholders){
        return chat(plugin.getLangConfig().getString(Lang.PREFIX)+plugin.getLangConfig().getString(path, placeholders));
    }

    public static String getP(Lang path, Object... placeholders){
        return chat(Main.plugin.getLangConfig().getString(Lang.PREFIX)+Main.plugin.getLangConfig().getString(path, placeholders));
    }

    public static String get(Lang path, Object... placeholders){
        return chat(Main.plugin.getLangConfig().getString(path, placeholders));
    }

    /**
     * Get a message from the lang config
     * @param path lang
     * @param plugin the plugin
     * @param placeholders the placeholders you want to set
     * @return the message
     */
    public static String get(Lang path, Main plugin, Object... placeholders){
        return chat(plugin.getLangConfig().getString(path, placeholders));
    }

    public static String[] getSL(Lang path, Object... placeholders){
        List<String> list = Main.plugin.getLangConfig().getConfig().getStringList(path.getPath());
        AtomicInteger c = new AtomicInteger(-1);
        list = list.stream().map(Utils::chat).map(s -> {
            c.getAndIncrement();
            return s.replace("%s", placeholders[c.get()].toString());
        }).toList();
        return list.toArray(new String[list.size() - 1]);
    }

    /**
     * Send message with prefix
     * @param player the target to send the message to
     * @param path the message path from the lang config
     * @param placeholders the placeholders you want to set
     */
    public void message(Player player, Lang path, Object... placeholders){
        player.sendMessage(plugin.getLangConfig().getString(Lang.PREFIX)+plugin.getLangConfig().getString(path, placeholders));
    }

    /**
     * Send a message without the prefix added before the message
     * @param path the path to the message
     * @param player the player to send the message to
     */
    public void message(Lang path, Player player){
        player.sendMessage(plugin.getLangConfig().getString(path));
    }

    /**
     *
     * @param player the target to check
     * @param perms perms to check
     * @return true if player has perms, false if not
     */
    public boolean checkPermission(@NotNull Player player, Perms... perms) {
        if(perms != null) {
            for (Perms perm : perms) {
                if (perm != null && !player.hasPermission(perm.getPermission())) {
                    message(player, Lang.NO_PERMISSION);
                    return false;
                }
            }
        }
        return true;
    }

    /**
     *
     * @param sender the target to check
     * @return true if sender is valid, false if not
     */
    public boolean isValid(@NotNull CommandSender sender){

        if(!(sender instanceof Player)){
            message(sender, Lang.SENDER_NOT_PLAYER);
            return false;
        }

        return true;
    }

    /**
     *
     * @param sender the target to check
     * @param perms perms to check
     * @return true if sender is valid, false if not
     */
    public boolean isValid(@NotNull CommandSender sender, Perms... perms){

        if(!(sender instanceof Player player)){
            message(sender, Lang.SENDER_NOT_PLAYER);
            return false;
        }

        return checkPermission(player, perms);
    }

    /**
     * Checks if target is valid
     * @param player the player to send a message to if the player is invalid
     * @param target the target to check
     * @return true if valid, false if not
     */
    public boolean isTargetValid(Player player, Player target){

        if(target == null || !target.isOnline()){
            message(player, Lang.INVALID_PLAYER);
            return false;
        }
        return true;
    }

    /**
     * Checks if target is valid
     * @param player the player to send a message to if the player is invalid
     * @param target the target to check
     * @return true if valid, false if not
     */
    public boolean isTargetValid(Player player, OfflinePlayer target){

        if(target == null){
            message(player, Lang.INVALID_PLAYER);
            return false;
        }
        return true;
    }

    /**
     * Same thing as {@link #isTargetValid(Player, Player)} except for the fact that
     * it does not send a message.
     * @param target the target to check
     * @return true if valid, false if not
     */
    public boolean isTargetValid(Player target){
        return target != null && target.isOnline();
    }

    public boolean isTargetValid(OfflinePlayer target){
        return target != null;
    }

    /**
     * Send message with prefix
     * @param sender the target to send the message to
     * @param path the message path from the lang config
     */
    public void message(CommandSender sender, Lang path){
        sender.sendMessage(plugin.getLangConfig().getString(Lang.PREFIX)+plugin.getLangConfig().getString(path));
    }
}
