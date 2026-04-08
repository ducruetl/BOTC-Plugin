package net.ducruetl.BOTCPlugin.gameobjects.roles.townsfolks;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import net.ducruetl.BOTCPlugin.gameobjects.Game;
import net.ducruetl.BOTCPlugin.gameobjects.GamePlayer;
import net.ducruetl.BOTCPlugin.gameobjects.NightActions;
import net.ducruetl.BOTCPlugin.gameobjects.Team;
import net.ducruetl.BOTCPlugin.gameobjects.roles.Role;
import net.ducruetl.BOTCPlugin.gameobjects.roles.demons.Imp;
import net.ducruetl.BOTCPlugin.gameobjects.roles.outsiders.Drunk;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;

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
        String colorEnd;

        if (role.getTeam() == Team.TOWNSFOLK) {
            color = "<green>";
            colorEnd = "</green>";
        } else if (role.getTeam() == Team.OUTSIDER) {
            color = "<aqua>";
            colorEnd = "</aqua>";
        } else {
            color = "<red>";
            colorEnd = "</red>";
        }

        final Component message = MiniMessage.miniMessage().deserialize(
            "<yellow><bold><name></bold></yellow> <blue>avait pour rôle</blue> " + color + "<role>" + colorEnd,
            Placeholder.unparsed("name", selectedPlayer.getRole().getName()),
            Placeholder.unparsed("role", role.getName())
        );
        player.getPlayer().sendMessage(message);
    }
    
}
