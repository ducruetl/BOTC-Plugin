package fr.ducruetl.BOTCPlugin.gameobjects.roles.outsiders;

import fr.ducruetl.BOTCPlugin.gameobjects.Game;
import fr.ducruetl.BOTCPlugin.gameobjects.GamePlayer;
import fr.ducruetl.BOTCPlugin.gameobjects.NightActions;
import fr.ducruetl.BOTCPlugin.gameobjects.Team;
import fr.ducruetl.BOTCPlugin.gameobjects.roles.Role;

public class Drunk extends Role {

    public Drunk() {
        super(
            "Ivrogne",
            "Tu ne sait pas que tu es ivrogne, au début de la partie, "
            + "un rôle t'es assigné mais tu n'auras pas ses pouvoirs, "
            + "si ton rôle attribué est censé recevoir des informations, "
            + "tu recevra des informations aléatoires.",
            Team.OUTSIDER,
            2
        );
    }

    @Override
    public void onNightTurn(Game game, GamePlayer player) {
        NightActions.processNextNightAction(game);
    }
    
}
