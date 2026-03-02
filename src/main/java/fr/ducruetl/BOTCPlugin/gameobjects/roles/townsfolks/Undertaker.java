package fr.ducruetl.BOTCPlugin.gameobjects.roles.folkstown;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;

import fr.ducruetl.BOTCPlugin.gameobjects.Game;
import fr.ducruetl.BOTCPlugin.gameobjects.GamePlayer;
import fr.ducruetl.BOTCPlugin.gameobjects.NightActions;
import fr.ducruetl.BOTCPlugin.gameobjects.Team;
import fr.ducruetl.BOTCPlugin.gameobjects.roles.Role;
import fr.ducruetl.BOTCPlugin.gameobjects.roles.demons.Imp;
import fr.ducruetl.BOTCPlugin.gameobjects.roles.outsiders.Drunk;

public class Undertaker extends Role {

    public Undertaker() {
        super(
            "Fossoyeur",
            "Lorsque quelqu'un est tué lors du vote du village, tu apprend son rôle la nuit suivante.",
            Team.TOWNSFOLK,
            2
        );
    }

    @Override
    public void onNightTurn(Game game, GamePlayer player) {
        if (game.getLastKilledByVote() != null) {
            Role role = new Imp();
                            
            if (player.isPoisoned() || player.getRole() instanceof Drunk) {
                Random r = new Random();
                List<Role> listRoles;

                switch (r.nextInt(4)) {
                    case 0:
                        listRoles = game.getTownsfolksPool();
                        Collections.shuffle(listRoles);
                        role = listRoles.getFirst();
                        break;
                
                    case 1:
                        listRoles = game.getOutsidersPool();
                        Collections.shuffle(listRoles);
                        role = listRoles.getFirst();
                        break;

                    case 2:
                        listRoles = game.getMinionPool();
                        Collections.shuffle(listRoles);
                        role = listRoles.getFirst();
                        break;

                    case 3:
                        role = new Imp();
                        break;
                }
            } else {
                role = game.getLastKilledByVote().getRole();

                if (role instanceof Drunk) {
                    role = game.getLastKilledByVote().getFacadeRole();
                }
            }

            sendInfoMessage(player, game.getLastKilledByVote(), role);
        }

        NightActions.processNextNightAction(game);
    }

    public void sendInfoMessage(GamePlayer player, GamePlayer selectedPlayer, Role role) {
        String color;

        if (role.getTeam() == Team.TOWNSFOLK) {
            color = ChatColor.GREEN + "" + ChatColor.BOLD;
        } else if (role.getTeam() == Team.OUTSIDER) {
            color = ChatColor.AQUA + "" + ChatColor.BOLD;
        } else {
            color = ChatColor.RED + "" + ChatColor.BOLD;
        }

        player.getPlayer().sendMessage(
            ChatColor.YELLOW + "" + ChatColor.BOLD + selectedPlayer.getPlayer().getDisplayName()
            + ChatColor.RESET + ChatColor.BLUE + " avait pour rôle " + color + role.getName()
        );
    }
    
}
