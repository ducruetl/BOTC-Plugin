package fr.ducruetl.BOTCPlugin.gameobjects.roles.townsfolks;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import fr.ducruetl.BOTCPlugin.gameobjects.Game;
import fr.ducruetl.BOTCPlugin.gameobjects.GamePlayer;
import fr.ducruetl.BOTCPlugin.gameobjects.NightActions;
import fr.ducruetl.BOTCPlugin.gameobjects.Team;
import fr.ducruetl.BOTCPlugin.gameobjects.roles.Role;
import fr.ducruetl.BOTCPlugin.gameobjects.roles.outsiders.Drunk;

public class Empath extends Role {

    public Empath() {
        super(
            "Empathique",
            "Chaque nuit, tu reçois un chiffre correspondant au" 
            + "nombre de démons présents entre tes 2 voisins.",
            Team.TOWNSFOLK,
            2
        );
    }

    @Override
    public void onNightTurn(Game game, GamePlayer player) {
        int nbDemons = 0;
        if (player.isPoisoned() || player.getRole() instanceof Drunk) {
            Random r = new Random();
            nbDemons = r.nextInt(3);
        } else {
            for (int i = 0; i < game.getPlayers().size(); i++) {
                if (game.getPlayers().get(i).equals(player)) {
                    if (i == 0) {
                        if (game.getPlayers().getLast().isSeenAsDemon()) {
                            nbDemons++;
                        }

                        if (game.getPlayers().get(i + 1).isSeenAsDemon()) {
                            nbDemons++;
                        }
                    } else if (i == game.getPlayers().size() - 1) {
                        if (game.getPlayers().get(i - 1).isSeenAsDemon()) {
                            nbDemons++;
                        }

                        if (game.getPlayers().getFirst().isSeenAsDemon()) {
                            nbDemons++;
                        }
                    } else {
                        if (game.getPlayers().get(i - 1).isSeenAsDemon()) {
                            nbDemons++;
                        }

                        if (game.getPlayers().get(i + 1).isSeenAsDemon()) {
                            nbDemons++;
                        }
                    }
                }
            }
        }

        Bukkit.broadcastMessage(
            ChatColor.BLUE + "Il y a "
            + ChatColor.RED + "" + ChatColor.BOLD + nbDemons
            + ChatColor.RESET + ChatColor.BLUE + " autour de vous."
        );

        NightActions.processNextNightAction(game);
    }
    
}
