package net.ducruetl.BOTCPlugin.gameobjects.roles.townsfolks;

import java.util.Random;

import net.ducruetl.BOTCPlugin.gameobjects.Game;
import net.ducruetl.BOTCPlugin.gameobjects.GamePlayer;
import net.ducruetl.BOTCPlugin.gameobjects.NightActions;
import net.ducruetl.BOTCPlugin.gameobjects.Team;
import net.ducruetl.BOTCPlugin.gameobjects.roles.Role;
import net.ducruetl.BOTCPlugin.gameobjects.roles.outsiders.Drunk;
import net.md_5.bungee.api.ChatColor;

public class Chef extends Role {

    public Chef() {
        super(
            "Chef",
            "Au début de la partie, tu recevra le nombre de paires de démons "
            + "qu'il y a autour de la table, 1 paire signifie qu'il y a 2 démons côte à côte.",
            Team.TOWNSFOLK,
            2
        );
    }

    @Override
    public void onNightTurn(Game game, GamePlayer player) {
        if (game.getDay() == 1) {
            int pairs = 0;

            if (player.isPoisoned() || player.getRole() instanceof Drunk) {
                Random r = new Random();
                pairs = r.nextInt(game.getMinionsCount());
            } else {
                GamePlayer previousPlayer = game.getPlayers().getLast();
                
                for (GamePlayer currentPlayer : game.getPlayers()) {
                    if (currentPlayer.isSeenAsDemon()
                    && previousPlayer.isSeenAsDemon()) {
                        pairs++;
                    }

                    previousPlayer = currentPlayer;
                }
            }

            player.getPlayer().sendMessage(
                ChatColor.BLUE + "Il y a " + ChatColor.RED + "" + ChatColor.BOLD + pairs
                + ChatColor.RESET + ChatColor.BLUE + " paires de démons autour de la table."
            );
        }

        NightActions.processNextNightAction(game);
    }
    
}
