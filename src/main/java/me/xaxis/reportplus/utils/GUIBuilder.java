package me.xaxis.reportplus.utils;

import me.xaxis.reportplus.ReportPlus;
import me.xaxis.reportplus.gui.GUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class GUIBuilder implements Listener {

    private String title;
    private final ReportPlus plugin;
    private int rows;
    private Map<Integer, GUIItem> items = new HashMap<>();
    private Consumer<InventoryClickEvent> onClick;

    public GUIBuilder(ReportPlus plugin){
        this.plugin= plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public GUIBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public GUIBuilder withRows(int rows) {
        this.rows = rows;
        return this;
    }

    public GUIBuilder withItem(int slot, ItemStack item, Consumer<InventoryClickEvent> onClick) {
        items.put(slot, new GUIItem(item, onClick));
        return this;
    }

    public GUIBuilder withItem(int slot, ItemStack item) {
        return withItem(slot, item, null);
    }

    public GUIBuilder withOnClick(Consumer<InventoryClickEvent> onClick) {
        this.onClick = onClick;
        return this;
    }

    public GUI build() {
        return new GUI() {
            @Override
            public void openGUI(Player player) {
                Inventory inventory = getGUI();
                player.openInventory(inventory);
            }

            @Override
            public Inventory getGUI() {
                Inventory inventory = Bukkit.createInventory(null, rows * 9, title);
                for (Map.Entry<Integer, GUIItem> entry : items.entrySet()) {
                    inventory.setItem(entry.getKey(), entry.getValue().item());
                }
                return inventory;
            }

            @Override
            public void createItems() {
                // Not needed with this builder pattern
            }

            @Override
            public void onClick(InventoryClickEvent event) {
                if (onClick != null) {
                    onClick.accept(event);
                }
            }
        };
    }

    private record GUIItem(ItemStack item, Consumer<InventoryClickEvent> onClick) {

        public void onClick(InventoryClickEvent event) {
                if (onClick != null) {
                    onClick.accept(event);
                }
            }
        }

}
