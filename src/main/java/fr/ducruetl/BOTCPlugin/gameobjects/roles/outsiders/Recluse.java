package fr.ducruetl.BOTCPlugin.gameobjects.roles.outsiders;

import java.util.Random;

import fr.ducruetl.BOTCPlugin.gameobjects.Game;
import fr.ducruetl.BOTCPlugin.gameobjects.GamePlayer;
import fr.ducruetl.BOTCPlugin.gameobjects.NightActions;
import fr.ducruetl.BOTCPlugin.gameobjects.Team;
import fr.ducruetl.BOTCPlugin.gameobjects.roles.Role;

public class Recluse extends Role {

    public Recluse() {
        super(
            "Reclus",
            "Tu peut apparaitre comme un démon au yeux "
            + "des rôles à informations, et cela change à chaque nuit.",
            Team.OUTSIDER,
            0
        );
    }

    @Override
    public void onNightTurn(Game game, GamePlayer player) {
        Random random = new Random();
        int team = random.nextInt(4);

        if (team == 0) {
            player.setFacadeTeam(Team.TOWNSFOLK);
        } else if (team == 1) {
            player.setFacadeTeam(Team.OUTSIDER);
        } else if (team == 2) {
            player.setFacadeTeam(Team.MINION);
        } else {
            player.setFacadeTeam(Team.DEMON);
        }

        NightActions.processNextNightAction(game);
    }
    
}
