package fr.ducruetl.BOTCPlugin.models.roles.folkstown;

import fr.ducruetl.BOTCPlugin.models.Game;
import fr.ducruetl.BOTCPlugin.models.GamePlayer;
import fr.ducruetl.BOTCPlugin.models.Team;
import fr.ducruetl.BOTCPlugin.models.roles.Role;

public class Ravenkeeper extends Role {

    public Ravenkeeper() {
        super(
            "Croque-Mort",
            "Si le diablotin te tue pendant la nuit, tu peut "
            + "choisir de voir le rôle de la personne de ton choix.",
            Team.TOWNSFOLK,
            2
        );
    }

    @Override
    public void onNightTurn(Game game, GamePlayer player) {
        game.processNextNightAction();
    }
    
}
