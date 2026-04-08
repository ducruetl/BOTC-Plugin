package net.ducruetl.BOTCPlugin.gameobjects.roles.townsfolks;

import java.util.Random;

import org.bukkit.Bukkit;

import net.ducruetl.BOTCPlugin.gameobjects.Game;
import net.ducruetl.BOTCPlugin.gameobjects.GamePlayer;
import net.ducruetl.BOTCPlugin.gameobjects.NightActions;
import net.ducruetl.BOTCPlugin.gameobjects.Team;
import net.ducruetl.BOTCPlugin.gameobjects.roles.Role;
import net.ducruetl.BOTCPlugin.gameobjects.roles.outsiders.Drunk;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;

public class Empath extends Role {

    public Empath() {
        super(
            "Empathique",
            "Chaque nuit, tu reçois un chifnete correspondant au" 
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

        final Component message = MiniMessage.miniMessage().deserialize(
            "<blue>Il y a</blue> <red><bold><nbdemons></bold></red> <blue>autour de vous.</blue>",
            Placeholder.unparsed("nbdemons", Integer.toString(nbDemons))
        );
        Bukkit.broadcast(message);

        NightActions.processNextNightAction(game);
    }
    
}
