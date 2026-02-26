package fr.ducruetl.BOTCPlugin.gameobjects.roles.demons;

import fr.ducruetl.BOTCPlugin.gameobjects.Game;
import fr.ducruetl.BOTCPlugin.gameobjects.GamePlayer;
import fr.ducruetl.BOTCPlugin.gameobjects.NightActions;
import fr.ducruetl.BOTCPlugin.gameobjects.Team;
import fr.ducruetl.BOTCPlugin.gameobjects.roles.Role;

public class Poisoner extends Role {

    public Poisoner() {
        super(
            "Empoisonneur", 
            "Chaque nuit, tu peut empoisonner un joueur, ce qui a "
            + "pour effet de lui retirer son pouvoir jusqu'à la prochaine nuit. "
            + "Si il était censé recevoir une information, il en recevra une aléatoire.", 
            Team.MINION,
            1
        );
    }

    @Override
    public void onNightTurn(Game game, GamePlayer player) {
        NightActions.processNextNightAction(game);
    }
    
}
