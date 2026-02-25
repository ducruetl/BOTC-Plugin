package fr.ducruetl.BOTCPlugin.models.roles.folkstown;

import fr.ducruetl.BOTCPlugin.models.Game;
import fr.ducruetl.BOTCPlugin.models.GamePlayer;
import fr.ducruetl.BOTCPlugin.models.Team;
import fr.ducruetl.BOTCPlugin.models.roles.Role;

public class Monk extends Role {

    public Monk() {
        super(
            "Prêtre",
            "Chaque nuit, tu peut choisir une personne à défendre contre l'attaque du diablotin.",
            Team.TOWNSFOLK,
            2
        );
    }

    @Override
    public void onNightTurn(Game game, GamePlayer player) {
        game.processNextNightAction();
    }
    
}
