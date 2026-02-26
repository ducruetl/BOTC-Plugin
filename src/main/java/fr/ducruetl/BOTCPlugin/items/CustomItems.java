package fr.ducruetl.BOTCPlugin.items;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.ducruetl.BOTCPlugin.BOTCPlugin;
import fr.ducruetl.BOTCPlugin.gameobjects.Game;
import org.bukkit.ChatColor;

public class CustomItems {
    
    public static ItemStack getConfigItem() {
        ItemStack configItem = new ItemStack(Material.REDSTONE_BLOCK, 1);
        ItemMeta configItemMeta = configItem.getItemMeta();

        configItemMeta.setDisplayName(ChatColor.DARK_GRAY + "Paramètres de la partie");
        configItem.setItemMeta(configItemMeta);

        return configItem;
    }

    public static ItemStack getTimeConfigItem() {
        ItemStack timeConfigItem = new ItemStack(Material.CLOCK, 1);
        ItemMeta timeConfigItemMeta = timeConfigItem.getItemMeta();

        timeConfigItemMeta.setDisplayName(ChatColor.YELLOW + "Gestion du temps");
        timeConfigItem.setItemMeta(timeConfigItemMeta);

        return timeConfigItem;
    }

    public static ItemStack getRoleConfigItem() {
        ItemStack roleConfigItem = new ItemStack(Material.BOOK, 1);
        ItemMeta roleConfigItemMeta = roleConfigItem.getItemMeta();

        roleConfigItemMeta.setDisplayName(ChatColor.DARK_RED + "Gestion des rôles");
        roleConfigItem.setItemMeta(roleConfigItemMeta);

        return roleConfigItem;
    }

    public static ItemStack getStartGameItem() {
        ItemStack startGameItem = new ItemStack(Material.LIME_CONCRETE, 1);
        ItemMeta startGameItemMeta = startGameItem.getItemMeta();

        startGameItemMeta.setDisplayName(ChatColor.GREEN + "Démarrer la partie");
        startGameItem.setItemMeta(startGameItemMeta);

        return startGameItem;
    }

    public static void handleCustomItem(BOTCPlugin plugin, Cancellable event, Player player, ItemStack item) {
        if (item.equals(getConfigItem())) {
            // Configuration main menu
            event.setCancelled(true);
            Inventory menuConfig = Bukkit.createInventory(player, 9, ChatColor.DARK_GRAY + "Paramètres de la partie");

            menuConfig.setItem(2, getTimeConfigItem());
            menuConfig.setItem(4, getRoleConfigItem());
            menuConfig.setItem(6, getStartGameItem());

            player.openInventory(menuConfig);
        } else if (item.equals(getTimeConfigItem())) {
            // Configuration time menu
            event.setCancelled(true);
            Inventory menuTimeConfig = Bukkit.createInventory(player, 9, ChatColor.YELLOW + "Gestion du temps");
            player.openInventory(menuTimeConfig);
        } else if (item.equals(getRoleConfigItem())) {
            // Configuration role menu
            event.setCancelled(true);
            Inventory menuRoleConfig = Bukkit.createInventory(player, 9, ChatColor.DARK_RED + "Gestion des rôles");
            player.openInventory(menuRoleConfig);
        } else if (item.equals(getStartGameItem())) {
            // Start game
            event.setCancelled(true);
            Game game = new Game(plugin);
            game.startGame();
        }
    }

}
