package fr.ducruetl.BOTCPlugin.models.roles.outsiders;

import fr.ducruetl.BOTCPlugin.models.Game;
import fr.ducruetl.BOTCPlugin.models.GamePlayer;
import fr.ducruetl.BOTCPlugin.models.Team;
import fr.ducruetl.BOTCPlugin.models.roles.Role;

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
        game.processNextNightAction();
    }
    
}
