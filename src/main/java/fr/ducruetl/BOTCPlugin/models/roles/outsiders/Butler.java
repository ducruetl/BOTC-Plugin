package fr.ducruetl.BOTCPlugin.models.roles.outsiders;

import fr.ducruetl.BOTCPlugin.models.Game;
import fr.ducruetl.BOTCPlugin.models.GamePlayer;
import fr.ducruetl.BOTCPlugin.models.Team;
import fr.ducruetl.BOTCPlugin.models.roles.Role;

public class Butler extends Role {

    public Butler() {
        super(
            "Majordome",
            "Chaque nuit, tu choisis une personne, le lendemain, tu peut "
            + "seulement voter si cette personne vote également.",
            Team.OUTSIDER,
            1
        );
    }

    @Override
    public void onNightTurn(Game game, GamePlayer player) {
        game.processNextNightAction();
    }
    
}