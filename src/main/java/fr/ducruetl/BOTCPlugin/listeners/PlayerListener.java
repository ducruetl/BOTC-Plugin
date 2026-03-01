package fr.ducruetl.BOTCPlugin.listeners;

import org.bukkit.ChatColor;
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

import fr.ducruetl.BOTCPlugin.BOTCPlugin;
import fr.ducruetl.BOTCPlugin.gameobjects.GamePlayer;
import fr.ducruetl.BOTCPlugin.gameobjects.GameState;
import fr.ducruetl.BOTCPlugin.items.CustomItems;

public class PlayerListener implements Listener {

    private final BOTCPlugin plugin;

    public PlayerListener(BOTCPlugin plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();
        event.setJoinMessage(ChatColor.GREEN + "" + ChatColor.BOLD + playerName 
                            + ChatColor.RESET + ChatColor.GREEN + " entre dans le village.");

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
        event.setQuitMessage(ChatColor.RED + "" + ChatColor.BOLD + playerName 
                            + ChatColor.RESET + ChatColor.RED + " quitte le village. :(");
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

        GamePlayer clickedGamePlayer = plugin.getGame().getPlayersToGamePlayers().get(clickedPlayer);
        if (clickedGamePlayer.isDead()) {
            event.getPlayer().sendMessage(ChatColor.RED + "Ce joueur est déjà mort.");
            return;
        }

        if (plugin.getGame().getNominatedPlayers().contains(clickedGamePlayer)) {
            event.getPlayer().sendMessage(ChatColor.RED + "Ce joueur est déjà nommé.");
            return;
        }
        
        String playerName = ChatColor.YELLOW + "" + ChatColor.BOLD 
            + event.getPlayer().getDisplayName() + ChatColor.RESET;
        String clickedPlayerName = ChatColor.YELLOW + "" + ChatColor.BOLD 
            + clickedGamePlayer.getPlayer().getDisplayName() + ChatColor.RESET;

        plugin.getServer().broadcastMessage(clickedPlayerName + ChatColor.BLUE + " a été nommé par " + playerName);
        plugin.getGame().getNominatedPlayers().add(clickedGamePlayer);
        event.getPlayer().getInventory().remove(CustomItems.getNominationItem());
    }
}
