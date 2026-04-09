package net.ducruetl.BOTCPlugin.listeners;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.RayTraceResult;

import net.ducruetl.BOTCPlugin.BOTCPlugin;
import net.ducruetl.BOTCPlugin.gameobjects.GamePlayer;
import net.ducruetl.BOTCPlugin.gameobjects.GameState;
import net.ducruetl.BOTCPlugin.items.CustomItems;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;

public class PlayerListener implements Listener {

    private final BOTCPlugin plugin;

    public PlayerListener(BOTCPlugin plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();
        final Component joinMessage = MiniMessage.miniMessage().deserialize(
            "<green><bold><name></bold> entre dans le village</green>",
            Placeholder.unparsed("name", playerName)
        );
        event.joinMessage(joinMessage);

        // Add player to the nametag team on join to be able to hide their nametag
        Team nametagTeam = plugin.getNametagTeam();
        if (!nametagTeam.hasEntry(playerName)) {
            nametagTeam.addEntry(playerName);
        }

        player.setInvulnerable(true);
        player.setGameMode(GameMode.ADVENTURE);
        player.teleport(player.getWorld().getSpawnLocation());

        if (player.isOp()) {
            player.getInventory().setItem(8, CustomItems.getConfigItem());
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        String playerName = event.getPlayer().getName();
        final Component quitMessage = MiniMessage.miniMessage().deserialize(
            "<red><bold><name></bold> quitte le village.</red>",
            Placeholder.unparsed("name", playerName)
        );
        event.quitMessage(quitMessage);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item == null) {
            return;
        }

        CustomItems.handleCustomItem(plugin, event, player, item);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onInteractWithPlayer(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR) {
            return;
        }

        if (plugin.getGame() == null 
        || plugin.getGame().getState() != GameState.MEETING) {
            return;
        }

        Player player = event.getPlayer();
        if (!player.getInventory().getItemInMainHand().equals(CustomItems.getNominationItem())) {
            return;
        }

        RayTraceResult result = player.getWorld().rayTraceEntities(
            player.getEyeLocation(),
            player.getEyeLocation().getDirection(),
            16.0, // range
            entity -> entity instanceof Player && entity != player
        );

        if (result == null 
        || !(result.getHitEntity() instanceof Player clickedPlayer)) {
            return;
        }

        GamePlayer clickedGamePlayer = plugin.getGame().getUuidToGamePlayers().get(clickedPlayer.getUniqueId());
        if (clickedGamePlayer.isDead()) {
            event.getPlayer().sendMessage(MiniMessage.miniMessage().deserialize("<red>Ce joueur est déjà mort.</red>"));
            return;
        }

        if (plugin.getGame().getNominatedPlayers().contains(clickedGamePlayer)) {
            event.getPlayer().sendMessage(MiniMessage.miniMessage().deserialize("<red>Ce joueur est déjà nommé.</red>"));
            return;
        }
        
        final Component message = MiniMessage.miniMessage().deserialize(
            "<yellow><bold><clickedname></bold></yellow> <blue>a été nommé par</blue> <yellow><bold><name></bold></yellow>",
            Placeholder.unparsed("clickedname", clickedGamePlayer.getPlayer().getName()),
            Placeholder.unparsed("name", player.getName())
        );

        plugin.getServer().broadcast(message);
        plugin.getGame().getNominatedPlayers().add(clickedGamePlayer);
        event.getPlayer().getInventory().remove(CustomItems.getNominationItem());
    }
}
