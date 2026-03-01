package fr.ducruetl.BOTCPlugin.gameobjects;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class NightActions {

    /**
     * Start the next night
     * @param game Game object related
     */
    public static void nextNight(Game game) {
        game.setState(GameState.NIGHT);
        Bukkit.broadcastMessage(ChatColor.DARK_BLUE + "La nuit tombe...");
        
        PotionEffect blindness = new PotionEffect(PotionEffectType.BLINDNESS, PotionEffect.INFINITE_DURATION, 0);
        for (GamePlayer player : game.getPlayers()) {
            player.getPlayer().addPotionEffect(blindness);
            game.getNightOrder().add(player);
        }

        NightActions.processNextNightAction(game);
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
        Bukkit.getLogger().info("Au tour de " + game.getCurrentNightActor().getPlayer().getDisplayName());
        game.getCurrentNightActor().getRole().onNightTurn(game, game.getCurrentNightActor());
    }
}
