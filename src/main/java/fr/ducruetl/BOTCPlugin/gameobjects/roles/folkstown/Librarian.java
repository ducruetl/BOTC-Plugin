package fr.ducruetl.BOTCPlugin.gameobjects.roles.folkstown;

import fr.ducruetl.BOTCPlugin.gameobjects.Game;
import fr.ducruetl.BOTCPlugin.gameobjects.GamePlayer;
import fr.ducruetl.BOTCPlugin.gameobjects.NightActions;
import fr.ducruetl.BOTCPlugin.gameobjects.Team;
import fr.ducruetl.BOTCPlugin.gameobjects.roles.Role;

public class Librarian extends Role {

    public Librarian() {
        super(
            "Libraire",
            "Au début de la partie, tu connaitras un rôle étranger "
            + "et 2 personnes, une de ces personnes possède ce rôle.",
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
