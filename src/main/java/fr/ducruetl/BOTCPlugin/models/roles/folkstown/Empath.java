package fr.ducruetl.BOTCPlugin.models.roles.folkstown;

import fr.ducruetl.BOTCPlugin.models.Game;
import fr.ducruetl.BOTCPlugin.models.GamePlayer;
import fr.ducruetl.BOTCPlugin.models.Team;
import fr.ducruetl.BOTCPlugin.models.roles.Role;

public class Empath extends Role {

    public Empath() {
        super(
            "Empathique",
            "Chaque nuit, tu reçois un chiffre correspondant au" 
            + "nombre de démons présents entre tes 2 voisins.",
            Team.TOWNSFOLK,
            2
        );
    }

    @Override
    public void onNightTurn(Game game, GamePlayer player) {
        game.processNextNightAction();
    }
    
}
