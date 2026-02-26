package fr.ducruetl.BOTCPlugin.gameobjects.roles.demons;

import fr.ducruetl.BOTCPlugin.gameobjects.Game;
import fr.ducruetl.BOTCPlugin.gameobjects.GamePlayer;
import fr.ducruetl.BOTCPlugin.gameobjects.NightActions;
import fr.ducruetl.BOTCPlugin.gameobjects.Team;
import fr.ducruetl.BOTCPlugin.gameobjects.roles.Role;

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
        NightActions.processNextNightAction(game);
    }
    
}
