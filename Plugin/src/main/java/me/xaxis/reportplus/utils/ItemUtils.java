package me.xaxis.reportplus.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemUtils extends ItemStack{

    private final ItemMeta itemMeta;

    public ItemUtils(Material material){
        super(material);
        itemMeta = getItemMeta();
    }

    public ItemUtils lore(List<String> lore){
        if(lore == null || lore.isEmpty()) return this;
        itemMeta.setLore(lore.stream().map(Utils::chat).toList());
        return this;
    }

    public ItemUtils setTitle(String s, boolean colorCoded){
        if(colorCoded) {
            itemMeta.setDisplayName(Utils.chat(s));
        }else {
            itemMeta.setDisplayName(s);
        }
        return this;
    }

    public ItemStack build(){
        setItemMeta(itemMeta);
        return this;
    }

}
