package fr.ducruetl.BOTCPlugin.gameobjects.roles.folkstown;

import fr.ducruetl.BOTCPlugin.gameobjects.Game;
import fr.ducruetl.BOTCPlugin.gameobjects.GamePlayer;
import fr.ducruetl.BOTCPlugin.gameobjects.NightActions;
import fr.ducruetl.BOTCPlugin.gameobjects.Team;
import fr.ducruetl.BOTCPlugin.gameobjects.roles.Role;

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
        NightActions.processNextNightAction(game);
    }
    
}
