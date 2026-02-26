package fr.ducruetl.BOTCPlugin.gameobjects.roles.folkstown;

import fr.ducruetl.BOTCPlugin.gameobjects.Game;
import fr.ducruetl.BOTCPlugin.gameobjects.GamePlayer;
import fr.ducruetl.BOTCPlugin.gameobjects.NightActions;
import fr.ducruetl.BOTCPlugin.gameobjects.Team;
import fr.ducruetl.BOTCPlugin.gameobjects.roles.Role;

public class Chef extends Role {

    public Chef() {
        super(
            "Chef",
            "Au début de la partie, tu recevra le nombre de paires de démons "
            + "qu'il y a autour de la table, 1 paire signifie qu'il y a 2 démons côte à côte.",
            Team.TOWNSFOLK,
            2
        );
    }

    @Override
    public void onNightTurn(Game game, GamePlayer player) {
        if (game.getDay() != 1) {
            NightActions.processNextNightAction(game);
            return;
        }

        NightActions.processNextNightAction(game);
    }
    
}
