package net.ducruetl.BOTCPlugin.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import net.ducruetl.BOTCPlugin.BOTCPlugin;
import net.ducruetl.BOTCPlugin.gameobjects.GameState;
import net.ducruetl.BOTCPlugin.items.CustomItems;

public class InventoryListener implements Listener {

    private BOTCPlugin plugin;

    public InventoryListener(BOTCPlugin plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();

        if (!(event.getWhoClicked() instanceof Player player) || item == null) {
            return;
        }

        if (plugin.getGame() != null
                && !plugin.getGame().getState().equals(GameState.NOT_STARTED)
                && event.getView().title().equals("Choose a player")) {
            if (item.getType().equals(Material.PLAYER_HEAD)
                    && item.hasItemMeta()) {
                event.setCancelled(true);
                SkullMeta meta = (SkullMeta) item.getItemMeta();
                Player selectedPlayer = meta.getOwningPlayer().getPlayer();
                plugin.getGame().setSelectedPlayer(
                    plugin.getGame().getPlayersToGamePlayers().get(selectedPlayer)
                );
                return;
            }
        }

        CustomItems.handleCustomItem(plugin, event, player, item);
    }

}
