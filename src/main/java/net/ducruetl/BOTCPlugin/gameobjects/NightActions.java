package net.ducruetl.BOTCPlugin.gameobjects;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.ducruetl.BOTCPlugin.BOTCPlugin;
import net.ducruetl.BOTCPlugin.gameobjects.roles.outsiders.Drunk;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class NightActions {

    /**
     * Start the next night
     * @param game Game object related
     */
    public static void nextNight(Game game) {
        game.setState(GameState.NIGHT);
        final Component message = MiniMessage.miniMessage().deserialize(
            "<dark_blue>La nuit tombe...</dark_blue>"
        );
        Bukkit.broadcast(message);
        
        PotionEffect blindness = new PotionEffect(PotionEffectType.BLINDNESS, PotionEffect.INFINITE_DURATION, 0);
        for (GamePlayer player : game.getPlayers()) {
            player.getPlayer().addPotionEffect(blindness);
            game.getNightOrder().add(player);
        }

        NightActions.teleportPlayersToRooms(game);
    }

    /**
     * Teleport players to their room
     * @param game Game instance
     */
    public static void teleportPlayersToRooms(Game game) {
        BOTCPlugin plugin = JavaPlugin.getPlugin(BOTCPlugin.class);

        List<CompletableFuture<Boolean>> futures = new ArrayList<>();

        for (int i = 0; i < game.getPlayers().size(); i++) {
            Player player = game.getPlayers().get(i).getPlayer();
            Location location = plugin.getRoomPositions().get(i);

            futures.add(player.teleportAsync(location));
        }

        // Wait for all tp before starting night actions
        CompletableFuture
            .allOf(futures.toArray(new CompletableFuture[0]))
            .thenRun(() -> NightActions.processNextNightAction(game));
    }

    /**
     * Start the next role night action in the game night order queue
     * @param game Game object related
     */
    public static void processNextNightAction(Game game) {
        if (game.getNightOrder().isEmpty()) {
            for (GamePlayer player : game.getPlayers()) {
                player.getPlayer().removePotionEffect(PotionEffectType.BLINDNESS);
            }
            DayActions.nextDay(game);
            return;
        }

        game.setCurrentNightActor(game.getNightOrder().poll());
        Bukkit.getLogger().info("Au tour de " + game.getCurrentNightActor().getPlayer().getName());

        if (game.getCurrentNightActor().getRole() instanceof Drunk) {
            game.getCurrentNightActor().getFacadeRole().onNightTurn(game, game.getCurrentNightActor());
        } else {
            game.getCurrentNightActor().getRole().onNightTurn(game, game.getCurrentNightActor());
        }    
    }
}
