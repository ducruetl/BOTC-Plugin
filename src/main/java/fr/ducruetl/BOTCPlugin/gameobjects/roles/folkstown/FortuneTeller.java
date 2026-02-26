package fr.ducruetl.BOTCPlugin.gameobjects.roles.folkstown;

import fr.ducruetl.BOTCPlugin.gameobjects.Game;
import fr.ducruetl.BOTCPlugin.gameobjects.GamePlayer;
import fr.ducruetl.BOTCPlugin.gameobjects.NightActions;
import fr.ducruetl.BOTCPlugin.gameobjects.Team;
import fr.ducruetl.BOTCPlugin.gameobjects.roles.Role;

public class FortuneTeller extends Role {

    public FortuneTeller() {
        super(
            "Voyante",
            "Chaque nuit, tu choisis 2 joueurs, tu recevra l'information de si "
            + "il y a le diablotin parmis eux ou non. Attention, il y a également un "
            + "citoyen dans la partie que tu verra aussi comme diablotin.",
            Team.TOWNSFOLK,
            2
        );
    }

    @Override
    public void onNightTurn(Game game, GamePlayer player) {
        NightActions.processNextNightAction(game);
    }
    
}
