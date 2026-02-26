package fr.ducruetl.BOTCPlugin.gameobjects;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class NightActions {

    public static void nextNight(Game game) {
        game.setState(GameState.NIGHT);
        Bukkit.broadcastMessage(ChatColor.DARK_BLUE + "La nuit tombe...");
        
        PotionEffect blindness = new PotionEffect(PotionEffectType.BLINDNESS, PotionEffect.INFINITE_DURATION, 1);
        for (GamePlayer player : game.getPlayers()) {
            player.getPlayer().addPotionEffect(blindness);
        }

        NightActions.processNextNightAction(game);
    }

    public static void processNextNightAction(Game game) {
        if (game.getNightOrder().isEmpty()) {
            NightActions.endNight(game);
            return;
        }

        game.setCurrentNightActor(game.getNightOrder().poll());
        game.getCurrentNightActor().getRole().onNightTurn(game, game.getCurrentNightActor());
    }

    public static void endNight(Game game) {
        game.setState(GameState.DAY);
        Bukkit.broadcastMessage(ChatColor.DARK_BLUE + "Fin de la nuit !");
    }

}
