package fr.ducruetl.BOTCPlugin.models.roles.folkstown;

import fr.ducruetl.BOTCPlugin.models.Game;
import fr.ducruetl.BOTCPlugin.models.GamePlayer;
import fr.ducruetl.BOTCPlugin.models.Team;
import fr.ducruetl.BOTCPlugin.models.roles.Role;

public class Slayer extends Role {

    public Slayer() {
        super(
            "Pourfendeur",
            "Une fois dans la partie, tu peut choisir de tirer sur quelqu'un, "
            + "si cette personne est diablotin, elle meurt.",
            Team.TOWNSFOLK,
            0
        );
    }

    @Override
    public void onNightTurn(Game game, GamePlayer player) {
        game.processNextNightAction();
    }
    
}
