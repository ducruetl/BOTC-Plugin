package fr.ducruetl.BOTCPlugin.gameobjects.roles.folkstown;

import fr.ducruetl.BOTCPlugin.gameobjects.Game;
import fr.ducruetl.BOTCPlugin.gameobjects.GamePlayer;
import fr.ducruetl.BOTCPlugin.gameobjects.NightActions;
import fr.ducruetl.BOTCPlugin.gameobjects.Team;
import fr.ducruetl.BOTCPlugin.gameobjects.roles.Role;

public class Ravenkeeper extends Role {

    public Ravenkeeper() {
        super(
            "Croque-Mort",
            "Si le diablotin te tue pendant la nuit, tu peut "
            + "choisir de voir le rôle de la personne de ton choix.",
            Team.TOWNSFOLK,
            4
        );
    }

    @Override
    public void onNightTurn(Game game, GamePlayer player) {
        NightActions.processNextNightAction(game);
    }
    
}
