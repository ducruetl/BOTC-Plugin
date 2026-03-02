package fr.ducruetl.BOTCPlugin.gameobjects.roles.townsfolks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.jspecify.annotations.Nullable;

import fr.ducruetl.BOTCPlugin.gameobjects.Game;
import fr.ducruetl.BOTCPlugin.gameobjects.GamePlayer;
import fr.ducruetl.BOTCPlugin.gameobjects.NightActions;
import fr.ducruetl.BOTCPlugin.gameobjects.Team;
import fr.ducruetl.BOTCPlugin.gameobjects.roles.Role;
import fr.ducruetl.BOTCPlugin.gameobjects.roles.outsiders.Drunk;

public class Investigator extends Role {

    public Investigator() {
        super(
            "Détective",
            "Au début de la partie, tu connaitras un rôle sbire et 2 personnes, "
            + "une de ces personnes possède ce rôle.",
            Team.TOWNSFOLK,
            2
        );
    }

    @Override
    public void onNightTurn(Game game, GamePlayer player) {
        if (game.getDay() == 1) {
            GamePlayer firstPlayer;
            GamePlayer secondPlayer;

            if (player.isPoisoned() || player.getRole() instanceof Drunk) {
                List<Role> minionsRoles = game.getMinionPool();
                Collections.shuffle(minionsRoles);
                Role minionRole = minionsRoles.getFirst();
                
                ArrayList<GamePlayer> players = game.getPlayers();
                players.remove(player);
                Collections.shuffle(players);
                firstPlayer = players.getFirst();
                players.removeFirst();
                secondPlayer = players.getFirst();

                Random r = new Random();
                if (r.nextInt(4) != 0) {
                    sendInfoMessage(player, null, null, null);
                } else {
                    sendInfoMessage(player, firstPlayer, secondPlayer, minionRole);
                }
            } else {
                ArrayList<GamePlayer> players = game.getPlayers();
                players.remove(player);
                Collections.shuffle(players);

                int i = 0;
                while (i < players.size() && players.get(i).getRole().getTeam() != Team.MINION) {
                    i++;
                }

                if (i < players.size()) {
                    firstPlayer = players.get(i);
                    if (i != 0) {
                        secondPlayer = players.get(i - 1);
                    } else {
                        secondPlayer = players.get(i + 1);
                    }

                    sendInfoMessage(player, firstPlayer, secondPlayer, firstPlayer.getRole());
                } else {
                    sendInfoMessage(player, null, null, null);
                }
            }
        }

        NightActions.processNextNightAction(game);
    }
    
    public void sendInfoMessage(GamePlayer player, @Nullable GamePlayer firstPlayer, 
        @Nullable GamePlayer secondPlayer, @Nullable Role role) {

        if (firstPlayer == null || secondPlayer == null || role == null) {
            player.getPlayer().sendMessage(
                ChatColor.BLUE + "Il n'y a pas de sbires dans la partie."
            );
        } else {
            String firstPlayerName;
            String secondPlayerName;
            Random r = new Random();
            
            if (r.nextBoolean()) {
                firstPlayerName = firstPlayer.getPlayer().getDisplayName();
                secondPlayerName = secondPlayer.getPlayer().getDisplayName();
            } else {
                firstPlayerName = secondPlayer.getPlayer().getDisplayName();
                secondPlayerName = firstPlayer.getPlayer().getDisplayName();
            }

            player.getPlayer().sendMessage(
                ChatColor.BLUE + "Le rôle " + ChatColor.RED + "" + ChatColor.BOLD + role.getName()
                + ChatColor.RESET + ChatColor.BLUE + " se trouve entre " + ChatColor.YELLOW + ""
                + ChatColor.BOLD + firstPlayerName + ChatColor.RESET + ChatColor.BLUE + " et "
                +  ChatColor.YELLOW + "" + ChatColor.BOLD + secondPlayerName
            );
        }
    }
}
