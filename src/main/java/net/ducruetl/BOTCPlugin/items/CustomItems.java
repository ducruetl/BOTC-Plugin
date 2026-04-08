package net.ducruetl.BOTCPlugin.items;

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

import net.ducruetl.BOTCPlugin.BOTCPlugin;
import net.ducruetl.BOTCPlugin.gameobjects.Game;
import net.ducruetl.BOTCPlugin.gameobjects.GamePlayer;
import net.ducruetl.BOTCPlugin.gameobjects.roles.outsiders.Butler;
import net.ducruetl.BOTCPlugin.gameobjects.roles.outsiders.Drunk;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;

import org.bukkit.Color;

public class CustomItems {
    
    public static ItemStack getConfigItem() {
        ItemStack configItem = new ItemStack(Material.REDSTONE_BLOCK, 1);
        ItemMeta configItemMeta = configItem.getItemMeta();

        final Component displayName = MiniMessage.miniMessage().deserialize("<dark_gray>Paramètres de la partie</dark_gray>");
        configItemMeta.displayName(displayName);
        configItem.setItemMeta(configItemMeta);

        return configItem;
    }

    public static ItemStack getTimeConfigItem() {
        ItemStack timeConfigItem = new ItemStack(Material.CLOCK, 1);
        ItemMeta timeConfigItemMeta = timeConfigItem.getItemMeta();

        final Component displayName = MiniMessage.miniMessage().deserialize("<yellow>Gestion du temps</yellow>");
        timeConfigItemMeta.displayName(displayName);
        timeConfigItem.setItemMeta(timeConfigItemMeta);

        return timeConfigItem;
    }

    public static ItemStack getRoleConfigItem() {
        ItemStack roleConfigItem = new ItemStack(Material.BOOK, 1);
        ItemMeta roleConfigItemMeta = roleConfigItem.getItemMeta();

        final Component displayName = MiniMessage.miniMessage().deserialize("<dark_red>Gestion des rôles</dark_red>");
        roleConfigItemMeta.displayName(displayName);
        roleConfigItem.setItemMeta(roleConfigItemMeta);

        return roleConfigItem;
    }

    public static ItemStack getStartGameItem() {
        ItemStack startGameItem = new ItemStack(Material.LIME_CONCRETE, 1);
        ItemMeta startGameItemMeta = startGameItem.getItemMeta();

        final Component displayName = MiniMessage.miniMessage().deserialize("<green>Démarrer la partie</green>");
        startGameItemMeta.displayName(displayName);
        startGameItem.setItemMeta(startGameItemMeta);

        return startGameItem;
    }

    public static ItemStack getNominationItem() {
        ItemStack nominationItem = new ItemStack(Material.FEATHER, 1);
        ItemMeta meta = nominationItem.getItemMeta();

        final Component displayName = MiniMessage.miniMessage().deserialize("<yellow>Nomination</yellow>");
        meta.displayName(displayName);

        final Component lore = MiniMessage.miniMessage().deserialize("<gray>Clic droit sur un joueur pour le nommer</gray>");
        meta.lore(new ArrayList<>(Arrays.asList(lore)));
        meta.addEnchant(Enchantment.UNBREAKING, 0, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        nominationItem.setItemMeta(meta);

        return nominationItem;
    }

    public static ItemStack getVoteToken() {
        ItemStack voteToken = new ItemStack(Material.GOLD_NUGGET, 1);
        ItemMeta meta = voteToken.getItemMeta();

        final Component displayName = MiniMessage.miniMessage().deserialize("<yellow>Vote</yellow>");
        meta.displayName(displayName);

        final Component lore = MiniMessage.miniMessage().deserialize("<gray>Clic droit sur un joueur pour le nommer</gray>");
        meta.lore(new ArrayList<>(Arrays.asList(lore)));
        meta.addEnchant(Enchantment.UNBREAKING, 0, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
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
            rolesString += player.getPlayer().getName();
            rolesString += " : ";
            if (player.getRole() instanceof Drunk) {
                rolesString += player.getFacadeRole().getName();
            } else {
                rolesString += player.getRole().getName();
            }
            rolesString += "\n";
        }

        meta.addPages(MiniMessage.miniMessage().deserialize(rolesString));
        book.setItemMeta(meta);

        return book;
    }

    public static void handleCustomItem(BOTCPlugin plugin, Cancellable event, Player player, ItemStack item) {
        if (item.equals(getConfigItem())) {
            // Configuration main menu
            event.setCancelled(true);
            final Component title = MiniMessage.miniMessage().deserialize("<dark_gray>Paramètres de la partie</dark_gray>");
            Inventory menuConfig = Bukkit.createInventory(player, 9, title);

            menuConfig.setItem(2, getTimeConfigItem());
            menuConfig.setItem(4, getRoleConfigItem());
            menuConfig.setItem(6, getStartGameItem());

            player.openInventory(menuConfig);
        } else if (item.equals(getTimeConfigItem())) {
            // Configuration time menu
            event.setCancelled(true);
            final Component title = MiniMessage.miniMessage().deserialize("<yellow>Gestion du temps</yellow>");
            Inventory menuTimeConfig = Bukkit.createInventory(player, 9, title);
            player.openInventory(menuTimeConfig);
        } else if (item.equals(getRoleConfigItem())) {
            // Configuration role menu
            event.setCancelled(true);
            final Component title = MiniMessage.miniMessage().deserialize("<dark_red>Gestion des rôles</dark_red>");
            Inventory menuRoleConfig = Bukkit.createInventory(player, 9, title);
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
                    final Component message = MiniMessage.miniMessage().deserialize("<red>Votre maître n'a pas encore voté</red>");
                    player.sendMessage(message);
                    return;
                }
            }

            player.getInventory().remove(getVoteToken());
            plugin.getGame().getPlayersVotes().put(voteTarget, plugin.getGame().getPlayersVotes().get(voteTarget) + 1);

            gamePlayer.setHasVoted(true);

            if (gamePlayer.isDead()) {
                gamePlayer.setHasVotedAfterDeath(true);
            }

            final Component message = MiniMessage.miniMessage().deserialize(
                "<yellow><bold><name></bold></yellow> <blue>a voté pour</blue> <yellow><bold><votedname></bold></yellow>",
                Placeholder.unparsed("name", player.getName()),
                Placeholder.unparsed("votedname", voteTarget.getPlayer().getName())
            );
            Bukkit.broadcast(message);
        }
    }

}
