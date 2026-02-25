package fr.ducruetl.BOTCPlugin.models.roles.demons;

import fr.ducruetl.BOTCPlugin.models.Game;
import fr.ducruetl.BOTCPlugin.models.GamePlayer;
import fr.ducruetl.BOTCPlugin.models.Team;
import fr.ducruetl.BOTCPlugin.models.roles.Role;

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
        game.processNextNightAction();
    }
    
}
