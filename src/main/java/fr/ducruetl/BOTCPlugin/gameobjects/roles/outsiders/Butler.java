package fr.ducruetl.BOTCPlugin.gameobjects.roles.outsiders;

import fr.ducruetl.BOTCPlugin.gameobjects.Game;
import fr.ducruetl.BOTCPlugin.gameobjects.GamePlayer;
import fr.ducruetl.BOTCPlugin.gameobjects.NightActions;
import fr.ducruetl.BOTCPlugin.gameobjects.Team;
import fr.ducruetl.BOTCPlugin.gameobjects.roles.Role;

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
        NightActions.processNextNightAction(game);
    }
    
}