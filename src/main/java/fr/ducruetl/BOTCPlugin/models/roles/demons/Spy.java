package fr.ducruetl.BOTCPlugin.models.roles.demons;

import fr.ducruetl.BOTCPlugin.models.Game;
import fr.ducruetl.BOTCPlugin.models.GamePlayer;
import fr.ducruetl.BOTCPlugin.models.Team;
import fr.ducruetl.BOTCPlugin.models.roles.Role;

public class Spy extends Role {

    public Spy() {
        super(
            "Espion",
            "Lors de la première nuit, tu pourra voir les rôles de tout le monde. "
            + "De plus, tu aura un rôle Citoyen/Etranger de façade, les rôles à informations "
            + "te verront donc comme ce rôle au lieu d'Espion.",
            Team.MINION,
            2
        );
    }

    @Override
    public void onNightTurn(Game game, GamePlayer player) {
        game.processNextNightAction();
    }
    
}
