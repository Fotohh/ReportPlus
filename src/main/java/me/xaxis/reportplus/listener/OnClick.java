package me.xaxis.reportplus.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class OnClick implements Listener {

    @EventHandler
    public void onInvClick(InventoryClickEvent event){
        //Item Selection if name == reporttype value then execute corresponding thingy
        //Report List access hashmap with name of report will be player UUID
    }

}
