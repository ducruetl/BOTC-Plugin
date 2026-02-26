package fr.ducruetl.BOTCPlugin.gameobjects.roles.folkstown;

import fr.ducruetl.BOTCPlugin.gameobjects.Game;
import fr.ducruetl.BOTCPlugin.gameobjects.GamePlayer;
import fr.ducruetl.BOTCPlugin.gameobjects.NightActions;
import fr.ducruetl.BOTCPlugin.gameobjects.Team;
import fr.ducruetl.BOTCPlugin.gameobjects.roles.Role;

public class Washerwoman extends Role {

    public Washerwoman() {
        super(
            "Lavendière",
            "Au début de la partie, tu connaitras un rôle citoyen et 2 personnes, "
            + "une de ces personnes possède ce rôle.",
            Team.TOWNSFOLK,
            2
        );
    }

    @Override
    public void onNightTurn(Game game, GamePlayer player) {
        NightActions.processNextNightAction(game);
    }
    
}
