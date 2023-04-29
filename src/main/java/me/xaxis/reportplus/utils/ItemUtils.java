package me.xaxis.reportplus.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ItemUtils {

    private final ItemStack item;
    private final ItemMeta itemMeta;

    public ItemUtils(Material material){
        item = new ItemStack(material);
        itemMeta = item.getItemMeta();
    }

    public ItemUtils lore(String... lore){

        if(lore == null || lore.length == 0) return this;
        List<String> l = Arrays.stream(lore)
                .map(Utils::chat)
                .collect(Collectors.toList());
        itemMeta.setLore(l);
        return this;
    }

    public ItemUtils setTitle(String s){
        itemMeta.setDisplayName(Utils.chat(s));
        return this;
    }

    public ItemStack build(){
        item.setItemMeta(itemMeta);
        return item;
    }

    public ItemStack i(){
        return item;
    }

}
