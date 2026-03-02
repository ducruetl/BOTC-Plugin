package fr.ducruetl.BOTCPlugin.items;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import fr.ducruetl.BOTCPlugin.BOTCPlugin;
import fr.ducruetl.BOTCPlugin.gameobjects.Game;
import fr.ducruetl.BOTCPlugin.gameobjects.GamePlayer;
import fr.ducruetl.BOTCPlugin.gameobjects.roles.outsiders.Butler;
import fr.ducruetl.BOTCPlugin.gameobjects.roles.outsiders.Drunk;

import org.bukkit.ChatColor;
import org.bukkit.Color;

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

    public static ItemStack getNominationItem() {
        ItemStack nominationItem = new ItemStack(Material.FEATHER, 1);
        ItemMeta meta = nominationItem.getItemMeta();

        meta.setDisplayName(ChatColor.YELLOW + "Nomination");
        meta.setLore(new ArrayList<>(Arrays.asList(ChatColor.GRAY + "Clic droit sur un joueur pour le nommer")));
        meta.addEnchant(Enchantment.UNBREAKING, 0, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTMENTS);
        nominationItem.setItemMeta(meta);

        return nominationItem;
    }

    public static ItemStack getVoteToken() {
        ItemStack voteToken = new ItemStack(Material.GOLD_NUGGET, 1);
        ItemMeta meta = voteToken.getItemMeta();

        meta.setDisplayName(ChatColor.YELLOW + "Vote");
        meta.setLore(new ArrayList<>(Arrays.asList(ChatColor.GRAY + "Clic droit sur un joueur pour le voter")));
        meta.addEnchant(Enchantment.UNBREAKING, 0, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTMENTS);
        voteToken.setItemMeta(meta);

        return voteToken;
    }

    public static ItemStack getDeathHelmet() {
        ItemStack deathHelmet = new ItemStack(Material.LEATHER_HELMET, 1);
        LeatherArmorMeta meta = (LeatherArmorMeta) deathHelmet.getItemMeta();

        meta.setColor(Color.BLACK);
        deathHelmet.setItemMeta(meta);

        return deathHelmet;
    }

    public static ItemStack getRolesBook(Game game) {
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();

        meta.setTitle("Roles");
        meta.setAuthor("Game");

        String rolesString = "";

        for (GamePlayer player : game.getPlayers()) {
            rolesString += player.getPlayer().getDisplayName();
            rolesString += " : ";
            if (player.getRole() instanceof Drunk) {
                rolesString += player.getFacadeRole().getName();
            } else {
                rolesString += player.getRole().getName();
            }
            rolesString += "\n";
        }

        meta.addPage(rolesString);
        book.setItemMeta(meta);

        return book;
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
            plugin.setGame(new Game(plugin));
            plugin.getGame().startGame();
        } else if (item.equals(getVoteToken())) {
            // Vote for current nominated player
            event.setCancelled(true);
            GamePlayer voteTarget = plugin.getGame().getNominatedPlayers().getFirst();

            GamePlayer gamePlayer = plugin.getGame().getPlayersToGamePlayers().get(player);
            if (gamePlayer.getRole() instanceof Butler butler) {
                if (!butler.getMaster().getHasVoted()) {
                    player.sendMessage(ChatColor.RED + "Votre maître n'a pas encore voté.");
                    return;
                }
            }

            player.getInventory().remove(getVoteToken());
            plugin.getGame().getPlayersVotes().put(voteTarget, plugin.getGame().getPlayersVotes().get(voteTarget) + 1);

            gamePlayer.setHasVoted(true);

            if (gamePlayer.isDead()) {
                gamePlayer.setHasVotedAfterDeath(true);
            }

            Bukkit.broadcastMessage(
                ChatColor.YELLOW + "" + ChatColor.BOLD + player.getDisplayName()
                + ChatColor.RESET + ChatColor.BLUE + " a voté pour "
                + ChatColor.YELLOW + "" + ChatColor.BOLD + voteTarget.getPlayer().getDisplayName()
            );
        }
    }

}
