package fr.ducruetl.BOTCPlugin.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Team;

import fr.ducruetl.BOTCPlugin.BOTCPlugin;

public class PlayerListener implements Listener {

    private final BOTCPlugin plugin;

    public PlayerListener(BOTCPlugin plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String playerName = event.getPlayer().getName();
        event.setJoinMessage(ChatColor.GREEN + "" + ChatColor.BOLD + playerName 
                            + ChatColor.RESET + ChatColor.GREEN + " entre dans le village.");

        // Add player to the nametag team on join to be able to hide their nametag
        Team nametagTeam = plugin.getNametagTeam();
        if (!nametagTeam.hasEntry(playerName)) {
            nametagTeam.addEntry(playerName);
        }

        event.getPlayer().setInvulnerable(true);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        String playerName = event.getPlayer().getName();
        event.setQuitMessage(ChatColor.RED + "" + ChatColor.BOLD + playerName 
                            + ChatColor.RESET + ChatColor.RED + " quitte le village. :(");
    }
    
}
