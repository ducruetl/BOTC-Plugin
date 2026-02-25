package fr.ducruetl.BOTCPlugin.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import fr.ducruetl.BOTCPlugin.items.CustomItems;

public class InventoryListener implements Listener {
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();

        if (!(event.getWhoClicked() instanceof Player player) || item == null) {
            return;
        }

        CustomItems.handleCustomItem(event, player, item);
    }

}
